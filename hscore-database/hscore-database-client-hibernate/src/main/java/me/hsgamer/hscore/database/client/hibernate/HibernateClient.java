package me.hsgamer.hscore.database.client.hibernate;

import me.hsgamer.hscore.database.BaseClient;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import java.util.function.Consumer;

/**
 * The Hibernate client
 */
public class HibernateClient extends BaseClient<Configuration> {
  private final Configuration configuration;

  /**
   * Create new Hibernate client
   *
   * @param setting the setting
   */
  public HibernateClient(Setting setting) {
    super(setting);
    Driver driver = setting.getDriver();

    // TODO: Use JPA-standard properties when the weird error is fixed
    //noinspection deprecation
    this.configuration = new Configuration()
      .setProperty(AvailableSettings.URL, driver.convertURL(setting))
      .setProperty(AvailableSettings.DRIVER, driver.getDriverClassName())
      .setProperty(AvailableSettings.USER, setting.getUsername())
      .setProperty(AvailableSettings.PASS, setting.getPassword());

    setting.getClientProperties().forEach((key, value) -> configuration.setProperty(key, value.toString()));
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
