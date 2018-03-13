package cz4013.shared.rpc;

import cz4013.shared.container.BufferPool;
import cz4013.shared.container.PooledByteBuffer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

import static cz4013.shared.serialization.Serializer.serialize;

/**
 * A UDP client.
 */
public class Transport {
  DatagramSocket socket;
  BufferPool pool;

  /**
   * Constructs a {@code Transport} with a associated socket and a {@link BufferPool} which is
   * used to read datagrams.
   *
   * @param socket the socket
   * @param pool   the pool
   */
  public Transport(DatagramSocket socket, BufferPool pool) {
    this.socket = socket;
    this.pool = pool;
  }

  /**
   * Blocks until a datagram from the UDP socket arrives.
   *
   * @return the datagram
   */
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

  /**
   * Serializes and sends a UDP datagram over the socket.
   *
   * @param dest the destination
   * @param obj  the datagram payload
   * @param <T>  type of payload
   */
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
