set -x

if [ 1 -eq $(docker ps -a | grep -v k8s | grep 'mes-redis' | wc -l) ];
then
  docker-compose down
  docker rmi mes-redis;
fi

mkdir -p /home/aiit-mes/redis/data
mkdir -p /home/aiit-mes/redis/log

docker-compose build
docker-compose up -d
docker ps|grep mes-redis