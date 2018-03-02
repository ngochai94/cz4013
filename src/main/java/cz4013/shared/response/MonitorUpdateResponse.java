package cz4013.shared.response;

public class MonitorUpdateResponse {
  public String info;

  public MonitorUpdateResponse() {}

  public MonitorUpdateResponse(String info) {
    this.info = info;
  }

  @Override
  public String toString() {
    return "MonitorUpdateResponse(" + info + ")";
  }
}
