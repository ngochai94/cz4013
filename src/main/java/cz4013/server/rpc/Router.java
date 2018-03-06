package cz4013.server.rpc;

import cz4013.shared.request.Header;
import cz4013.shared.response.Response;
import cz4013.shared.response.Status;
import cz4013.shared.serialization.SerializingException;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static cz4013.shared.serialization.Deserializer.deserialize;

public class Router {
  private Map<String, Route> routes = new HashMap<>();

  public <ReqBody, RespBody> Router bind(
    String method,
    Function<ReqBody, RespBody> handler,
    ReqBody reqBody
  ) {
    routes.put(method, new Route(
      reqBody,
      (req, remote) -> handler.apply((ReqBody) req)
    ));
    return this;
  }

  public <ReqBody, RespBody> Router bind(
    String method,
    BiFunction<ReqBody, SocketAddress, RespBody> handler,
    ReqBody reqBody
  ) {
    routes.put(method, new Route(
      reqBody,
      (req, remote) -> handler.apply((ReqBody) req, remote)
    ));
    return this;
  }

  public Response<?> route(RawRequest req) {
    Header header = deserialize(new Header() {}, req.payload);

    try {
      Route route = routes.get(header.method);
      if (route == null) {
        return Response.failed(header.uuid, Status.NOT_FOUND);
      }

      Object body = deserialize(route.reqBody, req.payload.slice());
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
}
