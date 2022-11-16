# 启动一个自测调试用数据库
set -x
# 目录
currdir=$(cd $(dirname $0); pwd);

docker_name="mes-self-test-mysql-${MES_MYSQL_PORT:-35005}"

docker exec -t ${docker_name} bash /opt/sql/rollback/rollback.sh
