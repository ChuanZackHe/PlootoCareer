set -x
# 目录
currdir=$(cd $(dirname $0); pwd);

if [ 1 -eq $(docker ps -a | grep -v k8s | grep 'mes-l1-test-mysql' | wc -l) ];
then
  docker rm -f mes-l1-test-mysql
fi

rm -rf /home/aiit-mes/L1-test-mysql/data;
mkdir -p /home/aiit-mes/L1-test-mysql/data;


## 默认L1测试mysql端口35006
docker run -p ${MES_MYSQL_PORT:-35006}:3306 \
--name="mes-l1-test-mysql" \
-e MYSQL_ROOT_PASSWORD=${MES_MYSQL_PASSWORD:-Y2h5d9pDa9D} \
-v /home/aiit-mes/L1-test-mysql/data:/var/lib/mysql \
-v ${currdir}/init:/docker-entrypoint-initdb.d/ \
-v ${currdir}/post_init:/opt/sql \
-v ${currdir}/upgrade:/opt/sql/upgrade \
-v ${currdir}/rollback:/opt/sql/rollback \
-v mysqld.cnf:/etc/mysql/mysql.conf.d/ \
-d mysql:5.7

sleep 2
if [ 0 -eq $(docker ps -a | grep -v k8s | grep 'mes-l1-test-mysql' |grep 'Up' | wc -l) ];
then
  echo 'test mysql init failed';
  exit 1;
fi

docker ps -a | grep -v k8s|grep mes-l1-test-mysql