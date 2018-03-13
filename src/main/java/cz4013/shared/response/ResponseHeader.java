package cz4013.shared.response;

import java.util.Objects;
import java.util.UUID;

/**
 * Header of a response: the corresponding request's ID and the response's status code.
 */
public class ResponseHeader {
  public UUID uuid;
  public Status status;

  public ResponseHeader() {}

  public ResponseHeader(UUID uuid, Status status) {
    this.uuid = uuid;
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ResponseHeader)) return false;
    ResponseHeader header = (ResponseHeader) o;
    return Objects.equals(uuid, header.uuid) &&
      status == header.status;
  }

  @Override
  public String toString() {
    return "ResponseHeader(" + uuid + ", " + status + ")";
  }
}
