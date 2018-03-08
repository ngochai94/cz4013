package cz4013.shared.request;

import java.util.Objects;

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

