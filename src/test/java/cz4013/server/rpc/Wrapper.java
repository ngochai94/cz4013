package cz4013.server.rpc;

/**
 * A wrapper around `String`, which can be used for request/response body.
 *
 * The reason for this is we can't create an anonymous class inherited from `String` for serialization,
 * as `String` is marked `final`.
 */
public class Wrapper {
  public String x;
  public Wrapper() {}
  public Wrapper(String x) { this.x = x; }
}
