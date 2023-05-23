package me.hsgamer.hscore.bukkit.variable;

import me.hsgamer.hscore.bukkit.utils.BukkitUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.variable.VariableBundle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * The utility class to register Bukkit variables to the {@link VariableBundle}
 */
public final class BukkitVariableBundle {
  private BukkitVariableBundle() {
    // EMPTY
  }

  /**
   * Register the Bukkit variables to the {@link VariableBundle}
   *
   * @param bundle the bundle
   */
  public static void registerVariables(VariableBundle bundle) {
    // Player Name
    bundle.register("player", StringReplacer.of((original, uuid) -> Bukkit.getOfflinePlayer(uuid).getName()), true);

    // Online Player
    bundle.register("online", StringReplacer.of(original -> String.valueOf(Bukkit.getOnlinePlayers().size())), true);

    // Max Players
    bundle.register("max_players", StringReplacer.of(original -> String.valueOf(Bukkit.getMaxPlayers())), true);

    // Location
    bundle.register("world", StringReplacer.of((original, uuid) -> {
      Optional<World> optional = Optional.ofNullable(Bukkit.getPlayer(uuid)).map(player -> player.getLocation().getWorld());
      if (original.equalsIgnoreCase("_env")) {
        return optional.map(World::getEnvironment).map(Enum::name).orElse("");
      } else if (original.isEmpty()) {
        return optional.map(World::getName).orElse("");
      } else {
        return null;
      }
    }));
    bundle.register("x", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getX).map(String::valueOf).orElse("")), true);
    bundle.register("y", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getY).map(String::valueOf).orElse("")), true);
    bundle.register("z", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getZ).map(String::valueOf).orElse("")), true);

    // Bed Location
    bundle.register("bed_", StringReplacer.of((original, uuid) -> {
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
    bundle.register("exp", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getExp).map(String::valueOf).orElse("")), true);

    // Level
    bundle.register("level", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLevel).map(String::valueOf).orElse("")), true);

    // Exp to level
    bundle.register("exp_to_level", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getExpToLevel).map(String::valueOf).orElse("")), true);

    // Food Level
    bundle.register("food_level", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getFoodLevel).map(String::valueOf).orElse("")), true);

    // IP
    bundle.register("ip", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getAddress).map(address -> address.getAddress().getHostName()).orElse("")), true);

    // Biome
    bundle.register("biome", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getBlock).map(block -> block.getBiome().name()).orElse("")), true);

    // Ping
    bundle.register("ping", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(BukkitUtils::getPing).map(String::valueOf).orElse("")), true);
  }
}
