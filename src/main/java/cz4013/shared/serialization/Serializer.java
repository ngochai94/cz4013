package cz4013.shared.serialization;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static cz4013.shared.serialization.Utils.resetBuffer;
import static cz4013.shared.serialization.Utils.serializableFields;

/**
 * Serializes an object.
 *
 * An object is serialized field-by-field, in the order declared in the object's class definition.
 *
 * For auto (de)serialization to work, all fields must have a concrete class type
 * (ex, `List<Integer>` won't work, use `ArrayList<Integer>` and concrete implementations of
 * `List<T>` instead).
 *
 * A field is serialized in different ways, depending on its type:
 * - If the field is a `Serializable` , its `serialize` method is called;
 * - If the field is of one of the 8 primitive types (boolean, byte, short, int, long, char, float,
 * double) it is serialized in the same representation as in memory (which takes 1, 1,
 * 2, 4, 8, 2, 4, 8, bytes respectively), but in little-endian byte order;
 * - If the field is an enum, it is serialized using 1 byte representing its ordinal;
 * - If the field is a `String`, it is serialized as UTF-8, prefixed with a 4-byte integer
 * representing the number of bytes in its UTF-8 representation;
 * - If the field is a `Optional<T>`, it is serialized by 1 byte, which is either 0 or 1, to
 * determine the presence of the contained value, followed by the serialization of T (if is present).
 * - If the field a `Container<T>`, it is serialized by 4 byte, representing the total number of bytes
 * needed to serialize all of its elements, followed by the serialized of its elements;
 * - Otherwise, the field is treated as an object containing subfields, which is serialized recursively
 * according to the rules above.
 */
public class Serializer {
  /**
   * Serializes an object into a ByteBuffer. It is the caller's resposibility to make sure the buffer
   * is big enough.
   *
   * @param obj the object to be serialized
   * @param buf the buffer to write to
   * @return the given buffer
   * @throws SerializingException - if the buffer is not big enough
   */
  public static ByteBuffer serialize(Object obj, ByteBuffer buf) {
    resetBuffer(buf);
    try {
      writeStruct(obj, buf);
      return buf;
    } catch (BufferOverflowException e) {
      throw new SerializingException("Buffer not big enough.", e);
    }
  }

  public static void writeStruct(Object obj, ByteBuffer buf) {
    int pos = buf.position();

    if (obj instanceof Serializable) {
      ((Serializable) obj).serialize(buf);
    } else {
      serializableFields(obj.getClass())
        .forEach(field -> {
          try {
            write(field.get(obj), buf);
          } catch (IllegalAccessException e) {
            assert false : "Field " + field.getName() + " of " + obj.getClass().getName()
              + " is not accessible.";
          }
        });
    }

    // If `buf` didn't advance, `obj` doesn't contain serializable fields.
    assert pos < buf.position() : obj.getClass().getName() + " is not serializable.";
  }

  public static void write(Object x, ByteBuffer buf) {
    if (x.getClass().isEnum()) {
      Enum<?> e = (Enum<?>) x;
      buf.put((byte) e.ordinal());
    } else if (x instanceof Byte) {
      buf.put((Byte) x);
    } else if (x instanceof Boolean) {
      buf.put((byte) ((Boolean) x ? 1 : 0));
    } else if (x instanceof Character) {
      buf.putChar((Character) x);
    } else if (x instanceof Short) {
      buf.putShort((Short) x);
    } else if (x instanceof Integer) {
      buf.putInt((Integer) x);
    } else if (x instanceof Float) {
      buf.putFloat((Float) x);
    } else if (x instanceof Long) {
      buf.putLong((Long) x);
    } else if (x instanceof Double) {
      buf.putDouble((Double) x);
    } else if (x instanceof String) {
      byte[] utf8 = ((String) x).getBytes(StandardCharsets.UTF_8);
      buf.putInt(utf8.length);
      buf.put(utf8);
    } else if (x instanceof Optional) {
      write(((Optional) x), buf);
    } else if (x instanceof Iterable<?>) {
      write(((Iterable<?>) x), buf);
    } else if (x instanceof UUID) {
      write((UUID) x, buf);
    } else {
      writeStruct(x, buf);
    }
  }

  public static void write(Optional<?> x, ByteBuffer buf) {
    if (!x.isPresent()) {
      buf.put((byte) 0);
    } else {
      buf.put((byte) 1);
      write(x.get(), buf);
    }
  }

  public static <T> void write(Iterable<T> xs, ByteBuffer buf) {
    int i = buf.position();
    // Write 0 as the placeholder for the length of the length-prefixed sequence, then fix it later.
    buf.putInt(0);

    // Write the elements.
    xs.forEach(x -> write(x, buf));

    // Fix length.
    int len = buf.position() - i - 4; // Subtract 4 to exclude the placeholder.
    buf.putInt(i, len);
  }

  public static void write(UUID x, ByteBuffer buf) {
    buf.putLong(x.getMostSignificantBits());
    buf.putLong(x.getLeastSignificantBits());
  }
}
