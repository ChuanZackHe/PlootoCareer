# 停止容器，删除历史镜像
if [ 0 -ne $(docker ps -a | grep -v k8s | grep 'mes-backend' | wc -l) ];
then
  docker-compose down
fi

mkdir -p /home/aiit-mes/mes-backend/log
mkdir -p /home/aiit-mes/file/static-file
mkdir -p /home/aiit-mes/file/dynamic-file

docker-compose build

docker-compose up -d

docker ps -a | grep -v k8s|grep mes-backend