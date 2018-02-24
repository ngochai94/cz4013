package cz4013.shared.serialization;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

enum Enum1 {
  A,
  B,
  C
}

public class SerializerTest {
  @Test
  public void serialize() {
    Class2 original = new Class2(
      1l,
      (short) 2,
      3,
      new ArrayList<>(Arrays.asList(
        Optional.of(new Class1('a', false, Enum1.A)),
        Optional.empty(),
        Optional.of(new Class1('b', true, Enum1.B))
      ))
    );
    ByteBuffer b = Serializer.serialize(original, ByteBuffer.allocate(8192));
    Class2 deserialized = Deserializer.deserialize(Class2.class, b);
    assertEquals(original, deserialized);
  }
}

class Class1 {
  public char x;
  public boolean y;
  public Enum1 z;

  public Class1() {
  }

  public Class1(char x, boolean y, Enum1 z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Class1)) return false;
    Class1 class1 = (Class1) o;
    return x == class1.x &&
             y == class1.y &&
             z == class1.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }
}

class Class2 {
  public long x;
  public short y;
  public int z;
  public ArrayList<Optional<Class1>> class1;

  public Class2() {
  }

  public Class2(long x, short y, int z, ArrayList<Optional<Class1>> class1) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.class1 = class1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Class2)) return false;
    Class2 class2 = (Class2) o;
    return x == class2.x &&
             y == class2.y &&
             z == class2.z &&
             Objects.equals(class1, class2.class1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z, class1);
  }

  @Override
  public String toString() {
    return "(Class2 " +

             x +
             " " + y +
             " " + z +
             " " + class1 +
             ')';
  }
}
