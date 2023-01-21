package me.hsgamer.hscore.minestom.gui.object;

import me.hsgamer.hscore.minecraft.gui.object.Item;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The item for Minestom
 *
 * @param itemStack the item stack
 */
public record MinestomItem(@NotNull ItemStack itemStack) implements Item {
}
