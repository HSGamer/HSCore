package me.hsgamer.hscore.database.client.hibernate;

import me.hsgamer.hscore.database.Client;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import java.util.function.Consumer;

/**
 * The Hibernate client
 */
public class HibernateClient implements Client<Configuration> {
  private final Setting setting;
  private final Configuration configuration;

  /**
   * Create new Hibernate client
   *
   * @param setting the setting
   */
  public HibernateClient(Setting setting) {
    this.setting = setting;
    Driver driver = setting.getDriver();

    this.configuration = new Configuration()
      .setProperty(AvailableSettings.JAKARTA_JDBC_URL, driver.convertURL(setting))
      .setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, driver.getDriverClassName())
      .setProperty(AvailableSettings.JAKARTA_JDBC_USER, setting.getUsername())
      .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, setting.getPassword());

    setting.getClientProperties().forEach((key, value) -> configuration.setProperty(key, value.toString()));
  }

  @Override
  public Setting getSetting() {
    return setting;
  }

  @Override
  public Configuration getOriginal() {
    return configuration;
  }

  /**
   * Configure the configuration
   *
   * @param configurationConsumer the consumer
   *
   * @return the client for chaining
   */
  public HibernateClient configure(Consumer<Configuration> configurationConsumer) {
    configurationConsumer.accept(configuration);
    return this;
  }

  /**
   * Add entity class to the client
   *
   * @param clazz the entity class
   *
   * @return the client for chaining
   *
   * @see Configuration#addAnnotatedClass(Class)
   */
  public HibernateClient addEntityClass(Class<?>... clazz) {
    for (Class<?> c : clazz) {
      configuration.addAnnotatedClass(c);
    }
    return this;
  }

  /**
   * Build the session factory from the client
   *
   * @return the session factory
   */
  public SessionFactory buildSessionFactory() {
    return configuration.buildSessionFactory();
  }
}
