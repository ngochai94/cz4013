package cz4013.shared.request;

import java.util.Objects;
import java.util.UUID;

/**
 * Header of a request, contains a unique ID and the method name
 */
public class RequestHeader {
  public UUID uuid;
  public String method;

  public RequestHeader() {}

  public RequestHeader(UUID uuid, String method) {
    this.uuid = uuid;
    this.method = method;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RequestHeader)) return false;
    RequestHeader header = (RequestHeader) o;
    return Objects.equals(uuid, header.uuid) &&
      Objects.equals(method, header.method);
  }

  @Override
  public String toString() {
    return "RequestHeader(" + uuid + ", " + method + ")";
  }
}
