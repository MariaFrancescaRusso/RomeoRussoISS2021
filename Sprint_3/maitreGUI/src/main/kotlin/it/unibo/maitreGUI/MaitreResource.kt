package it.unibo.maitreGUI

import it.unibo.connQak.*
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@kotlinx.coroutines.ObsoleteCoroutinesApi
class MaitreResource (name: String) {
	var caller = name
	var coap  = CoapSupport("coap://$hostAddr:$port", "$ctxqakdest/$qakdestination")
	var conn  : connQakBase

	init {
		conn = connQakBase.create(connprotocol)
		conn.createConnection()
		println("MaitreResource | configured ${sysUtil.curThread()} ")
	}

	suspend fun execPrepare( Crockery: String, Food: String) {
		var message = MsgUtil.buildDispatch(caller, "prepare", "prepare($Crockery, $Food)", "maitre")
		println("exec PREPARE")
		conn.forward(message)
	}

	suspend fun execAddFood( foodcode: String):String {
		var message = MsgUtil.buildDispatch(caller, "addFood", "addFood($foodcode)", "maitre")
		println("exec ADDFOOD")
		conn.forward(message)

		var res : String
		do {
			runBlocking() {
				delay(4000)	// = AddFoodtime+1000 in model.qak
			} 
			res = coap.readResource()
			println("MAITRERESOURCE | res: $res")
		}
		while(!res.startsWith("Warning!") && res!=("Request accepted!"))
		
		return res
	}

	suspend fun execClear() {
		var message = MsgUtil.buildDispatch(caller, "clear", "clear(0)", "maitre")
		println("exec CLEAR")
		conn.forward(message)
	}

	suspend fun execConsult():String {
		var message = MsgUtil.buildDispatch(caller, "consult", "consult(0)", "maitre")
		var s = message.toString()
		println("exec CONSULT $s")
		conn.forward(message)
		
		var res : String
		do {
			runBlocking() {
				delay(1000)
			} 
			res = coap.readResource()
			println("MAITRERESOURCE | res: $res")
		}
		while(!res.startsWith("{"))
		
		return res
	}

	suspend fun execStop():String {
		var message = MsgUtil.buildDispatch(caller, "stop", "stop(0)", "maitre")
		println("exec STOP")
		conn.forward(message)
		
		var res : String
		do {
			runBlocking() {
				delay(1000)
			}
			res = coap.readResource()
			println("MAITRERESOURCE | res: $res")
		}
		while(!res.startsWith("There is NO") && !res.startsWith("Task stopped"))
		
		return res
	}

	suspend fun execReactivate() {
		var message = MsgUtil.buildDispatch(caller, "reactivate", "reactivate(0)", "maitre")
		println("exec REACTIVATE")
		conn.forward(message)
	}

	suspend fun execKillMaitre() {
		var message = MsgUtil.buildDispatch(caller, "end", "end(0)", "maitre")
		println("exec TERMINATE")
		conn.forward(message)
	}
}