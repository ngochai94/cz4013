package cz4013.client;

import cz4013.shared.response.Status;

/**
 * This exception is thrown when a response is received from
 * the server with status code other than {@code Status.OK}.
 */
public class FailedRequestException extends RuntimeException {
  public final Status status;

  public FailedRequestException(Status status) {
    this.status = status;
  }
}
