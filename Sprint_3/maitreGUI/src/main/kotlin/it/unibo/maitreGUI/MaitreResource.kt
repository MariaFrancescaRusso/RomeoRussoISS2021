package it.unibo.maitreGUI

import it.unibo.connQak.ConnectionType
import it.unibo.connQak.connQakBase
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@kotlinx.coroutines.ObsoleteCoroutinesApi
class MaitreResource (name: String, addrdest: String, portdest: String, ctxdest: String, actordest: String, protocol: ConnectionType ){
	var caller = name
	var addr = addrdest
	var port = portdest
	var ctxqakdest = ctxdest
	var actor = actordest
	var coap  = CoapSupport("coap://$addr:$port", "$ctxqakdest/$actor")
	var conn  : connQakBase

	init {
		conn = connQakBase.create(protocol)
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
//		runBlocking{
			delay(2000)
//		}
		var	res = coap.readResource()
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
				delay(1500)
			} 
//			delay(1500)
			res = coap.readResource()
			println("MAITRERESOURCE | res: $res")
		}
		while(!res.startsWith("{"))
		
		return res
	}

	suspend fun execStop():String {
		var message = MsgUtil.buildDispatch(caller, "stop", "stop(0)", "maitre")
		println("exec STOP")
		conn.forward( message)
		
		var res : String
		do {
			runBlocking() {
				delay(1500)
			}
//			delay(1500)
			res = coap.readResource()
			println("MAITRERESOURCE | res: $res")
		}
		while(!res.startsWith("There is NO") && !res.startsWith("Task stopped"))
		
		return res
	}

	suspend fun execReactivate() {
		var message = MsgUtil.buildDispatch(caller, "reactivate", "reactivate(0)", "maitre")
		println("exec REACTIVATE")
		conn.forward( message  )
	}

	suspend fun execKillMaitre() {
		var message = MsgUtil.buildDispatch(caller, "end", "end(0)", "maitre")
		println("exec TERMINATE")
		conn.forward( message  )
	}
}