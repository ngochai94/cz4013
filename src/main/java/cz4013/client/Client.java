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


public class Client {
  private final Transport transport;
  private final int maxAttempts;
  private final SocketAddress serverAddr;

  public Client(Transport transport, SocketAddress serverAddr, int maxAttempts) {
    this.transport = transport;
    this.serverAddr = serverAddr;
    this.maxAttempts = maxAttempts;
  }

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
          continue;
        }

        throw e;
      }
    }

    throw new NoResponseException();
  }

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
