package me.hsgamer.hscore.database.client.redis;

import me.hsgamer.hscore.database.Client;
import me.hsgamer.hscore.database.Setting;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * The client with Redis
 */
public class RedisClient implements Client<JedisPool> {

  private final Setting setting;
  private final JedisPool jedisPool;

  /**
   * Create new client
   *
   * @param setting the setting
   */
  public RedisClient(Setting setting) {
    this.setting = setting;
    this.jedisPool = new JedisPool(applyConfig(new JedisPoolConfig()), setting.getHost(), Integer.parseInt(setting.getPort()), 45000, setting.getPassword());
  }

  /**
   * Apply pool config
   *
   * @param originalConfig the original pool config
   *
   * @return the applied pool config
   */
  protected JedisPoolConfig applyConfig(JedisPoolConfig originalConfig) {
    return originalConfig;
  }

  @Override
  public JedisPool getOriginal() {
    return jedisPool;
  }

  /**
   * Get the resource
   *
   * @return the resource
   */
  public Jedis getResource() {
    return jedisPool.getResource();
  }

  @Override
  public Setting getSetting() {
    return setting;
  }
}
