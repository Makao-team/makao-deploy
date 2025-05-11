#!/bin/bash
set -e

if [ -f "docker-compose.yml" ]; then
  COMPOSE_FILE="docker-compose-prod.yml"
else
  COMPOSE_FILE="docker-compose-local.yml"
fi

echo "📄 사용 중인 docker-compose 파일: $COMPOSE_FILE"

echo "🔄 기존 컨테이너 중지 및 삭제"
docker compose -f "$COMPOSE_FILE" down || echo "컨테이너 없음"

echo "🧹 기존 이미지 삭제"
docker rmi -f makao-deploy-makao-deploy-api:latest || echo "이미지 없음"

echo "🧹 기존 volume 삭제"
docker volume rm -f makao-deploy_postgres-data || echo "볼륨 없음"

echo "🔁 새 이미지 빌드 및 컨테이너 실행"
docker compose -f "$COMPOSE_FILE" up --build -d

echo "🔍 컨테이너 상태 확인"
docker ps

echo "✅ 컨테이너 실행 완료"