# HSCore ![Maven Central](https://img.shields.io/maven-central/v/me.hsgamer/hscore) ![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/me.hsgamer/hscore?color=green&label=snapshot&server=https%3A%2F%2Frepo.codemc.io) [![Build Status](https://ci.codemc.io/job/HSGamer/job/HSCore/badge/icon)](https://ci.codemc.io/job/HSGamer/job/HSCore/) [![Javadocs](https://img.shields.io/badge/javadocs-link-green)](https://hsgamer.github.io/HSCore/) [![Discord](https://img.shields.io/discord/660795353037144064)](https://discord.gg/9m4GdFD)

## Info

* This is a collection of common code used in my personal projects.
* I created this core because I am too lazy to change/fix/update the same code over and over again.
* This also includes some simple/useful/complex/dumb/... features used mainly in my projects.

## Modules

* **`hscore`**
  * **`hscore-addon`**
  * **`hscore-builder`**
  * **`hscore-common`**
  * **`hscore-collections`**
  * **`hscore-crontime`**
  * **`hscore-downloader`**
    * **`hscore-downloader-core`**
    * **`hscore-downloader-json`**
    * **`hscore-downloader-web-stream`**
  * **`hscore-expression`**
  * **`hscore-request`**
  * **`hscore-web`**
  * **`hscore-variable`**
  * **`hscore-checker`**
    * **`hscore-checker-common`**
    * **`hscore-checker-spigotmc`**
    * **`hscore-checker-github`**
    * **`hscore-checker-polymart`**
    * **`hscore-checker-modrinth`**
  * **`hscore-database`**
    * **`hscore-database-common`**
    * **`hscore-database-driver-mysql`**
    * **`hscore-database-driver-sqlite`**
    * **`hscore-database-driver-mariadb`**
    * **`hscore-database-driver-postgresql`**
    * **`hscore-database-driver-sqlserver`**
    * **`hscore-database-client-sql`**
    * **`hscore-database-client-hikari`**
    * **`hscore-database-client-java`**
    * **`hscore-database-client-hibernate`**
    * **`hscore-database-client-jpa`**
    * **`hscore-database-h2`**
  * **`hscore-config`**
    * **`hscore-config-common`**
    * **`hscore-config-path`**
    * **`hscore-config-simplixstorage`**
    * **`hscore-config-simpleconfiguration`**
    * **`hscore-config-configurate`**
    * **`hscore-config-annotation`**
    * **`hscore-config-annotated`**
    * **`hscore-config-proxy`**
  * **`hscore-task`**
  * **`hscore-ui`**
  * **`hscore-minecraft`**
    * **`hscore-minecraft-gui`**
    * **`hscore-minecraft-gui-button`**
    * **`hscore-minecraft-gui-mask`**
    * **`hscore-minecraft-gui-simple`**
    * **`hscore-minecraft-gui-advanced`**
    * **`hscore-minecraft-block`**
    * **`hscore-minecraft-block-impl`**
    * **`hscore-minecraft-block-utils`**
  * **`hscore-bukkit`**
    * **`hscore-bukkit-addon`**
    * **`hscore-bukkit-expansion`**
    * **`hscore-bukkit-command`**
    * **`hscore-bukkit-command-extra`**
    * **`hscore-bukkit-command-sub`**
    * **`hscore-bukkit-config`**
    * **`hscore-bukkit-config-extra`**
    * **`hscore-bukkit-utils`**
    * **`hscore-bukkit-gui`**
    * **`hscore-bukkit-gui-button`**
    * **`hscore-bukkit-gui-mask`**
    * **`hscore-bukkit-gui-simple`**
    * **`hscore-bukkit-gui-advanced`**
    * **`hscore-bukkit-item`**
    * **`hscore-bukkit-baseplugin`**
    * **`hscore-bukkit-channel`**
    * **`hscore-bukkit-key`**
    * **`hscore-bukkit-listener`**
    * **`hscore-bukkit-block`**
    * **`hscore-bukkit-scheduler`**
    * **`hscore-bukkit-folia`**
  * **`hscore-bungeecord`**
    * **`hscore-bungeecord-channel`**
    * **`hscore-bungeecord-config`**
  * **`hscore-minestom`**
    * **`hscore-minestom-board`**
    * **`hscore-minestom-gui`**
  * **`hscore-logging`**
    * **`hscore-logging-log4j`**
  * **`hscore-expansion`**
    * **`hscore-expansion-common`**
    * **`hscore-expansion-manifest`**
    * **`hscore-expansion-properties`**
    * **`hscore-expansion-extra`**

## Add as a dependency

### Maven

```xml

<dependencies>
  <dependency>
    <groupId>me.hsgamer</groupId>
    <artifactId>hscore-MODULE</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

### Gradle

```groovy
dependencies {
  implementation 'me.hsgamer:hscore-MODULE:VERSION'
}
```