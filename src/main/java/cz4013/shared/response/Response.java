package cz4013.shared.response;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The response to be sent back from server to client,
 * contains a response header and a response body.
 *
 * @param <RespBody> type of response body
 */
public class Response<RespBody> {
  public ResponseHeader header;
  public Optional<RespBody> body;

  public Response() {
  }

  public Response(ResponseHeader header, Optional<RespBody> body) {
    this.header = header;
    this.body = body;
  }

  public static Response<Object> failed(UUID uuid, Status status) {
    assert status != Status.OK;
    return new Response<>(
      new ResponseHeader(uuid, status),
      Optional.empty()
    );
  }

  public static <T> Response<T> ok(UUID uuid, T body) {
    return new Response<>(new ResponseHeader(uuid, Status.OK), Optional.of(body));
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
