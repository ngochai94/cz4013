package cz4013.shared.response;

import java.util.Objects;
import java.util.UUID;

public class Header {
  public UUID uuid;
  public Status status;

  public Header(UUID uuid, Status status) {
    this.uuid = uuid;
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Header)) return false;
    Header header = (Header) o;
    return Objects.equals(uuid, header.uuid) &&
             status == header.status;
  }
}
