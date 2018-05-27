# MyCujoo-Task

To run spring app on terminal :
./mvnw spring-boot:run

To create a Jar 
./mvnw clean package


To run created jar 
java -jar target/gs-actuator-service-0.1.0.jar

Docker :

Build docker image :
docker build -t mycujoo/lyricsapp:v1 .

Run docker image :
docker run -p 9100:9100  mycujoo/lyricsapp:v1

Kubernetes : 

minikube service lyricsapp -n=mycujoo --url 

curl http://192.168.99.100:30152/healthz

curl http://192.168.99.100:30152/verbs/artist/title

curl http://192.168.99.100:30152/adjectives/artist/title
