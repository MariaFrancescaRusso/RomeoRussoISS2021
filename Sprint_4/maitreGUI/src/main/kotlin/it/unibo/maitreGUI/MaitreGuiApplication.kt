package it.unibo.maitreGUI

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MaitreGuiApplication

fun main(args : Array<String>) {
	runApplication<MaitreGuiApplication>(*args)
}