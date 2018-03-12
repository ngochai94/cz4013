package cz4013.shared.response;

/**
 * The response to a request to monitor
 */
public class MonitorStatusResponse {
  public boolean success;

  public MonitorStatusResponse() {}

  public MonitorStatusResponse(boolean success) {
    this.success = success;
  }

  @Override
  public String toString() {
    return "MonitorStatusResponse(" + success + ")";
  }
}
