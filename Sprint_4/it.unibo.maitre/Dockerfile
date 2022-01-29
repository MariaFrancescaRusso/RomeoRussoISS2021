FROM openjdk:12.0.2
## hypriot/rpi-java			##uses jessie
EXPOSE 8050
## ADD extracts the tar
ADD ./build/distributions/*.tar /
ADD ./*.pl ./
##ADD ./*.sh ./
##ADD ./*.py ./
## Does not work ---------------
## RUN apt-get update -y
## RUN apt-get install -y wiringpi
## RUN sudo apt-get install -y python
RUN ls /SPRINT_3-1.0/bin
## ----------------------------------
CMD ["bash", "/SPRINT_3-1.0/bin/SPRINT_3"]
