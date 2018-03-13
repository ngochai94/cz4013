package cz4013.shared.container;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A fixed-sized pool of {@link ByteBuffer}s.
 */
public class BufferPool {
  private Queue<ByteBuffer> q = new LinkedBlockingQueue<>();

  /**
   * Constructs a new pool with the given capacity.
   *
   * @param bufferSize the size of each buffer
   * @param poolSize   the capacity of the pool
   */
  public BufferPool(int bufferSize, int poolSize) {
    for (int i = 0; i < poolSize; ++i) {
      putBack(ByteBuffer.allocate(bufferSize));
    }
  }

  /**
   * Takes a buffer from the pool. Blocks if the pool is empty.
   * <p>
   * The returned buffer must be released either explicit by calling {@link PooledByteBuffer#close()}
   * or implicitly by using the try-with-resource-idiom:
   * <pre>{@code
   * try (PooledByteBuffer buf = pool.take()) {
   *
   * }
   * }</pre>
   *
   * @return the buffer
   */
  public PooledByteBuffer take() {
    ByteBuffer buf = q.remove();
    buf.clear();
    return new PooledByteBuffer(this, buf);
  }

  void putBack(ByteBuffer buf) {
    q.add(buf);
  }
}
