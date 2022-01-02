package me.hsgamer.hscore.web;

/**
 * The user agent
 */
public final class UserAgent {
  /**
   * User agent for Firefox
   */
  public static final UserAgent FIREFOX = new UserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");

  /**
   * User agent for Chrome
   */
  public static final UserAgent CHROME = new UserAgent("Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");

  /**
   * The agent string
   */
  private final String agent;

  /**
   * Create a new user agent
   *
   * @param agent the agent string
   */
  public UserAgent(String agent) {
    this.agent = agent;
  }

  /**
   * Get the agent string
   *
   * @return the agent string
   */
  public String getAgent() {
    return agent;
  }
}
