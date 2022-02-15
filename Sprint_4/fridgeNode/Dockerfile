
FROM openjdk:12.0.2
EXPOSE 8060
## ON PC
ADD ./build/distributions/fridgeNode-1.0.tar  /


WORKDIR /fridgeNode-1.0/bin
COPY ./*.pl ./
##COPY ./*.json ./
##RUN ls
RUN yum -y install nano && yum -y clean all  && rm -rf /var/cache
CMD ["bash", "fridgeNode"]

### gradle -b build_ctxfridge.gradle build
### docker build -f Dockerfile -t fridgenode:1.1 .

### run the image to test
### docker run -p 8060:8060 -ti --rm fridgenode:1.1 /bin/bash //to use the console
### docker run -d --name fridgenode -p 8060:8060/tcp -p 8060:8060/udp fridgenode:1.1

## Tag the image
## docker tag fridgenode:1.1 bluffgnuff/fridgenode:1.1

## Register the image on DockerHub
## docker push bluffgnuff/fridgenode:1.1




