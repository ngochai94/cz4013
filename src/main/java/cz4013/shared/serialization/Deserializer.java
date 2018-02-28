package cz4013.shared.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;

import static cz4013.shared.serialization.Utils.serializableFields;

public class Deserializer {
  /**
   * Deserializes an object from a buffer.
   *
   * @param clazz the class type of the object
   * @param buf   the buffer to read from
   * @param <T>   the object type
   * @return the deserialized object
   * @throws SerializingException if the data is corrupted or the object's class violates the
   *                              auto serialization constraints
   */
  public static <T> T deserialize(Class<T> clazz, ByteBuffer buf) {
    buf.order(ByteOrder.LITTLE_ENDIAN);
    buf.clear();
    try {
      return readStruct(clazz, buf);
    } catch (BufferOverflowException e) {
      throw new SerializingException("Corrupted data", e);
    }
  }

  public static <T> T readStruct(Class<T> clazz, ByteBuffer buf) {
    T obj = newInstance(clazz);
    if (obj instanceof Serializable) {
      ((Serializable) obj).deserialize(buf);
    } else {
      serializableFields(clazz)
        .forEach(field -> read(obj, field, buf));
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

  public static Object read(Type ty, ByteBuffer buf) {
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
            return Optional.of(read(pty.getActualTypeArguments()[0], buf));

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

        for (; buf.position() - pos < len; xs.add(read(pty.getActualTypeArguments()[0], buf)))
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

      throw new SerializingException(
        "Unable to deserialize an object of generic type " + clazz.getName() + ".");
    }

    Class<?> clazz = (Class<?>) ty;
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

    return readStruct((Class<?>) ty, buf);
  }

  public static void read(Object obj, Field field, ByteBuffer buf) {
    try {
      Object x = read(field.getGenericType(), buf);
      if (x instanceof Byte) {
        field.setByte(obj, (Byte) x);
      } else if (x instanceof Boolean) {
        field.setBoolean(obj, (Boolean) x);
      } else if (x instanceof Short) {
        field.setShort(obj, (Short) x);
      } else if (x instanceof Character) {
        field.setChar(obj, (Character) x);
      } else if (x instanceof Integer) {
        field.setInt(obj, (Integer) x);
      } else if (x instanceof Float) {
        field.setFloat(obj, (Float) x);
      } else if (x instanceof Long) {
        field.setLong(obj, (Long) x);
      } else if (x instanceof Double) {
        field.setDouble(obj, (Double) x);
      } else {
        field.set(obj, x);
      }
    } catch (IllegalAccessException e) {
      assert false : "Unable to write to field " + field.getName() + " of " + obj.getClass().getName();
    }
  }
}
