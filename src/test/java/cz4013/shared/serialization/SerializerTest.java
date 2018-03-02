package cz4013.shared.serialization;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
    Class2<Integer, Boolean> original = new Class2<>(
      1l,
      (short) 2,
      3,
      "asdf",
      new ArrayList<>(Arrays.asList(
        Optional.of(new Class1<>('a', false, Enum1.A, Optional.of(new ArrayList<>(Arrays.asList(12, 42))), true)),
        Optional.empty(),
        Optional.of(new Class1<>('b', true, Enum1.B, Optional.empty(), false))
      )),
      true
    );
    ByteBuffer b = Serializer.serialize(original, ByteBuffer.allocate(8192));
    Class2<Integer, Boolean> deserialized = Deserializer.deserialize(new Class2<Integer, Boolean>() {}, b);
    assertEquals(original, deserialized);
  }
}

class Class1<V, W> {
  public char x;
  public boolean y;
  public Enum1 z;
  public Optional<ArrayList<V>> t;
  public W u;

  public Class1() {
  }

  public Class1(char x, boolean y, Enum1 z, Optional<ArrayList<V>> t, W u) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.t = t;
    this.u = u;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Class1<?, ?> class1 = (Class1<?, ?>) o;
    return x == class1.x &&
      y == class1.y &&
      z == class1.z &&
      Objects.equals(t, class1.t) &&
      Objects.equals(u, class1.u);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z, t, u);
  }
}

class Class2<T, U> {
  public long x;
  public short y;
  public int z;
  public String s;
  public ArrayList<Optional<Class1<T, U>>> class1;
  public U u;

  public Class2() {}

  public Class2(long x, short y, int z, String s, ArrayList<Optional<Class1<T, U>>> class1, U u) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.s = s;
    this.class1 = class1;
    this.u = u;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Class2<?, ?> class2 = (Class2<?, ?>) o;
    return x == class2.x &&
      y == class2.y &&
      z == class2.z &&
      Objects.equals(s, class2.s) &&
      Objects.equals(class1, class2.class1) &&
      Objects.equals(u, class2.u);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z, s, class1, u);
  }
}
