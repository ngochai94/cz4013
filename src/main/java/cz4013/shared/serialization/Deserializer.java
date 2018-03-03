package cz4013.shared.serialization;

import one.util.streamex.StreamEx;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static cz4013.shared.serialization.Utils.serializableFields;

public class Deserializer {
  public static Map<String, Class> EMPTY_TYPE_MAP = new HashMap<>();

  /**
   * Deserializes an object from a buffer.
   *
   * If the object is an instance of a generic class, it must be created with anonymous
   * class syntax due to type erasure. For example:
   * ```
   * class Clazz<T> {}
   *
   * // INCORRECT
   * deserialize(new Clazz<Integer>(), buffer);
   *
   * // Correct (note the extra {})
   * deserialize(new Clazz<Integer>() {}, buffer);
   * ```
   *
   * @param obj the object to be deserialized into
   * @param buf the buffer to read from
   * @param <T> the object type
   * @return the deserialized object
   * @throws SerializingException if the data is corrupted or the object's class violates the
   *                              auto serialization constraints
   */
  public static <T> T deserialize(T obj, ByteBuffer buf) {
    buf.order(ByteOrder.LITTLE_ENDIAN);
    buf.clear();
    try {
      return (T) readStruct(obj.getClass().getGenericSuperclass(), buf, EMPTY_TYPE_MAP);
    } catch (BufferOverflowException e) {
      throw new SerializingException("Corrupted data", e);
    }
  }

  public static Object readStruct(Type ty, ByteBuffer buf, Map<String, Class> parentTypeMap) {
    Class<?> clazz;
    Map<String, Class> typeMap = EMPTY_TYPE_MAP;
    if (ty instanceof ParameterizedType) {
      ParameterizedType pty = (ParameterizedType) ty;
      clazz = ((Class) pty.getRawType());
      typeMap = makeTypeMap(pty, parentTypeMap);
    } else {
      clazz = (Class<?>) ty;
    }

    Object obj = newInstance(clazz);
    if (obj instanceof Serializable) {
      ((Serializable) obj).deserialize(buf);
    } else {
      Map<String, Class> finalTypeMap = typeMap;
      serializableFields(clazz)
        .forEach(field -> read(obj, field, buf, finalTypeMap));
    }

    return obj;
  }

  private static <T> T newInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      assert false : "Cannot create an instance of " + clazz.getName() + ". " +
        "Make sure the class is concrete and has a nullary constructor.";
    } catch (IllegalAccessException e) {
      assert false : "Unable to access " + clazz.getName() +
        ". Make sure the class is declared public.";
    }

    throw new RuntimeException("We should never reach here.");
  }

  public static Object read(Type ty, ByteBuffer buf, Map<String, Class> typeMap) {
    if (ty == Byte.TYPE || ty == Byte.class) {
      return buf.get();
    }

    if (ty == Boolean.TYPE || ty == Boolean.class) {
      Byte b = buf.get();
      switch (b) {
        case 1:
          return true;

        case 0:
          return false;

        default:
          throw new SerializingException(
            String.format("Unexpected byte %d while reading a bool value at offset %d", b, buf.position())
          );
      }
    }

    if (ty == Short.TYPE || ty == Short.class) {
      return buf.getShort();
    }

    if (ty == Character.TYPE || ty == Character.class) {
      return buf.getChar();
    }

    if (ty == Integer.TYPE || ty == Integer.class) {
      return buf.getInt();
    }

    if (ty == Float.TYPE || ty == Float.class) {
      return buf.getFloat();
    }

    if (ty == Long.TYPE || ty == Long.class) {
      return buf.getLong();
    }

    if (ty == Double.TYPE || ty == Double.class) {
      return buf.getDouble();
    }

    if (ty == String.class) {
      int len = buf.getInt();
      byte[] utf8 = new byte[len];
      buf.get(utf8);
      return new String(utf8, StandardCharsets.UTF_8);
    }

    if (ParameterizedType.class.isAssignableFrom(ty.getClass())) {
      ParameterizedType pty = (ParameterizedType) ty;
      Class<?> clazz = (Class<?>) pty.getRawType();

      if (Optional.class.isAssignableFrom(clazz)) {
        Byte b = buf.get();
        switch (b) {
          case 0:
            return Optional.empty();

          case 1:
            return Optional.of(read(
              resolveGenericType(pty.getActualTypeArguments()[0], typeMap),
              buf,
              typeMap
            ));

          default:
            throw new SerializingException(
              String.format("Unexpected tag %d while reading an optional value at offset %d", b, buf.position())
            );
        }
      }

      if (Collection.class.isAssignableFrom(clazz)) {
        Collection<Object> xs = ((Collection<Object>) newInstance(clazz));
        int len = buf.getInt();
        int pos = buf.position();

        for (; buf.position() - pos < len; xs.add(read(resolveGenericType(pty.getActualTypeArguments()[0], typeMap), buf, typeMap)))
          ;

        int read = buf.position() - pos;
        if (read != len) {
          throw new SerializingException(
            String.format(
              "Mismatch collection length, expected %d bytes, read %d bytes at offset %d.",
              len,
              len,
              read,
              buf.position()
            )
          );
        }
        return xs;
      }

      return readStruct(ty, buf, typeMap);
    }

    Class<?> clazz = (Class) ty;
    if (clazz.isEnum()) {
      int i = buf.get();
      Object[] cs = clazz.getEnumConstants();
      if (i < 0 || i > cs.length) {
        throw new SerializingException(
          String.format("Invalid ordinal %d of %s at offset %d.", i, clazz.getName(), buf.position())
        );
      }

      return cs[i];
    }

    return readStruct(ty, buf, typeMap);
  }

  public static void read(Object obj, Field field, ByteBuffer buf, Map<String, Class> typeMap) {
    try {
      Type ty = field.getGenericType();
      if (ty instanceof TypeVariable) {
        ty = typeMap.get(((TypeVariable) ty).getName());
      }
      Object x = read(ty, buf, typeMap);
      if (ty == Byte.TYPE) {
        field.setByte(obj, (Byte) x);
      } else if (ty == Boolean.TYPE) {
        field.setBoolean(obj, (Boolean) x);
      } else if (ty == Short.TYPE) {
        field.setShort(obj, (Short) x);
      } else if (ty == Character.TYPE) {
        field.setChar(obj, (Character) x);
      } else if (ty == Integer.TYPE) {
        field.setInt(obj, (Integer) x);
      } else if (ty == Float.TYPE) {
        field.setFloat(obj, (Float) x);
      } else if (ty == Long.TYPE) {
        field.setLong(obj, (Long) x);
      } else if (ty == Double.TYPE) {
        field.setDouble(obj, (Double) x);
      } else {
        field.set(obj, x);
      }
    } catch (IllegalAccessException e) {
      assert false : "Unable to write to field " + field.getName() + " of " + obj.getClass().getName();
    }
  }

  /**
   * Makes a map from type parameters to type arguments of a `ParameterizedType`.
   *
   * Example:
   * ```
   * class Clazz<T, U, V> {}
   *
   * makeTypeMap((ParameterizedType) new Clazz<Integer, Boolean, Float>() {}.getClass().getGenericSuperclass(), EMPTY_TYPE_MAP);
   * // => {T=java.lang.Integer, U=java.lang.Boolean, V=java.lang.Float}
   * ```
   *
   * @param ty            the type
   * @param parentTypeMap the type map of the generic class containing this `ParameterizedType`.
   * @return the map
   */
  public static Map<String, Class> makeTypeMap(ParameterizedType pty, Map<String, Class> parentTypeMap) {
    return StreamEx.of(((Class<?>) pty.getRawType()).getTypeParameters())
      .map(TypeVariable::getName)
      .zipWith(StreamEx.of(pty.getActualTypeArguments())
        .map(arg -> (arg instanceof Class) ? (Class) arg : parentTypeMap.get(((TypeVariable) arg).getName()))
      )
      .toMap();
  }

  public static Type resolveGenericType(Type ty, Map<String, Class> typeMap) {
    if (ty instanceof TypeVariable) {
      return typeMap.get(((TypeVariable) ty).getName());
    }
    return ty;
  }
}
