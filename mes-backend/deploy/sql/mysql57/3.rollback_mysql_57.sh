set -x
docker_name="mes-mysql"

if [ 1 -ne $(docker ps | grep -w ${docker_name} | wc -l) ];
then
  echo "mysql container ${docker_name} not exist"
  exit 1;
fi

while [ 1 -ne $(docker ps | grep -w ${docker_name}|grep -w Up|wc -l) ]
do
  sleep 1
done

sleep 1
docker exec -t ${docker_name} bash /opt/sql/rollback/rollback.sh
