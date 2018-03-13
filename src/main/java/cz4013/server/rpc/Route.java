package cz4013.server.rpc;

import java.net.SocketAddress;
import java.util.function.BiFunction;

/**
 * A route: a method, its request type and its handler.
 */
public class Route {
  public Object reqBody;
  public BiFunction<Object, SocketAddress, Object> handler;

  public Route(Object reqBody, BiFunction<Object, SocketAddress, Object> handler) {
    this.reqBody = reqBody;
    this.handler = handler;
  }
}
