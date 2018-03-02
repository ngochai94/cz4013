package cz4013.shared.request;

import java.util.UUID;

public class Request<BodyType> {
  public UUID id;
  public BodyType body;

  public Request() {
  }

  public Request(BodyType body) {
    id = UUID.randomUUID();
    this.body = body;
  }

  @Override
  public String toString() {
    return "Request(" + id + ", " + body + ")";
  }
}

