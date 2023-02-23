package me.hsgamer.hscore.bukkit.item.modifier;

import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The item flag modifier
 */
public class ItemFlagModifier extends ItemMetaModifier {
  private List<String> flagList = Collections.emptyList();

  @Override
  public String getName() {
    return "item-flag";
  }

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
  public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    for (ItemFlag flag : getParsed(uuid, stringReplacerMap.values())) {
      meta.addItemFlags(flag);
    }
    return meta;
  }

  @Override
  public void loadFromItemMeta(ItemMeta meta) {
    this.flagList = meta.getItemFlags().stream().map(ItemFlag::name).collect(Collectors.toList());
  }

  @Override
  public boolean canLoadFromItemMeta(ItemMeta meta) {
    return true;
  }

  @Override
  public boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
    Set<ItemFlag> list1 = getParsed(uuid, stringReplacerMap.values());
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