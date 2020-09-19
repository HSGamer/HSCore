#!/bin/bash
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ./HSCore-bukkit || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
