package cz4013.shared.request;

import java.util.Objects;

/**
 * The request to be sent from client to server,
 * contains a request header and a request body
 *
 * @param <ReqBody> type of request body
 */
public class Request<ReqBody> {
  public RequestHeader header;
  public ReqBody body;

  public Request() {
  }

  public Request(RequestHeader header, ReqBody body) {
    this.header = header;
    this.body = body;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Request)) return false;
    Request<?> request = (Request<?>) o;
    return Objects.equals(header, request.header) &&
      Objects.equals(body, request.body);
  }
}

