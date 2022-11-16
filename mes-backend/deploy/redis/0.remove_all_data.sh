# 停止容器，删除历史数据
if [ 1 -eq $(docker ps -a | grep -v k8s | grep 'mes-redis' | wc -l) ];
then
  docker-compose down
  docker rmi mes-redis;
fi

# 删除历史数据
rm -rf /home/aiit-mes/redis/data;