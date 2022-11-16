##!/usr/bin/env bash
#
#dockerImage=registry.cn-wulanchabu.aliyuncs.com/aiit-mes/service-platform:$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
#./mvnw clean package -Dmaven.test.skip=true -Dmaven.build.timestamp="$(date "+%Y-%m-%d %H:%M:%S")" -s settings.xml
#if [ $? -ne 0 ];then
#    echo "构建失败!"
#else
#  cd ./jetlinks-standalone || exit
#
#  docker build -t "$dockerImage" . && docker push "$dockerImage"
#fi
#本地编译完 运行这个文件 推镜像 远程直接执行docker-compose.yml文件
docker login --username=shqd@bd-aiit registry.cn-wulanchabu.aliyuncs.com -p aiit1234
#密码 aiit1234
cd jetlinks-standalone
docker build -t registry.cn-wulanchabu.aliyuncs.com/aiit-lab/service-platform:1.0 .
docker push registry.cn-wulanchabu.aliyuncs.com/aiit-lab/service-platform:1.0