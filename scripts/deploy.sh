#!/bin/bash
BUILD_JAR=$(ls /etc/server/PlantBackend/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /etc/server/PlantBackend/deploy.log

echo "> build 파일 복사" >> /etc/server/PlantBackend/deploy.log
DEPLOY_PATH=/etc/server/PlantBackend/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /etc/server/PlantBackend/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /etc/server/PlantBackend/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /etc/server/PlantBackend/deploy.log
nohup java -jar $DEPLOY_JAR >> /deploy.log 2>/etc/server/PlantBackend/deploy_err.log &
