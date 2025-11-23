package me.hsgamer.hscore.bukkit.variable;

import me.hsgamer.hscore.bukkit.utils.BukkitUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.variable.VariableBundle;
import me.hsgamer.hscore.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * The {@link VariableBundle} for Bukkit variables
 */
public final class BukkitVariableBundle extends VariableBundle {
  /**
   * Create a new bundle for the variable manager
   *
   * @param variableManager the variable manager
   */
  public BukkitVariableBundle(VariableManager variableManager) {
    super(variableManager);

    // Player Name
    register("player", StringReplacer.of((original, uuid) -> Bukkit.getOfflinePlayer(uuid).getName()), true);

    // Online Player
    register("online", StringReplacer.of(original -> String.valueOf(Bukkit.getOnlinePlayers().size())), true);

    // Max Players
    register("max_players", StringReplacer.of(original -> String.valueOf(Bukkit.getMaxPlayers())), true);

    // Location
    register("world", StringReplacer.of((original, uuid) -> {
      Optional<World> optional = Optional.ofNullable(Bukkit.getPlayer(uuid)).map(player -> player.getLocation().getWorld());
      if (original.equalsIgnoreCase("_env")) {
        return optional.map(World::getEnvironment).map(Enum::name).orElse("");
      } else if (original.isEmpty()) {
        return optional.map(World::getName).orElse("");
      } else {
        return null;
      }
    }));
    register("x", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getX).map(String::valueOf).orElse("")), true);
    register("y", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getY).map(String::valueOf).orElse("")), true);
    register("z", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getZ).map(String::valueOf).orElse("")), true);

    // Bed Location
    register("bed_", StringReplacer.of((original, uuid) -> {
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
    register("exp", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getExp).map(String::valueOf).orElse("")), true);

    // Level
    register("level", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLevel).map(String::valueOf).orElse("")), true);

    // Exp to level
    register("exp_to_level", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getExpToLevel).map(String::valueOf).orElse("")), true);

    // Food Level
    register("food_level", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getFoodLevel).map(String::valueOf).orElse("")), true);

    // IP
    register("ip", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getAddress).map(address -> address.getAddress().getHostName()).orElse("")), true);

    // Biome
    register("biome", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getLocation).map(Location::getBlock).map(block -> block.getBiome().name()).orElse("")), true);

    // Ping
    register("ping", StringReplacer.of((original, uuid) -> Optional.ofNullable(Bukkit.getPlayer(uuid)).map(BukkitUtils::getPing).map(String::valueOf).orElse("")), true);
  }
}
