
# Appunti internos
Ho aggiunto al file build_ctxrbr.gradle (che è la dipendenza che contiene il planner ) le dipendenze:
compile name: 'IssActorKotlinRobotSupport-2.0'

Per far comunicare dei container locali utilizzare l'ip asseggnatogli da docker:
    si ottiene con:
        docker network inspect bridge (es ip maitrenode "172.17.0.3", bridge è quello di default)


## IP hotspot Ste
    raspberry: 192.168.43.228
    ste: 192.168.43.157
    maria:  192.168.43.211
## IP wifi casa maria
     ste: 192.168.1.117
     maria:  192.168.1.14
     raspberry: 192.168.1.40

# Docker tips
- Aprire una shell su un container
    docker exec -it nome container bash
    docker run -p 8081:8081 -ti --rm maitregui:1.1 /bin/bash //to use the console
- Output del container con:
    docker logs maitrenode
-Installare nano(o altro software) con:
    apt-get update && apt-get install nano

# Avviare GUI basicrobot su Raspberry
primo avvio:
    docker-compose -f ./issLab2021/webBasicrobotqak/webbasicrobotqakonpc.yaml
    docker start webbasicrobotqak-webgui-1
    (da browser localhost:8085)

# Guida deploy

## Avviare basicrobot

- fare login
    username: pi
    password: raspberry
        Per velocizzare il login tramite ssh ho impostato il loggin tramite chiave privata:
        - Generare una chiave pubblica associata ad un email (noi lo abbiamo gia fatto)
        - dare il comando: ssh-copy-id pi@192.168.1.93
        - dopo, per connettersi, basterà dare ssh pi@192.168.1.93 senza che venga chiesta la password

- avviare basicrobot su rapberry:
    - passare il file basicrobotMbot.yaml su raspberry (io ho passato tutto il progetto per sicurezza)
    scp -r ./issLab2021/it.unibo.qak21.basicrobot pi@192.168.1.93:/home/pi/it.unibo.qak21.basicrobot
    - avviare container:
        docker-compose -f basicrobotMbot.yaml run --service-ports mbot ./it.unibo.qak21.basicrobot
    -configurare json corretto:
        docker exec -it nome_container mv basicrobotConfigForNano.json basicrobotConfig.json
    - avvio il container al boot:
        docker update --restart always nome_container


## Deploy MaitreGUI
- Modifica file build.gradle.kts:
    Decommentare:
```
    java
	application
    distribution
```
```
    Aggiunta di:
    application {
        // Define the main class for the application.
        mainClass.set("it.unibo.maitreGUI.MaitreGuiApplicationKt")
    }

    version = "1.0"

    tasks.jar {
        manifest {
            attributes["Main-Class"] = "it.unibo.maitreGUI.MaitreGuiApplicationKt"
            attributes(mapOf("Implementation-Title" to project.name,
                "Implementation-Version" to project.version))
        }
    }
```
- Creazione container docker di MaitreGUI
        gradle -b build.gradle.kts build
        docker build -f Dockerfile -t maitregui:1.1 .
    ## run the image to test
        docker run -d --name maitregui -p 8081:8081/tcp -p 8081:8081/udp maitregui:1.1
    ## Tag the image
        docker tag maitregui:1.1 bluffgnuff/maitregui:1.1
    ## Register the image on DockerHub
        docker push bluffgnuff/maitregui:1.1

- Creazione container docker di fridge
    gradle -b build_ctxfridge.gradle build
    docker build -f Dockerfile -t fridgenode:1.1 .
    docker run -d --name fridgenode -p 8060:8060/tcp -p 8060:8060/udp fridgenode:1.1
    docker tag fridgenode:1.1 bluffgnuff/fridgenode:1.1
    docker push bluffgnuff/fridgenode:1.1

- Creazione container docker di rbr on Raspberry
    gradle -b build_ctxrbr.gradle build
    docker build -f DockerfileOnRasp -t rbrnode:1.1 .
    docker run -d --name rbrnode -p 8050:8050/tcp -p 8050:8050/udp rbrnode:1.1
    docker tag rbrnode:1.1 bluffgnuff/rbrnode:1.1
    docker push bluffgnuff/rbrnode:1.1

- Creazione container docker di rbr on PC (uguale a sopra ma modifica scelta del file docker)
    docker build -f DockerfileOnPC-t rbrnode_on_pc:1.1 .
    docker run -d --name rbrnode_on_pc -p 8050:8050/tcp -p 8050:8050/udp rbrnode_on_pc:1.1
    docker tag rbrnode_on_pc:1.1 bluffgnuff/rbrnode_on_pc:1.1
    docker push bluffgnuff/rbrnode_on_pc:1.1

- Creazione container docker di maitre
    gradle -b build_ctxmaitre.gradle build
    docker build -f Dockerfile -t maitrenode:1.1 .
    docker run -d --name maitrenode -p 8070:8070/tcp -p 8070:8070/udp maitrenode:1.1
    docker tag maitrenode:1.1 bluffgnuff/maitrenode:1.1
    docker push bluffgnuff/maitrenode:1.1
- passare rbr su raspberry

nei container è sufficiente impostare come ip degli altri l'in cui si trovano es 192.168.1.211 non importa se ripetuto
