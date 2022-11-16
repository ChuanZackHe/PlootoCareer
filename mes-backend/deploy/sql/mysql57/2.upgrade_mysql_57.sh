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
  echo "waiting for ${docker_name} to be ready"
done

# 备份原有数据
cd /home/aiit-mes/mysql
stamp=$(date +%Y%m%d%H%M%S);
tar -czvf ${docker_name}-data.${stamp}.tar.gz data

# 执行升级
docker exec -t ${docker_name} bash /opt/sql/upgrade/upgrade.sh
