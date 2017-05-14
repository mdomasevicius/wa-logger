FROM java:8-jre-alpine

ADD build/libs/crawler.jar app.jar

ENTRYPOINT exec java $JAVA_OPTS -server -jar app.jar
