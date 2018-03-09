package cz4013.shared.request;

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
