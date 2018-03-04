package cz4013.shared.response;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Response<Body> {
  public Header header;
  public Optional<Body> body;

  public Response(Header header, Optional<Body> body) {
    this.header = header;
    this.body = body;
  }

  public static Response<Object> failed(UUID uuid, Status status) {
    assert status != Status.OK;
    return new Response<>(
      new Header(uuid, status),
      Optional.empty()
    );
  }

  public static <T> Response<T> ok(UUID uuid, T body) {
    return new Response<>(new Header(uuid, Status.OK), Optional.of(body));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Response)) return false;
    Response<?> response = (Response<?>) o;
    return Objects.equals(header, response.header) &&
             Objects.equals(body, response.body);
  }
}
