package cz4013.shared.serialization;

import cz4013.shared.response.*;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class ResponseSerializeDeserializeTest {
  @Test
  public void OpenAccountResponseTest() {
    OpenAccountResponse response = new OpenAccountResponse(123);
    ByteBuffer b = Serializer.serialize(response, ByteBuffer.allocate(8192));
    OpenAccountResponse deserialized = Deserializer.deserialize(new OpenAccountResponse() {}, b);
    assertEquals(response.toString(), deserialized.toString());
  }

  @Test
  public void CloseAccountResponseTest() {
    CloseAccountResponse response = new CloseAccountResponse(true, "");
    ByteBuffer b = Serializer.serialize(response, ByteBuffer.allocate(8192));
    CloseAccountResponse deserialized = Deserializer.deserialize(new CloseAccountResponse() {}, b);
    assertEquals(response.toString(), deserialized.toString());
  }

  @Test
  public void DepositResponseTest() {
    DepositResponse response = new DepositResponse(10, true, "");
    ByteBuffer b = Serializer.serialize(response, ByteBuffer.allocate(8192));
    DepositResponse deserialized = Deserializer.deserialize(new DepositResponse() {}, b);
    assertEquals(response.toString(), deserialized.toString());
  }

  @Test
  public void MonitorStatusResponseTest() {
    MonitorStatusResponse response = new MonitorStatusResponse(true);
    ByteBuffer b = Serializer.serialize(response, ByteBuffer.allocate(8192));
    MonitorStatusResponse deserialized = Deserializer.deserialize(new MonitorStatusResponse() {}, b);
    assertEquals(response.toString(), deserialized.toString());
  }

  @Test
  public void MonitorUpdateResponseTest() {
    MonitorUpdateResponse response = new MonitorUpdateResponse("asdf");
    ByteBuffer b = Serializer.serialize(response, ByteBuffer.allocate(8192));
    MonitorUpdateResponse deserialized = Deserializer.deserialize(new MonitorUpdateResponse() {}, b);
    assertEquals(response.toString(), deserialized.toString());
  }
}
