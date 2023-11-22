package me.hsgamer.hscore.database.client.hibernate;

import me.hsgamer.hscore.database.BaseClient;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.ServiceRegistry;

import java.util.function.Consumer;

/**
 * The Hibernate client
 */
public class HibernateClient extends BaseClient<MetadataSources> {
  private final MetadataSources metadataSources;
  private Metadata metadata;

  /**
   * Create new Hibernate client
   *
   * @param setting the setting
   */
  public HibernateClient(Setting setting) {
    super(setting);
    Driver driver = setting.getDriver();
    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
      .applySetting(AvailableSettings.JAKARTA_JDBC_DRIVER, driver.getDriverClass().getName())
      .applySetting(AvailableSettings.JAKARTA_JDBC_URL, driver.convertURL(setting))
      .applySetting(AvailableSettings.JAKARTA_JDBC_USER, setting.getUsername())
      .applySetting(AvailableSettings.JAKARTA_JDBC_PASSWORD, setting.getPassword())
      .applySettings(setting.getClientProperties())
      .build();
    this.metadataSources = new MetadataSources(serviceRegistry);
  }

  @Override
  public MetadataSources getOriginal() {
    return metadataSources;
  }

  /**
   * Configure the metadata sources
   *
   * @param metadataSourcesConsumer the consumer for the metadata sources
   *
   * @return the client for chaining
   */
  public HibernateClient configure(Consumer<MetadataSources> metadataSourcesConsumer) {
    metadataSourcesConsumer.accept(metadataSources);
    return this;
  }

  /**
   * Add an entity class to the client
   *
   * @param clazz the entity class
   *
   * @return the client for chaining
   */
  public HibernateClient addEntityClass(Class<?> clazz) {
    metadataSources.addAnnotatedClass(clazz);
    return this;
  }

  /**
   * Get the metadata of the client
   *
   * @return the metadata
   */
  public Metadata getMetadata() {
    if (metadata == null) {
      metadata = metadataSources.buildMetadata();
    }
    return metadata;
  }

  /**
   * Build the session factory from the client
   *
   * @return the session factory
   */
  public SessionFactory buildSessionFactory() {
    return getMetadata().buildSessionFactory();
  }
}
