package cz4013.shared.container;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class BufferPool {
  private Queue<ByteBuffer> q = new LinkedBlockingQueue<>();

  public BufferPool(int bufferSize, int poolSize) {
    for (int i = 0; i < poolSize; ++i) {
      putBack(ByteBuffer.allocate(bufferSize));
    }
  }

  public PooledByteBuffer take() {
    ByteBuffer buf = q.remove();
    buf.clear();
    return new PooledByteBuffer(this, buf);
  }

  void putBack(ByteBuffer buf) {
    q.add(buf);
  }
}
