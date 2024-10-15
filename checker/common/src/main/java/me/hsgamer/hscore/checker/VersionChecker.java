package me.hsgamer.hscore.checker;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * The interface for the version checker
 */
public interface VersionChecker {
  /**
   * Get the version
   *
   * @return the version
   */
  @NotNull
  CompletableFuture<String> getVersion();
}
