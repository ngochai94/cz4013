package cz4013.client;

import cz4013.shared.response.Status;

public class FailedRequestException extends RuntimeException {
  public final Status status;

  public FailedRequestException(Status status) {
    this.status = status;
  }
}
