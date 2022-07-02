#!/bin/bash
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../hscore-downloader || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ./hscore-bukkit || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../hscore-config || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../hscore-database || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../hscore-checker || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../hscore-bungeecord || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../hscore-minestom || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
