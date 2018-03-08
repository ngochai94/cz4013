package cz4013.shared.response;

public enum Status {
  NOT_FOUND("service not found"),
  MALFORMED("malformed request"),
  INTERNAL_ERR("internal server error"),
  OK("ok");

  private final String reason;

  Status(final String reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return reason;
  }
}
