# 停止容器，删除历史镜像
if [ 0 -ne $(docker ps -a | grep -v k8s | grep 'mes-backend' | wc -l) ];
then
  docker-compose down
fi

if [ 2 -eq $(docker images mes-backend|wc -l) ];
then
  docker rmi mes-backend;
fi