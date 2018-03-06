package cz4013.shared.container;

import java.nio.ByteBuffer;

public class PooledByteBuffer implements AutoCloseable {
  private BufferPool parent;
  private ByteBuffer buf;

  public PooledByteBuffer(BufferPool parent, ByteBuffer buf) {
    this.parent = parent;
    this.buf = buf;
  }

  public ByteBuffer get() {
    return buf;
  }

  @Override
  public void close() {
    parent.putBack(buf);
  }
}
