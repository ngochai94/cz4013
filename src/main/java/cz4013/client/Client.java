package cz4013.client;

import cz4013.shared.request.Request;
import cz4013.shared.request.RequestHeader;
import cz4013.shared.response.Response;
import cz4013.shared.response.Status;
import cz4013.shared.rpc.RawMessage;
import cz4013.shared.rpc.Transport;

import java.io.InterruptedIOException;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

import static cz4013.shared.serialization.Deserializer.deserialize;

/**
 * This class acts as the transport layer in client side.
 */
public class Client {
  private final Transport transport;
  private final int maxAttempts;
  private final SocketAddress serverAddr;

  public Client(Transport transport, SocketAddress serverAddr, int maxAttempts) {
    this.transport = transport;
    this.serverAddr = serverAddr;
    this.maxAttempts = maxAttempts;
  }

  /**
   * Sends a request to server side via UDP.
   * This method handles the necessary retries in case the UDP packets are lost.
   *
   * @param method     the method name to be invoked in server side
   * @param reqBody    the body of the request
   * @param respObj    an empty response object, to be used in deserialization
   * @param <ReqBody>  type of request body
   * @param <RespBody> type of response body
   * @return the body of the response
   */
  public <ReqBody, RespBody> RespBody request(String method, ReqBody reqBody, Response<RespBody> respObj) {
    UUID id = UUID.randomUUID();

    for (int triesLeft = maxAttempts; triesLeft > 0; --triesLeft) {
      try {
        transport.send(serverAddr, new Request<>(new RequestHeader(id, method), reqBody));
        try (RawMessage rawResp = transport.recv()) {
          Response<RespBody> resp = deserialize(respObj, rawResp.payload.get());
          if (!resp.header.uuid.equals(id)) {
            continue;
          }

          if (resp.header.status != Status.OK) {
            throw new FailedRequestException(resp.header.status);
          }

          return resp.body.get();
        }
      } catch (RuntimeException e) {
        if (e.getCause() instanceof SocketTimeoutException) {
          System.out.println("Socket timeout, retrying...");
          continue;
        }

        throw e;
      }
    }

    throw new NoResponseException();
  }

  /**
   * Waits for updates from server during the given interval.
   *
   * @param respObj    an empty response object, to be used in deserialization
   * @param interval   time to wait for update, in second
   * @param callback   the callback denotes how to handle update from server
   * @param <RespBody> type of the response body
   */
  public <RespBody> void poll(Response<RespBody> respObj, Duration interval, Consumer<RespBody> callback) {
    Instant end = Instant.now().plus(interval);

    Thread pollingThread = new Thread(() -> {
      for (; ; ) {
        if (Instant.now().isAfter(end)) {
          return;
        }
        try (RawMessage msg = transport.recv()) {
          deserialize(respObj, msg.payload.get()).body.ifPresent(callback);
        } catch (RuntimeException e) {
          if (e.getCause() instanceof SocketTimeoutException) {
            continue;
          }

          if (e.getCause() instanceof InterruptedIOException) {
            return;
          }
        }
      }
    });
    pollingThread.run();

    try {
      pollingThread.join(interval.toMillis());
    } catch (InterruptedException e) {
    }

    pollingThread.interrupt();
  }
}
