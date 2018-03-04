package cz4013.shared.request;

import java.util.Objects;
import java.util.UUID;

public class Header {
  public UUID uuid;
  public String method;

  public Header() {}

  public Header(UUID uuid, String method) {
    this.uuid = uuid;
    this.method = method;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Header)) return false;
    Header header = (Header) o;
    return Objects.equals(uuid, header.uuid) &&
             Objects.equals(method, header.method);
  }
}
