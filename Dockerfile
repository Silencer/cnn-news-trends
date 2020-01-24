FROM gradle:6.0.1-jdk11 AS build
RUN git clone https://github.com/Silencer/cnn-news-trends.git /home/gradle/src
RUN chown -R gradle:gradle /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

WORKDIR /home/gradle/src
EXPOSE 8080
ENTRYPOINT ["gradle", "run"]
