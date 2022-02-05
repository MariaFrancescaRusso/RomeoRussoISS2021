
FROM openjdk:12.0.2
EXPOSE 8081
## ON PC
ADD ./build/distributions/maitreGUI-1.0.tar  /
ADD ./build/distributions/maitreGUI-boot-1.0.tar  /


WORKDIR /maitreGUI-boot-1.0/bin
##COPY ./*.pl ./
##COPY ./*.json ./
RUN ls

CMD ["bash", "maitreGUI"]

### gradlew build
### docker build -f Dockerfile -t maitregui:1.1 .

### run the image to test
### docker run -p 8081:8081 -ti --rm maitregui:1.1 /bin/bash //to use the console
### docker run -d --name maitregui -p 8081:8081 maitregui:1.1

## Tag the image
## docker tag maitregui:1.0 bluffgnuff/maitregui:1.1

## Register the image on DockerHub
## docker push bluffgnuff/maitregui:1.1




