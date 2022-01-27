package it.unibo.connQak

import it.unibo.connQak.ConnectionType

var mqtthostAddr    = "broker.hivemq.com"	//broker.hivemq.com
var mqttport		= "1883" 
var mqtttopic       = "unibo/maitre"
var hostAddr 		= "localhost" //   172.17.0.2 "192.168.1.5" "localhost"
var port     		= "8040"
var qakdestination 	= "maitre"
var ctxqakdest      = "ctxsystem"
var connprotocol    = ConnectionType.TCP //TCP COAP HTTP MQTT

//fun main(){ println("consoles") }