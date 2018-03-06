package cz4013.shared.rpc;

import cz4013.shared.container.BufferPool;
import cz4013.shared.container.PooledByteBuffer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

import static cz4013.shared.serialization.Serializer.serialize;

public class Transport {
  DatagramSocket socket;
  BufferPool pool;

  public Transport(DatagramSocket socket, BufferPool pool) {
    this.socket = socket;
    this.pool = pool;
  }

  public RawMessage recv() {
    PooledByteBuffer buf = pool.take();
    byte[] rawBuf = buf.get().array();
    DatagramPacket packet = new DatagramPacket(rawBuf, rawBuf.length);
    try {
      socket.receive(packet);
      return new RawMessage(packet.getSocketAddress(), buf);
    } catch (Exception e) {
      buf.close();
      throw new RuntimeException(e);
    }
  }

  public <T> void send(SocketAddress dest, T obj) {
    try (PooledByteBuffer buf = pool.take()) {
      serialize(obj, buf.get());
      byte[] rawBuf = buf.get().array();
      socket.send(new DatagramPacket(rawBuf, rawBuf.length, dest));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
