#!/bin/bash

gzip -kdc /salesdb.sql.gz | mysql -DSalesDB -hlocalhost -uroot -ppassword
