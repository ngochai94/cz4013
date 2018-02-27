package cz4013.shared.serialization;

import cz4013.shared.currency.Currency;
import cz4013.shared.request.CloseAccountRequest;
import cz4013.shared.request.OpenAccountRequest;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class RequestSerializeDeserializeTest {
  @Test
  public void OpenAccountRequestTest() {
    OpenAccountRequest request = new OpenAccountRequest("Alice", "123456", Currency.USD, 10);
    ByteBuffer b = Serializer.serialize(request, ByteBuffer.allocate(8192));
    OpenAccountRequest deserialized = Deserializer.deserialize(OpenAccountRequest.class, b);
    assertEquals(request.toString(), deserialized.toString());
  }

  @Test
  public void CloseAccountRequestTest() {
    CloseAccountRequest request = new CloseAccountRequest(123, "Bob", "789012");
    ByteBuffer b = Serializer.serialize(request, ByteBuffer.allocate(8192));
    CloseAccountRequest deserialized = Deserializer.deserialize(CloseAccountRequest.class, b);
    assertEquals(request.toString(), deserialized.toString());
  }
}
