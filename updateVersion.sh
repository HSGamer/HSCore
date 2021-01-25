#!/bin/bash
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ./HSCore-bukkit || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../HSCore-config || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../HSCore-database || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../HSCore-checker || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
