package me.hsgamer.hscore.minestom.board;

import me.hsgamer.hscore.common.CollectionUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.scoreboard.Sidebar;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * A simple scoreboard for Minestom.
 * Call {@link #hook(EventNode)} when the server is starting up to make sure the feature work properly.
 */
public class Board {
  private static final Map<Player, Sidebar> boards = new ConcurrentHashMap<>();
  private static final int MAX_LINES = 15;
  private static final String MAGIC_STRING = "boardLine_";
  private final Function<Player, Component> title;
  private final Function<Player, List<Component>> lines;

  /**
   * Create a new board
   *
   * @param title the title supplier
   * @param lines the lines supplier
   */
  public Board(Function<Player, Component> title, Function<Player, List<Component>> lines) {
    this.title = title;
    this.lines = lines;
  }

  /**
   * Hook the board event to the event node.
   * Call the method when the server is starting up to hook the event and make sure the function work properly.
   *
   * @param node the event node
   */
  public static void hook(EventNode<Event> node) {
    node.addListener(PlayerDisconnectEvent.class, event -> Board.removeBoard(event.getPlayer()));
  }

  private static Sidebar getBoard(Player player) {
    return boards.computeIfAbsent(player, p -> {
      Sidebar sidebar = new Sidebar(Component.text(""));
      sidebar.addViewer(p);
      return sidebar;
    });
  }

  private static void removeBoard(Player player) {
    Optional.ofNullable(boards.remove(player)).ifPresent(sidebar -> sidebar.removeViewer(player));
  }

  private static void updateBoard(Player player, Component title, List<Component> lines) {
    Sidebar sidebar = getBoard(player);

    sidebar.setTitle(title);

    List<Component> lineList = CollectionUtils.reverse(lines);
    for (int i = 0; i < MAX_LINES; i++) {
      String lineId = MAGIC_STRING + i;
      Sidebar.ScoreboardLine line = sidebar.getLine(lineId);
      Component component = i < lineList.size() ? lineList.get(i) : null;
      if (line == null && component == null) {
        break;
      } else if (line == null) {
        sidebar.createLine(new Sidebar.ScoreboardLine(lineId, component, i));
      } else if (component == null) {
        sidebar.removeLine(lineId);
      } else {
        sidebar.updateLineContent(lineId, component);
      }
    }
  }

  /**
   * Update the board for the player
   *
   * @param player the player
   */
  public void update(Player player) {
    updateBoard(player, title.apply(player), lines.apply(player));
  }
}
