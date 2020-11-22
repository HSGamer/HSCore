package me.hsgamer.hscore.database.client.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import me.hsgamer.hscore.database.Client;
import me.hsgamer.hscore.database.Setting;

/**
 * The client with MongoDB
 */
public class MongoDBClient implements Client<MongoClient> {

  private final Setting setting;
  private final MongoCredential credential;
  private final MongoClientOptions options;
  private final ServerAddress address;

  /**
   * Create new client
   *
   * @param setting the setting
   */
  public MongoDBClient(Setting setting) {
    this.setting = setting;
    this.credential = MongoCredential.createCredential(setting.getUsername(), setting.getDatabaseName(), setting.getPassword().toCharArray());
    this.options = MongoClientOptions.builder().sslEnabled(setting.hasProperty("useSSL") && Boolean.parseBoolean(String.valueOf(setting.getProperty("useSSL")))).build();
    this.address = new ServerAddress(setting.getHost(), Integer.parseInt(setting.getPort()));
  }

  @Override
  public MongoClient getOriginal() {
    return new MongoClient(address, credential, options);
  }

  /**
   * Get the database
   *
   * @return the database
   */
  public MongoDatabase getDatabase() {
    return getOriginal().getDatabase(setting.getDatabaseName());
  }

  @Override
  public Setting getSetting() {
    return setting;
  }
}
