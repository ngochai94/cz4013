package cz4013.server.rpc;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class RawRequest {
  SocketAddress remote;
  ByteBuffer payload;

  public RawRequest(SocketAddress remote, ByteBuffer payload) {
    this.remote = remote;
    this.payload = payload;
  }
}
