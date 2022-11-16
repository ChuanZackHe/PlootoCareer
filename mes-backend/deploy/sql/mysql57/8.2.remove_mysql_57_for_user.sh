set -x

docker_name="mes-self-test-mysql-${MES_MYSQL_PORT:-35005}"

if [ 1 -eq $(docker ps -a | grep -v k8s | grep ${docker_name} | wc -l) ];
then
  docker rm -f ${docker_name}
fi

rm -rf /home/aiit-mes/${docker_name}/data;
