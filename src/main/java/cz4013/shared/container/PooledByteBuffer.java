package cz4013.shared.container;

import java.nio.ByteBuffer;

/**
 * A wrapper around {@link ByteBuffer} coming from a {@link PooledByteBuffer} which supports
 * the try-with-resource idiom.
 */
public class PooledByteBuffer implements AutoCloseable {
  private BufferPool parent;
  private ByteBuffer buf;

  public PooledByteBuffer(BufferPool parent, ByteBuffer buf) {
    this.parent = parent;
    this.buf = buf;
  }

  /**
   * Returns the underlying {@link ByteBuffer}.
   * @return the {@link ByteBuffer}.
   */
  public ByteBuffer get() {
    return buf;
  }

  @Override
  public void close() {
    parent.putBack(buf);
  }
}
