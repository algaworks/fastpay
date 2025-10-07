FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/fastpay.jar /app/fastpay.jar

CMD exec java $JAVA_OPTS -jar /app/fastpay.jar