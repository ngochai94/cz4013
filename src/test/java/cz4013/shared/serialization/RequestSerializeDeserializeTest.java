package cz4013.shared.serialization;

import cz4013.shared.currency.Currency;
import cz4013.shared.request.*;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class RequestSerializeDeserializeTest {
  @Test
  public void OpenAccountRequestTest() {
    OpenAccountRequest request = new OpenAccountRequest("Alice", "123456", Currency.USD, 10);
    ByteBuffer b = Serializer.serialize(request, ByteBuffer.allocate(8192));
    OpenAccountRequest deserialized = Deserializer.deserialize(new OpenAccountRequest() {}, b);
    assertEquals(request.toString(), deserialized.toString());
  }

  @Test
  public void CloseAccountRequestTest() {
    CloseAccountRequest request = new CloseAccountRequest("Bob", 123, "789012");
    ByteBuffer b = Serializer.serialize(request, ByteBuffer.allocate(8192));
    CloseAccountRequest deserialized = Deserializer.deserialize(new CloseAccountRequest() {}, b);
    assertEquals(request.toString(), deserialized.toString());
  }

  @Test
  public void DepositRequestTest() {
    DepositRequest request = new DepositRequest("Bob", 123, "789012", Currency.USD, 10);
    ByteBuffer b = Serializer.serialize(request, ByteBuffer.allocate(8192));
    DepositRequest deserialized = Deserializer.deserialize(new DepositRequest() {}, b);
    assertEquals(request.toString(), deserialized.toString());
  }

  @Test
  public void MonitorRequestTest() {
    MonitorRequest request = new MonitorRequest(12);
    ByteBuffer b = Serializer.serialize(request, ByteBuffer.allocate(8192));
    MonitorRequest deserialized = Deserializer.deserialize(new MonitorRequest() {}, b);
    assertEquals(request.toString(), deserialized.toString());
  }

  @Test
  public void QueryRequestTest() {
    QueryRequest request = new QueryRequest(1, "asdf");
    ByteBuffer b = Serializer.serialize(request, ByteBuffer.allocate(8192));
    QueryRequest deserialized = Deserializer.deserialize(new QueryRequest() {}, b);
    assertEquals(request.toString(), deserialized.toString());
  }

  @Test
  public void PayMaintenanceFeeRequestTest() {
    PayMaintenanceFeeRequest request = new PayMaintenanceFeeRequest(1, "asdf", "qwer");
    ByteBuffer b = Serializer.serialize(request, ByteBuffer.allocate(8192));
    PayMaintenanceFeeRequest deserialized = Deserializer.deserialize(new PayMaintenanceFeeRequest() {}, b);
    assertEquals(request.toString(), deserialized.toString());
  }
}
