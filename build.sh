./gradlew build
docker buildx build \
  --push \
  --platform linux/arm64/v8,linux/amd64 \
  --tag algaworks/fastpay:latest .