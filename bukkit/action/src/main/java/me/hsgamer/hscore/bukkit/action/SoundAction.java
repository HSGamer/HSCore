package me.hsgamer.hscore.bukkit.action;

import io.github.projectunified.minelib.scheduler.entity.EntityScheduler;
import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * The action to play a sound at a player.
 * The sound value can be either a {@link Sound} or a raw sound.
 * The volume and pitch can be configured by using the format of <code>sound, volume, pitch</code>.
 */
public class SoundAction implements Action {
  private final Plugin plugin;
  private final String value;
  private final float defaultVolume;
  private final float defaultPitch;

  /**
   * Create a new action
   *
   * @param plugin        the plugin
   * @param value         the value
   * @param defaultVolume the default volume
   * @param defaultPitch  the default pitch
   */
  public SoundAction(Plugin plugin, String value, float defaultVolume, float defaultPitch) {
    this.plugin = plugin;
    this.value = value;
    this.defaultVolume = defaultVolume;
    this.defaultPitch = defaultPitch;
  }

  /**
   * Create a new action
   *
   * @param plugin the plugin
   * @param value  the value
   */
  public SoundAction(Plugin plugin, String value) {
    this(plugin, value, 1f, 1f);
  }

  @Override
  public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
    Player player = Bukkit.getPlayer(uuid);
    if (player == null) {
      process.next();
      return;
    }

    String sound;
    float volume = defaultVolume;
    float pitch = defaultPitch;
    String replaced = stringReplacer.replaceOrOriginal(value, uuid);
    String[] split;
    if (replaced.indexOf(',') != -1) {
      split = replaced.split(",");
    } else {
      split = replaced.split(" ");
    }

    sound = split[0].trim();
    if (split.length > 1) {
      try {
        volume = Float.parseFloat(split[1].trim());
      } catch (NumberFormatException ignored) {
        // IGNORED
      }
    }
    if (split.length > 2) {
      try {
        pitch = Float.parseFloat(split[2].trim());
      } catch (NumberFormatException ignored) {
        // IGNORED
      }
    }

    float finalVolume = volume;
    float finalPitch = pitch;
    EntityScheduler.get(plugin, player)
      .run(() -> {
        try {
          Sound soundEnum = Sound.valueOf(sound.replace(" ", "_").toUpperCase());
          player.playSound(player.getLocation(), soundEnum, finalVolume, finalPitch);
        } catch (Exception exception) {
          player.playSound(player.getLocation(), sound, finalVolume, finalPitch);
        } finally {
          process.next();
        }
      }, process::next);
  }
}
