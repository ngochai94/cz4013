package cz4013.server.rpc;

import cz4013.shared.container.BufferPool;
import cz4013.shared.container.LruCache;
import cz4013.shared.container.PooledByteBuffer;
import cz4013.shared.request.Header;
import cz4013.shared.request.Request;
import cz4013.shared.response.Response;
import cz4013.shared.response.Status;
import cz4013.shared.rpc.RawMessage;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.function.Function;

import static cz4013.shared.serialization.Serializer.serialize;
import static org.junit.Assert.*;

public class RouterTest {

  @Test
  public void testOk() {
    String method = "ok";
    Function<Wrapper, Wrapper> echoHandler = x -> x;
    Router r = new Router(new LruCache<>(0))
      .bind(method, echoHandler, new Wrapper() {});
    BufferPool pool = new BufferPool(8192, 1);

    Request<Wrapper> req = new Request<>(
      new Header(UUID.randomUUID(), method),
      new Wrapper("request body")
    );

    try (PooledByteBuffer buf = pool.take()) {
      serialize(req, buf.get());
      Response<Wrapper> resp = (Response<Wrapper>) r.route(new RawMessage(new InetSocketAddress(80), buf));

      assertEquals(Status.OK, resp.header.status);
      assertEquals(req.header.uuid, resp.header.uuid);
      assertEquals(req.body.x, resp.body.get().x);
    }
  }

  @Test
  public void testNotFound() {
    Router r = new Router(new LruCache<>(0));
    Request<Wrapper> req = new Request<>(
      new Header(UUID.randomUUID(), "notfound"),
      new Wrapper("")
    );
    BufferPool pool = new BufferPool(8192, 1);

    try (PooledByteBuffer buf = pool.take()) {
      serialize(req, buf.get());
      Response resp = r.route(new RawMessage(new InetSocketAddress(80), buf));

      assertEquals(Status.NOT_FOUND, resp.header.status);
      assertEquals(req.header.uuid, resp.header.uuid);
      assertFalse(resp.body.isPresent());
    }
  }

  @Test
  public void testCached() {
    String method = "cached";
    Function<Wrapper, Wrapper> echoHandler = x -> x;
    Router r = new Router(new LruCache<>(1))
      .bind(method, echoHandler, new Wrapper() {});
    BufferPool pool = new BufferPool(8192, 1);

    Request<Wrapper> req = new Request<>(
      new Header(UUID.randomUUID(), method),
      new Wrapper("request body")
    );

    try (PooledByteBuffer buf = pool.take()) {
      serialize(req, buf.get());
      Response resp1 = r.route(new RawMessage(new InetSocketAddress(80), buf));
      Response resp2 = r.route(new RawMessage(new InetSocketAddress(80), buf));
      assertSame(resp1, resp2);
    }
  }
}
