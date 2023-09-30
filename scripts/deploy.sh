##!/bin/bash
#BUILD_JAR=$(ls /etc/server/PlantBackend/build/libs/*.jar)
#JAR_NAME=$(basename $BUILD_JAR)
#echo "> build 파일명: $JAR_NAME" >> /etc/server/PlantBackend/deploy.log
#
#echo "> build 파일 복사" >> /etc/server/PlantBackend/deploy.log
#DEPLOY_PATH=/etc/server/PlantBackend/
#cp $BUILD_JAR $DEPLOY_PATH
#
#echo "> 현재 실행중인 애플리케이션 pid 확인" >> /etc/server/PlantBackend/deploy.log
#CURRENT_PID=$(pgrep -f $JAR_NAME)
#
#if [ -z $CURRENT_PID ]
#then
#  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /etc/server/PlantBackend/deploy.log
#else
#  echo "> kill -15 $CURRENT_PID"
#  kill -15 $CURRENT_PID
#  sleep 5
#fi
#
#DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
#echo "> DEPLOY_JAR 배포"    >> /etc/server/PlantBackend/deploy.log
#nohup java -jar $DEPLOY_JAR >> /deploy.log 2>/etc/server/PlantBackend/deploy_err.log &
#!/bin/bash
BASE_PATH=/etc/server/PlantBackend
BUILD_PATH=$(ls $BASE_PATH/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)
echo "> build 파일명: $JAR_NAME"

echo "> build 파일 복사"
DEPLOY_PATH=$BASE_PATH/jar/
cp $BUILD_PATH $DEPLOY_PATH

echo "> 현재 구동중인 Set 확인"
CURRENT_PROFILE=$(curl -s http://localhost/api/profile)
echo "> $CURRENT_PROFILE"

# 쉬고 있는 set 찾기: set1이 사용중이면 set2가 쉬고 있고, 반대면 set1이 쉬고 있음
if [ $CURRENT_PROFILE == set1 ]
then
  IDLE_PROFILE=set2
  IDLE_PORT=8082
elif [ $CURRENT_PROFILE == set2 ]
then
  IDLE_PROFILE=set1
  IDLE_PORT=8081
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE"
  echo "> set1을 할당합니다. IDLE_PROFILE: set1"
  IDLE_PROFILE=set1
  IDLE_PORT=8081
fi

echo "> application.jar 교체"
IDLE_APPLICATION=$IDLE_PROFILE-PlantBackend.jar
IDLE_APPLICATION_PATH=$DEPLOY_PATH$IDLE_APPLICATION

ln -Tfs $DEPLOY_PATH$JAR_NAME $IDLE_APPLICATION_PATH

echo "> $IDLE_PROFILE 에서 구동중인 애플리케이션 pid 확인"
IDLE_PID=$(pgrep -f $IDLE_APPLICATION)

if [ -z $IDLE_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 $IDLE_PID
  sleep 5
fi

echo "> $IDLE_PROFILE 배포"
nohup java -jar -Dspring.profiles.active=$IDLE_PROFILE $IDLE_APPLICATION_PATH &

echo "> $IDLE_PROFILE 10초 후 Health check 시작"
echo "> curl -s http://localhost:$IDLE_PORT/actuator/health "
sleep 10

for retry_count in {1..10}
do
  response=$(curl -s http://localhost:$IDLE_PORT/actuator/health)
  up_count=$(echo $response | grep 'UP' | wc -l)

  if [ $up_count -ge 1 ]
  then # $up_count >= 1 ("UP" 문자열이 있는지 검증)
      echo "> Health check 성공"
      break
  else
      echo "> Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다."
      echo "> Health check: ${response}"
  fi

  if [ $retry_count -eq 10 ]
  then
    echo "> Health check 실패. "
    echo "> Nginx에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done

echo "> 스위칭"
sleep 10
~/etc/server/PlantBackend/scripts/switch.sh