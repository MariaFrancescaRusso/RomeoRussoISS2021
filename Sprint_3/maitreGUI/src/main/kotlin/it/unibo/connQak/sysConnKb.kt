package it.unibo.connQak

import it.unibo.connQak.ConnectionType

val mqtthostAddr    = "broker.hivemq.com"	//broker.hivemq.com
val mqttport		= "1883" 
val mqtttopic       = "unibo/maitre"
val hostAddr 		= "localhost" //   172.17.0.2 "192.168.1.5" "localhost"
val port     		= "8040"
val qakdestination 	= "maitre"
val ctxqakdest      = "ctxsystem"
val connprotocol    = ConnectionType.TCP //TCP COAP HTTP MQTT

//fun main(){ println("consoles") }