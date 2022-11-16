# 启动一个自测调试用数据库
set -x
# 目录
currdir=$(cd $(dirname $0); pwd);

docker_name="mes-self-test-mysql-${MES_MYSQL_PORT:-35005}"

mkdir -p /home/aiit-mes/${docker_name}/data;

if [ 1 -eq $(docker ps -a | grep -v k8s | grep ${docker_name} | wc -l) ];
then
  docker rm -f ${docker_name}
fi

## 默认L1测试mysql端口35006
docker run -p ${MES_MYSQL_PORT:-35005}:3306 \
--name="${docker_name}" \
--restart=always \
-e MYSQL_ROOT_PASSWORD=${MES_MYSQL_PASSWORD:-Y2h5d9pDa9D} \
-v /home/aiit-mes/${docker_name}/data:/var/lib/mysql \
-v ${currdir}/init:/docker-entrypoint-initdb.d/ \
-v ${currdir}/post_init:/opt/sql \
-v ${currdir}/upgrade:/opt/sql/upgrade \
-v ${currdir}/rollback:/opt/sql/rollback \
-v mysqld.cnf:/etc/mysql/mysql.conf.d/ \
-d mysql:5.7


docker ps|grep ${docker_name}