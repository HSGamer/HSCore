package me.hsgamer.hscore.bukkit.variable;

import me.hsgamer.hscore.bukkit.utils.BukkitUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.variable.VariableBundle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The utility class to manage the global {@link VariableBundle} for Bukkit
 */
public final class BukkitVariableBundle {
  /**
   * The global {@link VariableBundle} for Bukkit
   */
  public static final VariableBundle BUNDLE = new VariableBundle();

  private BukkitVariableBundle() {
    // EMPTY
  }

  /**
   * Register the default variables to {@link BukkitVariableBundle#BUNDLE}
   */
  public static void registerDefaultVariables() {
    // Player Name
    BUNDLE.register("player", StringReplacer.of((original, uuid) -> Bukkit.getOfflinePlayer(uuid).getName()), true);

    // Online Player
    BUNDLE.register("online", StringReplacer.of(original -> String.valueOf(Bukkit.getOnlinePlayers().size())), true);

    // Max Players
    BUNDLE.register("max_players", StringReplacer.of(original -> String.valueOf(Bukkit.getMaxPlayers())), true);

    // Location
    BUNDLE.register("world", StringReplacer.of((original, uuid) -> {
      Optional<World> optional = Optional.ofNullable(Bukkit.getPlayer(uuid)).map(player -> player.getLocation().getWorld());
      if (original.equalsIgnoreCase("_env")) {
        return optional.map(World::getEnvironment).map(Enum::name).orElse("");
      } else if (original.isEmpty()) {
        return optional.map(World::getName).orElse("");
      } else {
        return null;
      }
    }));
    BUNDLE.register("x", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getX).map(String::valueOf).orElse("")), true);
    BUNDLE.register("y", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getY).map(String::valueOf).orElse("")), true);
    BUNDLE.register("z", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getZ).map(String::valueOf).orElse("")), true);

    // Bed Location
    BUNDLE.register("bed_", StringReplacer.of((original, uuid) -> {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null || player.getBedSpawnLocation() == null) {
        return "";
      } else if (original.equalsIgnoreCase("world")) {
        return player.getBedSpawnLocation().getWorld().getName();
      } else if (original.equalsIgnoreCase("x")) {
        return String.valueOf(player.getBedSpawnLocation().getX());
      } else if (original.equalsIgnoreCase("y")) {
        return String.valueOf(player.getBedSpawnLocation().getY());
      } else if (original.equalsIgnoreCase("z")) {
        return String.valueOf(player.getBedSpawnLocation().getZ());
      } else if (original.equalsIgnoreCase("world_env")) {
        return player.getBedSpawnLocation().getWorld().getEnvironment().name();
      } else {
        return null;
      }
    }));

    // Exp
    BUNDLE.register("exp", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getExp).map(String::valueOf).orElse("")), true);

    // Level
    BUNDLE.register("level", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLevel).map(String::valueOf).orElse("")), true);

    // Exp to level
    BUNDLE.register("exp_to_level", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getExpToLevel).map(String::valueOf).orElse("")), true);

    // Food Level
    BUNDLE.register("food_level", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getFoodLevel).map(String::valueOf).orElse("")), true);

    // IP
    BUNDLE.register("ip", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getAddress).map(address -> address.getAddress().getHostName()).orElse("")), true);

    // Biome
    BUNDLE.register("biome", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getBlock).map(block -> block.getBiome().name()).orElse("")), true);

    // Ping
    BUNDLE.register("ping", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(BukkitUtils::getPing).map(String::valueOf).orElse("")), true);

    // Random
    BUNDLE.register("random_", StringReplacer.of(original -> {
      original = original.trim();
      if (original.contains(":")) {
        String[] split = original.split(":", 2);
        String s1 = split[0].trim();
        String s2 = split[1].trim();
        if (Validate.isValidInteger(s1) && Validate.isValidInteger(s2)) {
          int i1 = Integer.parseInt(s1);
          int i2 = Integer.parseInt(s2);
          int max = Math.max(i1, i2);
          int min = Math.min(i1, i2);
          return String.valueOf(ThreadLocalRandom.current().nextInt(min, max + 1));
        }
      } else if (Validate.isValidInteger(original)) {
        return String.valueOf(ThreadLocalRandom.current().nextInt(Integer.parseInt(original)));
      }
      return null;
    }));

    // UUID
    BUNDLE.register("uuid", StringReplacer.of((original, uuid) -> uuid.toString()), true);
  }
}
