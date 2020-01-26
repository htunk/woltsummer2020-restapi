FROM mozilla/sbt
COPY . .
RUN sbt clean assembly
RUN mv /target/scala-2.12/woltsummer2020-restapi-assembly-0.1.0-SNAPSHOT.jar /woltrestDeployed.jar
EXPOSE 8080
CMD java -jar woltrestDeployed.jar
