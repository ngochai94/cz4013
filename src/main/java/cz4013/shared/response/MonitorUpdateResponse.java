package cz4013.shared.response;

/**
 * The response sent from server to all clients which are monitoring when there's a new update.
 * This response contains the updated information.
 */
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
