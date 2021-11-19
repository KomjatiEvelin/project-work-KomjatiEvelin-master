#!/usr/bin/env bash
docker run \
--name mysql \
--rm \
--publish 3306:3306 \
--env MYSQL_ROOT_PASSWORD=password \
--env MYSQL_DATABASE=SalesDB \
--volume $(pwd)/salesdb.sql.gz:/salesdb.sql.gz \
--volume $(pwd)/use_sql_file.sh:/docker-entrypoint-initdb.d/use_sql_file.sh \
mysql:8
