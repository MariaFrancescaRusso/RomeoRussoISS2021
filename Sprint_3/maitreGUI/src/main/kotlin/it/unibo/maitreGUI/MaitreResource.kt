package it.unibo.maitreGUI

import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.MsgUtil
import it.unibo.kactor.sysUtil
import it.unibo.supports.FactoryProtocol
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@kotlinx.coroutines.ObsoleteCoroutinesApi
class MaitreResource (name: String, addrdest: String, portdest: String, ctxdest: String, actordest: String){
	var caller = name
	var addr = addrdest
	var port = portdest
	var ctxqakdest = ctxdest
	var actor = actordest
	var coap  = CoapSupport("coap://$addr:$port", "$ctxqakdest/$actor")
	var conn   : IConnInteraction

	//TODO valutare se la connessione può essere delegata ad un altro oggetto
	init {
		val fp	= FactoryProtocol(null,"TCP","connQakTcp")
		conn	= fp.createClientProtocolSupport(addr, port.toInt() )
		println("MaitreResource | configured ${sysUtil.curThread()} ")
	}

	//TODO decidere se il messaggio viene preparato dal controller o qua
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun execPrepare( foodAndCrockery: String) {
		var message = MsgUtil.buildDispatch(caller, "prepare", "prepare($foodAndCrockery)", "maitre")
		println("exec PREPARE")
		conn.sendALine( message.toString())	
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun execAddFood( foodcode: String):String {
		var message = MsgUtil.buildDispatch(caller, "addfood", "addfood($foodcode)", "maitre")
		println("exec ADDFOOD")
		conn.sendALine( message.toString())
		runBlocking{
			delay(100)
		}
		var	res = coap.readResource()
		return res
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun execClear( foodAndCrockery: String) {
		var message = MsgUtil.buildDispatch(caller, "clear", "clear($foodAndCrockery)", "maitre")
		println("exec CLEAR")
		conn.sendALine( message.toString()  )
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun execConsult():String {
		var message = MsgUtil.buildDispatch(caller, "consult", "consult(0)", "maitre")
		var s = message.toString()
		println("exec CONSULT $s")
		conn.sendALine( message.toString())
		runBlocking{
			delay(100)
		}
		var res = coap.readResource()
		return res
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun execStop():String {
		var message = MsgUtil.buildDispatch(caller, "stop", "stop(0)", "maitre")
		println("exec STOP")
		conn.sendALine( message.toString())
		runBlocking{
			delay(100)
		}
		var res = coap.readResource()
		return res
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun execReactivate() {
		var message = MsgUtil.buildDispatch(caller, "reactivate", "reactivate(0)", "maitre")
		println("exec REACTIVATE")
		conn.sendALine( message.toString()  )
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun execKillMaitre() {
		var message = MsgUtil.buildDispatch(caller, "end", "end(0)", "maitre")
		println("exec TERMINATE")
		conn.sendALine( message.toString()  )
	}
}