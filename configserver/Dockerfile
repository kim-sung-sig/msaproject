# 1. 베이스 이미지 설정 (Java 런타임)
FROM openjdk:17-jdk-slim

# 2. JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} configApp

# 3. 컨테이너 포트 설정
EXPOSE 9000

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/configApp"]