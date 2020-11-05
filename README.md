# HSCore ![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/me.HSGamer/HSCore?color=green&label=version&nexusVersion=3&server=https%3A%2F%2Frepo.codemc.io) [![Build Status](https://ci.codemc.io/job/HSGamer/job/HSCore/badge/icon)](https://ci.codemc.io/job/HSGamer/job/HSCore/) [![Javadocs](https://img.shields.io/badge/javadocs-link-green)](https://hsgamer.github.io/HSCore/) [![Discord](https://img.shields.io/discord/660795353037144064)](https://discord.gg/9m4GdFD)
## Info
* This is a collection of common code used in my personal projects.
* I created this core because I am too lazy to change/fix/update the same code over and over again.
* This also includes some simple/useful/complex/dumb/... features used mainly in my projects.
## Used on
* [TopIn](https://github.com/TopIn-MC/TopIn)
* [JoinWork](https://github.com/HSGamer/JoinWork/)
* [BetterGUI](https://github.com/BetterGUI-MC/BetterGUI)
## Modules
* **`HSCore-all`**
  * **`HSCore-addon`**
  * **`HSCore-common`**
  * **`HSCore-collections`**
  * **`HSCore-downloader`**
  * **`HSCore-expression`**
  * **`HSCore-json`**
  * **`HSCore-request`**
  * **`HSCore-web`**
  * **`HSCore-checker-all`**
    * **`HSCore-checker-spigotmc`**
  * **`HSCore-database-all`**
    * **`HSCore-database-common`**
    * **`HSCore-database-driver-mysql`**
    * **`HSCore-database-driver-sqlite`**
    * **`HSCore-database-driver-mariadb`**
    * **`HSCore-database-driver-postgresql`**
    * **`HSCore-database-hikari`**
    * **`HSCore-database-mongodb`**
    * **`HSCore-database-redis`**
  * **`HSCore-config-all`**
    * **`HSCore-config-common`**
    * **`HSCore-config-yaml`**
    * **`HSCore-config-json`**
    * **`HSCore-config-toml`**
    * **`HSCore-config-path`**
  * **`HSCore-ui-all`**
    * **`HSCore-ui-common`**
  * **`HSCore-bukkit-all`**
    * **`HSCore-bukkit-addon`**
    * **`HSCore-bukkit-command`**
    * **`HSCore-bukkit-subcommand`**
    * **`HSCore-bukkit-config`**
    * **`HSCore-bukkit-config-extra`**
    * **`HSCore-bukkit-utils`**
    * **`HSCore-bukkit-gui`**
## Add as a dependency (Maven)
```xml
  <repositories>
    <repository>
      <id>codemc-repo</id>
      <url>https://repo.codemc.io/repository/maven-public/</url>
    </repository>
  </repositories>

  <dependencies>
    <dependency>
      <groupId>me.HSGamer</groupId>
      <artifactId>HSCore-MODULE</artifactId>
      <version>VERSION</version>
      
      <!-- Add this line if you want to get the shaded artifact -->
      <classifier>jar-with-dependencies</classifier>
    </dependency>
  </dependencies>
```
