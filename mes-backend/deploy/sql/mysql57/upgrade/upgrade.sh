#!/bin/bash
set -e

currdir=$(cd $(dirname $0); pwd);

mysql -uroot -p$MYSQL_ROOT_PASSWORD <<EOF

source ${currdir}/upgrade.sql