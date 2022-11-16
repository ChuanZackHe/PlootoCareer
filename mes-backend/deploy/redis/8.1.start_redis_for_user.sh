# 启动一个自测调试用数据库
set -x
# 目录
currdir=$(cd $(dirname $0); pwd);

docker_name="mes-self-test-redis-${MES_REDIS_PORT:-34001}"

mkdir -p /home/aiit-mes/${docker_name}/data;

if [ 1 -eq $(docker ps -a | grep -v k8s | grep ${docker_name} | wc -l) ];
then
  docker rm -f ${docker_name}
fi

## 默认L1测试mysql端口35006
docker run -p ${MES_REDIS_PORT:-35007}:6379 \
--name="${docker_name}" \
--restart=always \
-v /home/aiit-mes/${docker_name}/data:/date \
-v ${currdir}/redis.conf:/usr/local/etc/redis/redis.conf \
-v /home/aiit-mes/redis/log:/logs \
-d redis:5.0.5


docker ps|grep ${docker_name}