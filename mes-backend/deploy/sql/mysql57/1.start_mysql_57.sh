set -x
# 目录
docker_name="mes-mysql"
mkdir -p /home/aiit-mes/mysql/data;

# 停止原容器
if [ 1 -eq $(docker ps -a | grep -v k8s | grep ${docker_name} | wc -l) ];
then
  docker-compose down
  docker rmi ${docker_name};
fi

# 启动容器
docker-compose build
docker-compose up -d
docker ps|grep ${docker_name}