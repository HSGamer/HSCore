#!/bin/bash

MODULE_DIRS=()

# Function to recursively find valid pom.xml files
find_modules() {
  local dir="$1"
  POM_FILE="$dir/pom.xml"
  if [[ -f "$POM_FILE" ]] && grep -q '<packaging>pom</packaging>' "$POM_FILE"; then
    MODULE_DIRS+=("$dir")
    for subdir in "$dir"/*/; do
      [[ -d "$subdir" ]] || continue
      find_modules "$subdir"  # Recursively check for sub-modules
    done
  fi
}

# Start searching from current directory
find_modules "$(pwd)"

# Run Maven command for each valid module directory
for dir in "${MODULE_DIRS[@]}"; do
  echo "Updating versions for module in directory: $dir"
  cd "$dir" || exit
  mvn -N versions:update-child-modules -DgenerateBackupPoms=false
done