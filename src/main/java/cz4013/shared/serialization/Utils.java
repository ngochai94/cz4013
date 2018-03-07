package cz4013.shared.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.stream.Stream;

class Utils {
  static Stream<Field> serializableFields(Class<?> clazz) {
    return Arrays.stream(clazz.getFields())
      .filter(field -> {
        int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers) && !Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers);
      });
  }

  static void resetBuffer(ByteBuffer buf) {
    buf.clear();
    buf.order(ByteOrder.LITTLE_ENDIAN);
  }
}
