#!/usr/bin/env bash
docker run \
--name mysql \
--rm \
--publish 3306:3306 \
--env MYSQL_ROOT_PASSWORD=secret \
--env MYSQL_DATABASE=SalesDB \
--volume $(pwd)/SalesDB.sql:/docker-entrypoint-initdb.d/SalesDB.sql:ro \
mysql:8
