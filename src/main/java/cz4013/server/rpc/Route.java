package cz4013.server.rpc;

import java.net.SocketAddress;
import java.util.function.BiFunction;

/**
 * This class contains the route for a specific request method
 */
public class Route {
  public Object reqBody;
  public BiFunction<Object, SocketAddress, Object> handler;

  public Route(Object reqBody, BiFunction<Object, SocketAddress, Object> handler) {
    this.reqBody = reqBody;
    this.handler = handler;
  }
}
