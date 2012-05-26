#!/bin/bash

if test ! -d database
then
        mkdir database
fi
echo Starting database engine...
cd database/
java -classpath ../lib/hsqldb.jar org.hsqldb.Server 
