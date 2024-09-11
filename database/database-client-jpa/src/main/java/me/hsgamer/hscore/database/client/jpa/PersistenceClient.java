package me.hsgamer.hscore.database.client.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import me.hsgamer.hscore.database.Client;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

import java.util.HashMap;
import java.util.Map;

/**
 * The JPA persistence client
 */
public class PersistenceClient implements Client<EntityManagerFactory> {
  private final Setting setting;
  private final EntityManagerFactory entityManagerFactory;

  /**
   * Create a new persistence client
   *
   * @param name    The name of the persistence client
   * @param setting The setting of the persistence client
   */
  public PersistenceClient(String name, Setting setting) {
    this.setting = setting;
    Driver driver = setting.getDriver();
    Map<String, Object> properties = new HashMap<>();
    properties.put("jakarta.persistence.jdbc.url", driver.convertURL(setting));
    properties.put("jakarta.persistence.jdbc.user", setting.getUsername());
    properties.put("jakarta.persistence.jdbc.password", setting.getPassword());
    properties.put("jakarta.persistence.jdbc.driver", driver.getDriverClassName());
    properties.putAll(setting.getClientProperties());
    this.entityManagerFactory = Persistence.createEntityManagerFactory(name, properties);
  }

  @Override
  public Setting getSetting() {
    return setting;
  }

  @Override
  public EntityManagerFactory getOriginal() {
    return entityManagerFactory;
  }

  /**
   * Get a new entity manager
   *
   * @return The entity manager
   */
  public EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }
}
