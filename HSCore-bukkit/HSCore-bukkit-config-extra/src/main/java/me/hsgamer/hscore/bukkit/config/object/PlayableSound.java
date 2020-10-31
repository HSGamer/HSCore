package me.hsgamer.hscore.bukkit.config.object;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * a class that plays sounds to the given players.
 */
public final class PlayableSound {

  /**
   * the sound to play.
   */
  @NotNull
  private final Sound sound;

  /**
   * the volume of the sound.
   */
  private final Double volume;

  /**
   * the pitch of the sound.
   */
  private final Double pitch;

  /**
   * ctor.
   *
   * @param sound  the sound to play.
   * @param volume the volume to play.
   * @param pitch  the pitch to play.
   */
  public PlayableSound(@NotNull final Sound sound, @NotNull final Double volume, @NotNull final Double pitch) {
    this.sound = sound;
    this.volume = volume;
    this.pitch = pitch;
  }

  /**
   * serializes the values of the sound.
   *
   * @return a serialized map.
   */
  @Nullable
  public static PlayableSound deserialize(@NotNull final Map<String, Object> serialized) {
    final Object sound = serialized.get("sound");
    final Object volume = serialized.getOrDefault("volume", 1.0d);
    final Object pitch = serialized.getOrDefault("pitch", 1.0d);
    if (sound instanceof String && volume instanceof Number && pitch instanceof Number) {
      try {
        // Sound#valueOf method can replace with XSeries's XSound.
        return new PlayableSound(Sound.valueOf((String) sound), ((Number) volume).doubleValue(),
          ((Number) pitch).doubleValue());
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
    return null;
  }

  /**
   * plays the sound for the given player.
   *
   * @param players the players to play.
   */
  public void play(@NotNull final Player... players) {
    this.play(Arrays.asList(players));
  }

  /**
   * plays the sound for the given player.
   *
   * @param players the players to play.
   */
  public void play(@NotNull final Iterable<Player> players) {
    players.forEach(player ->
      this.play(player.getLocation(), player));
  }

  /**
   * plays the sound for the given players on the given location.
   *
   * @param players  the players to play.
   * @param location the location to play.
   */
  public void play(@NotNull final Location location, @NotNull final Player... players) {
    this.play(location, Arrays.asList(players));
  }

  /**
   * plays the sound for the given players on the given location.
   *
   * @param players  the players to play.
   * @param location the location to play.
   */
  public void play(@NotNull final Location location, @NotNull final Iterable<Player> players) {
    players.forEach(player ->
      player.playSound(location, this.sound, this.volume.floatValue(), this.pitch.floatValue()));
  }

  /**
   * serializes the values of the sound.
   *
   * @return a serialized map.
   */
  public Map<String, Object> serialize() {
    final Map<String, Object> map = new HashMap<>();
    map.put("sound", this.sound.name());
    map.put("volume", this.volume);
    map.put("pitch", this.pitch);
    return map;
  }
}
