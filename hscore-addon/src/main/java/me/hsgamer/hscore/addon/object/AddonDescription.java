package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.expansion.common.ExpansionDescription;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class AddonDescription implements ExpansionDescription {
  private final String name;
  private final String version;
  private final String mainClass;
  private final Map<String, Object> data;

  public AddonDescription(@NotNull final String name, @NotNull final String version, @NotNull final String mainClass, @NotNull final Map<String, Object> data) {
    this.name = name;
    this.version = version;
    this.mainClass = mainClass;
    this.data = data;
  }

  public AddonDescription(@NotNull final ExpansionDescription expansionDescription) {
    this(expansionDescription.getName(), expansionDescription.getVersion(), expansionDescription.getMainClass(), expansionDescription.getData());
  }

  @Override
  public @NotNull String getName() {
    return name;
  }

  @Override
  public @NotNull String getVersion() {
    return version;
  }

  @Override
  public @NotNull String getMainClass() {
    return mainClass;
  }

  @Override
  public @NotNull Map<String, Object> getData() {
    return data;
  }
}
