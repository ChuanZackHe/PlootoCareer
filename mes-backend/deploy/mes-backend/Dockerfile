FROM centos8-jdk8u301-x86:1.0

# 未安装自定镜像时使用
# 方案1， 直接使用al-jdk
#FROM openjdk:8-jdk-alpine

# 方案2， 重新从centos:8 开始制造，依赖jdk 301，归档：https://codeup.aliyun.com/60ee96e92f222be2d94038f3/common/base-docker-images/tree/master/centos8-jdk8u301
#FROM centos:8
# 安装JDK
#ADD jdk1.8.0_301 /var/local/jdk1.8.0_301
# 设置环境变量
#ENV JAVA_HOME /var/local/jdk1.8.0_301
#ENV PATH /usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin:$JAVA_HOME/bin

RUN rm -f /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
ADD Asia/Shanghai /etc/localtime

ENV TZ="Asia/Shanghai"
EXPOSE 9093
EXPOSE 9094
ENTRYPOINT ["/bin/sh","start_service.sh"]
