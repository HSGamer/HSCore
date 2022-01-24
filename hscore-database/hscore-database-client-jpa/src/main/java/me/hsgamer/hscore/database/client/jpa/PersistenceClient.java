package me.hsgamer.hscore.database.client.jpa;

import me.hsgamer.hscore.database.Client;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
   * @param driver  The driver of the persistence client
   */
  public PersistenceClient(String name, Setting setting, Driver driver) {
    this.setting = setting;
    Map<String, Object> properties = new HashMap<>();
    properties.put("javax.persistence.jdbc.url", driver.convertURL(setting));
    properties.put("javax.persistence.jdbc.user", setting.getUsername());
    properties.put("javax.persistence.jdbc.password", setting.getPassword());
    properties.put("javax.persistence.jdbc.driver", driver.getDriverClass());
    properties.putAll(setting.getProperties());
    this.entityManagerFactory = Persistence.createEntityManagerFactory(name, properties);
  }

  @Override
  public EntityManagerFactory getOriginal() {
    return entityManagerFactory;
  }

  @Override
  public Setting getSetting() {
    return setting;
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
