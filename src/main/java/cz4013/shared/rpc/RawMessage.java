package cz4013.shared.rpc;

import cz4013.shared.container.PooledByteBuffer;

import java.net.SocketAddress;

/**
 * A UDP datagram: its sender and its content.
 */
public class RawMessage implements AutoCloseable {
  public SocketAddress remote;
  public PooledByteBuffer payload;

  public RawMessage(SocketAddress remote, PooledByteBuffer payload) {
    this.remote = remote;
    this.payload = payload;
  }

  @Override
  public void close() {
    payload.close();
  }
}
