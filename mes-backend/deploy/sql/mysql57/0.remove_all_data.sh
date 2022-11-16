set -x
set -e
# 目录
currdir=$(cd $(dirname $0); pwd);

docker_name="mes-mysql"

# 停止容器，删除历史数据
if [ 1 -eq $(docker ps -a | grep -v k8s | grep ${docker_name} | wc -l) ];
then
  docker-compose down
  docker rmi ${docker_name};
fi

# 删除mysql历史数据
rm -rf /home/aiit-mes/mysql/data;
mkdir -p /home/aiit-mes/mysql/data;