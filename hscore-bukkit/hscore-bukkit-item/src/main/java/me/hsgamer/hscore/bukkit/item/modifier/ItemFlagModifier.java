package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemMetaComparator;
import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The item flag modifier
 */
public class ItemFlagModifier implements ItemMetaModifier, ItemMetaComparator {
  private List<String> flagList = Collections.emptyList();

  private Set<ItemFlag> getParsed(UUID uuid, Collection<StringReplacer> stringReplacers) {
    Set<ItemFlag> flags = new HashSet<>();
    flagList.forEach(string -> {
      string = StringReplacer.replace(string, uuid, stringReplacers).trim();
      if (string.equalsIgnoreCase("all")) {
        Collections.addAll(flags, ItemFlag.values());
        return;
      }
      try {
        flags.add(ItemFlag.valueOf(string.toUpperCase().replace(" ", "_")));
      } catch (IllegalArgumentException e) {
        // IGNORED
      }
    });
    return flags;
  }

  @Override
  public @NotNull ItemMeta modifyMeta(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    for (ItemFlag flag : getParsed(uuid, stringReplacers)) {
      meta.addItemFlags(flag);
    }
    return meta;
  }

  @Override
  public boolean loadFromItemMeta(ItemMeta meta) {
    this.flagList = meta.getItemFlags().stream().map(ItemFlag::name).collect(Collectors.toList());
    return true;
  }

  @Override
  public boolean compare(@NotNull ItemMeta meta, @Nullable UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
    Set<ItemFlag> list1 = getParsed(uuid, stringReplacers);
    Set<ItemFlag> list2 = meta.getItemFlags();
    return list1.size() == list2.size() && list1.containsAll(list2);
  }

  @Override
  public Object toObject() {
    return flagList;
  }

  @Override
  public void loadFromObject(Object object) {
    this.flagList = CollectionUtils.createStringListFromObject(object, true);
  }

  /**
   * Add the flags to the modifier
   *
   * @param flags the flag to add
   *
   * @return {@code this} for builder chain
   */
  public ItemFlagModifier setFlag(ItemFlag... flags) {
    this.flagList = Arrays.stream(flags).map(ItemFlag::name).collect(Collectors.toList());
    return this;
  }
}
