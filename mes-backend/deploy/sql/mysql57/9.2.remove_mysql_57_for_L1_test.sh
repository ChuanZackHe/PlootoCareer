set -x
if [ 1 -eq $(docker ps -a | grep -v k8s | grep 'mes-l1-test-mysql' | wc -l) ];
then
  docker rm -f mes-l1-test-mysql
fi

rm -rf /home/aiit-mes/L1-test-mysql/data;
