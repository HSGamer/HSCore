#!/bin/bash
mvn -N versions:update-child-modules
cd ./HSCore-bukkit || return
mvn -N versions:update-child-modules