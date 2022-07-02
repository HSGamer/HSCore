package me.hsgamer.hscore.minestom.board;

import me.hsgamer.hscore.common.CollectionUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.scoreboard.Sidebar;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

/**
 * A simple scoreboard for Minestom.
 * Call {@link #hook(EventNode)} when the server is starting up to make sure the feature work properly.
 */
public class Board {
  private static final Map<Player, Sidebar> boards = new HashMap<>();
  private static final int MAX_LINES = 15;
  private static final String MAGIC_STRING = "boardLine_";
  private final Collection<Player> players = new ConcurrentLinkedDeque<>();
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

  private static void initBoard(Player player) {
    if (boards.containsKey(player)) {
      return;
    }
    Sidebar sidebar = new Sidebar(Component.text(""));
    sidebar.addViewer(player);
    boards.put(player, sidebar);
  }

  private static void removeBoard(Player player) {
    Optional.ofNullable(boards.remove(player)).ifPresent(sidebar -> sidebar.removeViewer(player));
  }

  /**
   * Add the player to the board
   *
   * @param player the player
   */
  public void addPlayer(Player player) {
    initBoard(player);
    players.add(player);
    update(player);
  }

  /**
   * Remove the player from the board
   *
   * @param player the player
   */
  public void removePlayer(Player player) {
    players.remove(player);
  }

  /**
   * Remove all the players from the board
   */
  public void removeAll() {
    players.clear();
  }

  /**
   * Update the board for the player
   *
   * @param player the player
   *
   * @return true if the board was updated, false if the player is not in the board
   */
  public boolean update(Player player) {
    Sidebar sidebar = boards.get(player);
    if (sidebar == null) {
      return false;
    }

    updateBoard(player, sidebar);
    return true;
  }

  /**
   * Update the board for all the players
   */
  public void updateAll() {
    for (Player player : players) {
      update(player);
    }
  }

  private void updateBoard(Player player, Sidebar sidebar) {
    sidebar.setTitle(title.apply(player));

    List<Component> lineList = CollectionUtils.reverse(lines.apply(player));
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
}
