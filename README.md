# HSCore ![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/me.HSGamer/HSCore?color=green&label=version&nexusVersion=3&server=https%3A%2F%2Frepo.codemc.io) [![Build Status](https://ci.codemc.io/job/HSGamer/job/HSCore/badge/icon)](https://ci.codemc.io/job/HSGamer/job/HSCore/) [![Javadocs](https://img.shields.io/badge/javadocs-link-green)](https://hsgamer.github.io/HSCore/)
## Info
* This is a collection of common code used in my personal projects.
* I created this core because I am too lazy to change/fix/update the same code over and over again.
* This also includes some simple/useful/complex/dumb/... features used mainly in my projects.
## Used on
* [TopIn](https://github.com/TopIn-MC/TopIn)
* [JoinWork](https://github.com/HSGamer/JoinWork/)
* [BetterGUI](https://github.com/BetterGUI-MC/BetterGUI)
## Modules
* **`HSCore-addon`**
* **`HSCore-common`**
* **`HSCore-collections`**
* **`HSCore-expression`**
* **`HSCore-json`**
* **`HSCore-request`**
* **`HSCore-web`**
* **`HSCore-config-common`**
* **`HSCore-config-yaml`**
* **`HSCore-config-json`**
* **`HSCore-config-path`**
* **`HSCore-bukkit-command`**
* **`HSCore-bukkit-subcommand`**
* **`HSCore-bukkit-config-extra`**
* **`HSCore-bukkit-updater`**
* **`HSCore-bukkit-utils`**
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
    </dependency>
  </dependencies>
```
