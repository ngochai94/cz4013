package cz4013.server.rpc;

import cz4013.shared.container.LruCache;
import cz4013.shared.request.RequestHeader;
import cz4013.shared.response.Response;
import cz4013.shared.response.Status;
import cz4013.shared.rpc.RawMessage;
import cz4013.shared.serialization.SerializingException;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static cz4013.shared.serialization.Deserializer.deserialize;

/**
 * This class contains information of how to route the requests
 * It will cache the response and return the old response for duplicated request
 */
public class Router {
  private Map<String, Route> routes = new HashMap<>();
  private LruCache<UUID, Response<?>> cache;

  public Router(LruCache<UUID, Response<?>> cache) {
    this.cache = cache;
  }

  /**
   * Bind a handler with a request method
   * This handler take 1 input which is the request body
   *
   * @param method method name to bind
   * @param handler handler for the corresponding method
   * @param reqBody placeholder to deserialize the request body
   * @param <ReqBody> type of request body
   * @param <RespBody> type of response body
   * @return this router after binding
   */
  public <ReqBody, RespBody> Router bind(
    String method,
    Function<ReqBody, RespBody> handler,
    Object reqBody
  ) {
    routes.put(method, new Route(
      reqBody,
      (req, remote) -> ((Function<Object, Object>)handler).apply(req)
    ));
    return this;
  }

  /**
   * Bind a handler with a request method
   * This handler take 2 inputs which are the request body and the client's address
   *
   * @param method method name to bind
   * @param handler handler for the corresponding method
   * @param reqBody placeholder to deserialize the request body
   * @param <ReqBody> type of request body
   * @param <RespBody> type of response body
   * @return this router after binding
   */
  public <ReqBody, RespBody> Router bind(
    String method,
    BiFunction<ReqBody, SocketAddress, RespBody> handler,
    Object reqBody
  ) {
    routes.put(method, new Route(
      reqBody,
      (req, remote) -> ((BiFunction<Object, SocketAddress, Object>) handler).apply(req, remote)
    ));
    return this;
  }

  private Response<?> routeUncached(RawMessage req, RequestHeader header) {
    try {
      Route route = routes.get(header.method);
      if (route == null) {
        return Response.failed(header.uuid, Status.NOT_FOUND);
      }

      Object body = deserialize(route.reqBody, req.payload.get().slice());
      Object respBody = route.handler.apply(body, req.remote);
      return Response.ok(header.uuid, respBody);
    } catch (SerializingException e) {
      return Response.failed(header.uuid, Status.MALFORMED);
    } catch (Exception e) {
      System.out.print(header.uuid);
      e.printStackTrace();
      return Response.failed(header.uuid, Status.INTERNAL_ERR);
    }
  }

  /**
   * Route a request
   *
   * @param req request
   * @return response
   */
  public Response<?> route(RawMessage req) {
    RequestHeader header = deserialize(new RequestHeader() {}, req.payload.get());
    return cache.get(header.uuid).orElseGet(() -> {
      Response<?> resp = routeUncached(req, header);
      cache.put(header.uuid, resp);
      return resp;
    });
  }
}
