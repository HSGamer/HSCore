#!/bin/bash
UPDATE_DIR="
bukkit
config
database
checker
bungeecord
minestom
downloader
minecraft
expansion
logger
license
"

BASE_DIR="$(pwd)"
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
for dir in $UPDATE_DIR; do
  cd "$BASE_DIR/$dir" || exit
  mvn -N versions:update-child-modules -DgenerateBackupPoms=false
done
cd "$BASE_DIR" || exit
