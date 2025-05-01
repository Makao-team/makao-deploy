#!/bin/bash
set -e

echo "🧹 기존 이미지 삭제"
docker rmi -f makao-deploy-api:latest || echo "이미지 없음"

echo "🔁 새 이미지 빌드 및 컨테이너 실행"
docker compose up --build -d

echo "🔍 컨테이너 상태 확인"
docker ps

echo "✅ 컨테이너 실행 완료"