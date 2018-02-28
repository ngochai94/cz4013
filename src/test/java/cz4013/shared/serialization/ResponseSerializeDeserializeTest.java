package cz4013.shared.serialization;

import cz4013.shared.response.CloseAccountResponse;
import cz4013.shared.response.OpenAccountResponse;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class ResponseSerializeDeserializeTest {
  @Test
  public void OpenAccountResponseTest() {
    OpenAccountResponse response = new OpenAccountResponse(123);
    ByteBuffer b = Serializer.serialize(response, ByteBuffer.allocate(8192));
    OpenAccountResponse deserialized = Deserializer.deserialize(OpenAccountResponse.class, b);
    assertEquals(response.toString(), deserialized.toString());
  }

  @Test
  public void CloseAccountResponseTest() {
    CloseAccountResponse response = new CloseAccountResponse(true, "");
    ByteBuffer b = Serializer.serialize(response, ByteBuffer.allocate(8192));
    CloseAccountResponse deserialized = Deserializer.deserialize(CloseAccountResponse.class, b);
    assertEquals(response.toString(), deserialized.toString());
  }
}
