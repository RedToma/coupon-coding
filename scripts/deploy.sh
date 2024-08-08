#!/bin/bash

# 작업 디렉토리를 /home/ec2-user/app으로 변경
cd /home/ec2-user/app

# 환경변수 DOCKER_APP_NAME을 spring으로 설정
DOCKER_APP_NAME=spring

# 로그 파일 경로
LOG_FILE="/home/ec2-user/deploy.log"

# 로그 파일 열기
exec > >(tee -a "$LOG_FILE") 2>&1

# 실행중인 blue가 있는지 확인
EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

# 배포 시작한 날짜와 시간을 기록
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"

# green이 실행중이면 blue up
if [ -z "$EXIST_BLUE" ]; then
  echo "blue 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build
  sleep 30
  echo "green 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
  sudo docker image prune -af
  echo "green 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
else
  echo "green 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build
  sleep 30
  echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
  sudo docker image prune -af
  echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
fi

echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
echo "===================== 배포 완료 ====================="
