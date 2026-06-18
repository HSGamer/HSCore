package me.hsgamer.hscore.bukkit.action.builder;

import me.hsgamer.hscore.action.builder.ActionBuilder;
import me.hsgamer.hscore.action.builder.ActionInput;
import me.hsgamer.hscore.bukkit.action.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.function.UnaryOperator;

/**
 * The utility class to register {@link me.hsgamer.hscore.action.common.Action} to the {@link ActionBuilder}
 */
public final class BukkitActionBuilder {
  private BukkitActionBuilder() {
    // EMPTY
  }

  /**
   * Register the actions
   *
   * @param actionBuilder the action builder
   * @param plugin        the plugin
   * @param colorizer     a function to colorize the text for message actions
   * @param <I>           the type of the input
   */
  public static <I extends ActionInput> void register(ActionBuilder<I> actionBuilder, Plugin plugin, UnaryOperator<String> colorizer) {
    actionBuilder.register(input -> new BroadcastAction(input.getValue(), colorizer), "broadcast");
    actionBuilder.register(input -> new ConsoleAction(plugin, input.getValue()), "console");
    actionBuilder.register(input -> new DelayAction(plugin, input.getValue()), "delay");
    actionBuilder.register(input -> new OpAction(plugin, input.getValue()), "op");
    actionBuilder.register(input -> new PermissionAction(plugin, input.getValue(), input.getOptionAsList()), "permission");
    actionBuilder.register(input -> new PlayerAction(plugin, input.getValue()), "player");
    actionBuilder.register(input -> new TellAction(input.getValue(), colorizer), "tell");
    actionBuilder.register(input -> new SoundAction(plugin, input.getValue()), "sound", "raw-sound");
  }

  /**
   * Register the actions
   *
   * @param actionBuilder the action builder
   * @param plugin        the plugin
   * @param <I>           the type of the input
   */
  public static <I extends ActionInput> void register(ActionBuilder<I> actionBuilder, Plugin plugin) {
    register(actionBuilder, plugin, s -> ChatColor.translateAlternateColorCodes('&', s));
  }
}
