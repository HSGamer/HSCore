package me.hsgamer.hscore.bukkit.config.object;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * a wrapper class for {@link Location}.
 */
public final class Position {

  /**
   * the world name.
   */
  @NotNull
  private final String world;

  /**
   * the x.
   */
  private final double x;

  /**
   * the y.
   */
  private final double y;

  /**
   * the z.
   */
  private final double z;

  /**
   * the yaw.
   */
  private final float yaw;

  /**
   * the pitch.
   */
  private final float pitch;

  /**
   * ctor.
   *
   * @param world the world.
   * @param x the x.
   * @param y the y.
   * @param z the z.
   * @param yaw the yaw.
   * @param pitch the pitch.
   */
  public Position(@NotNull final String world, final double x, final double y, final double z, final float yaw,
                  final float pitch) {
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  /**
   * converts the given map into a new instance of {@code this}.
   *
   * @param map map to deserialize.
   *
   * @return deserialized {@code this} instance.
   *
   * @throws NullPointerException if the given map has not world, x, y, z, yaw, pitch keys.
   * @throws ClassCastException if the given map values contain wrong type of value for
   *   {@link #Position(String, double, double, double, float, float)}
   */
  @NotNull
  public static Position deserialize(@NotNull final Map<String, Object> map) {
    return new Position(
      (String) Objects.requireNonNull(map.get("world"), "The map has not world key!"),
      ((Number) Objects.requireNonNull(map.get("x"), "The map has not x key!")).doubleValue(),
      ((Number) Objects.requireNonNull(map.get("y"), "The map has not y key!")).doubleValue(),
      ((Number) Objects.requireNonNull(map.get("z"), "The map has not z key!")).doubleValue(),
      ((Number) Objects.requireNonNull(map.get("yaw"), "The map has not yaw key!")).doubleValue(),
      ((Number) Objects.requireNonNull(map.get("pitch"), "The map has not pitch key!")).doubleValue());
  }

  /**
   * converts {@code this} class's values into a {@link Location}.
   *
   * @return a new {@link Location} instance.
   *
   * @throws NullPointerException if the {@link #world} world is not found in the server.
   */
  @NotNull
  public Location get() {
    return new Location(
      Objects.requireNonNull(Bukkit.getWorld(this.world),
        String.format("The world called %s not found!", this.world)),
      this.x, this.y, this.z, this.yaw, this.pitch);
  }

  /**
   * converts {@code this} class's values into a map.
   *
   * @return a serialized map.
   */
  @NotNull
  public Map<String, Object> serialize() {
    final Map<String, Object> map = new HashMap<>();
    map.put("world", this.world);
    map.put("x", this.x);
    map.put("y", this.y);
    map.put("z", this.z);
    map.put("yaw", this.yaw);
    map.put("pitch", this.pitch);
    return map;
  }
}
