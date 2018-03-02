package cz4013.shared.request;

public class MonitorRequest {
  public double interval;

  public MonitorRequest() {
  }

  public MonitorRequest(double interval) {
    this.interval = interval;
  }

  @Override
  public String toString() {
    return "MonitorRequest(" + interval + ")";
  }
}
