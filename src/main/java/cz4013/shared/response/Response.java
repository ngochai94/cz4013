package cz4013.shared.response;

import java.util.UUID;

public class Response<BodyType> {
  public UUID id;
  public BodyType body;

  public Response() {
  }

  public Response(UUID id, BodyType body) {
    this.id = id;
    this.body = body;
  }

  @Override
  public String toString() {
    return "Response(" + id + ", " + body + ")";
  }
}
