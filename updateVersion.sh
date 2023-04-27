#!/bin/bash
UPDATE_DIR="
hscore-bukkit
hscore-config
hscore-database
hscore-checker
hscore-bungeecord
hscore-minestom
hscore-downloader
hscore-logging
hscore-minecraft
hscore-expansion
hscore-logger
"

BASE_DIR="$(pwd)"
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
for dir in $UPDATE_DIR; do
  cd "$BASE_DIR/$dir"
  mvn -N versions:update-child-modules -DgenerateBackupPoms=false
done
cd "$BASE_DIR"
