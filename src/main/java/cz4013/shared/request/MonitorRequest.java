package cz4013.shared.request;

/**
 * The request to monitor updates from other clients.
 */
public class MonitorRequest {
  public int interval;

  public MonitorRequest() {
  }

  public MonitorRequest(int interval) {
    this.interval = interval;
  }

  @Override
  public String toString() {
    return "MonitorRequest(" + interval + ")";
  }
}
