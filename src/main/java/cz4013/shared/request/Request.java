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
}

