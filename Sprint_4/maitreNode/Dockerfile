
FROM openjdk:12.0.2
EXPOSE 8070
## ON PC
ADD ./build/distributions/maitreNode-1.0.tar  /


WORKDIR /maitreNode-1.0/bin
COPY ./*.pl ./
##COPY ./*.json ./
##RUN ls

CMD ["bash", "maitreNode"]

### gradle -b build_ctxfridge.gradle build
### docker build -f Dockerfile -t maitrenode:1.1 .

### run the image to test
### docker run -p 8060:8060 -ti --rm maitrenode:1.1 /bin/bash //to use the console
### docker run -d --name maitrenode -p 8070:8070 maitrenode:1.1

## Tag the image
## docker tag maitrenode:1.1 bluffgnuff/maitrenode:1.1

## Register the image on DockerHub
## docker push bluffgnuff/maitrenode:1.1




