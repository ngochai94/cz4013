package cz4013.server.rpc;

import cz4013.shared.request.Header;
import cz4013.shared.request.Request;
import cz4013.shared.response.Response;
import cz4013.shared.response.Status;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.function.Function;

import static cz4013.shared.serialization.Serializer.serialize;
import static org.junit.Assert.*;

public class RouterTest {
  @Test
  public void testOk() {
    String method = "ok";
    Function<Wrapper, Wrapper> echoHandler = x -> x;
    Router r = new Router()
      .bind(method, echoHandler, new Wrapper() {});

    Request<Wrapper> req = new Request<>(
      new Header(UUID.randomUUID(), method),
      new Wrapper("request body")
    );

    Response<Wrapper> resp = (Response<Wrapper>) r.route(new RawRequest(
      new InetSocketAddress(80),
      serialize(req, ByteBuffer.allocate(8192))
    ));

    assertEquals(Status.OK, resp.header.status);
    assertEquals(req.header.uuid, resp.header.uuid);
    assertEquals(req.body.x, resp.body.get().x);
  }

  @Test
  public void testNotFound() {
    Router r = new Router();
    Request<Wrapper> req = new Request<>(
      new Header(UUID.randomUUID(), "notfound"),
      new Wrapper("")
    );

    Response resp = r.route(new RawRequest(
      new InetSocketAddress(80), serialize(req, ByteBuffer.allocate(8192))
    ));

    assertEquals(Status.NOT_FOUND, resp.header.status);
    assertEquals(req.header.uuid, resp.header.uuid);
    assertFalse(resp.body.isPresent());
  }
}

class A{}
