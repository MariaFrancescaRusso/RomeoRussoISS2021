package it.unibo.maitreGUI

//import it.unibo.`is`.interfaces.protocols.IConnInteraction
//import it.unibo.connQak.ConnectionType
//import it.unibo.connQak.connQakBase
//import it.unibo.kactor.sysUtil
//import it.unibo.supports.FactoryProtocol
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.runBlocking
//import it.unibo.kactor.MsgUtil
//
//@kotlinx.coroutines.ObsoleteCoroutinesApi
//class MaitreResource (name: String, addrdest: String, portdest: String, ctxdest: String, actordest: String, protocol: ConnectionType ){
//	var caller = name
//	var addr = addrdest
//	var port = portdest
//	var ctxqakdest = ctxdest
//	var actor = actordest
//	var coap  = CoapSupport("coap://$addr:$port", "$ctxqakdest/$actor")
//	var conn  : connQakBase
//
//	init {
//		conn = connQakBase.create(protocol)
//		conn.createConnection()
//		println("MaitreResource | configured ${sysUtil.curThread()} ")
//	}
//
//	//TODO decidere se il messaggio viene preparato dal controller o qua
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	suspend fun execPrepare( foodAndCrockery: String) {
//		var message = MsgUtil.buildDispatch(caller, "prepare", "prepare($foodAndCrockery)", "maitre")
//		println("exec PREPARE")
//		//conn.forward( message)
//	}
//
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	fun execAddFood( foodcode: String):String {
//		var message = MsgUtil.buildDispatch(caller, "addfood", "addfood($foodcode)", "maitre")
//		println("exec ADDFOOD")
//		conn.forward( message)
//		runBlocking{
//			delay(100)
//		}
//		var	res = coap.readResource()
//		return res
//	}
//
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	suspend fun execClear( foodAndCrockery: String) {
//		var message = MsgUtil.buildDispatch(caller, "clear", "clear($foodAndCrockery)", "maitre")
//		println("exec CLEAR")
//		conn.forward( message  )
//	}
//
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	suspend fun execConsult():String {
//		var message = MsgUtil.buildDispatch(caller, "consult", "consult(0)", "maitre")
//		var s = message.toString()
//		println("exec CONSULT $s")
//		conn.forward( message)
//		runBlocking{
//			delay(100)
//		}
//		var res = coap.readResource()
//		return res
//	}
//
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	suspend fun execStop():String {
//		var message = MsgUtil.buildDispatch(caller, "stop", "stop(0)", "maitre")
//		println("exec STOP")
//		conn.forward( message)
//		runBlocking{
//			delay(100)
//		}
//		var res = coap.readResource()
//		return res
//	}
//
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	suspend fun execReactivate() {
//		var message = MsgUtil.buildDispatch(caller, "reactivate", "reactivate(0)", "maitre")
//		println("exec REACTIVATE")
//		conn.forward( message  )
//	}
//
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	suspend fun execKillMaitre() {
//		var message = MsgUtil.buildDispatch(caller, "end", "end(0)", "maitre")
//		println("exec TERMINATE")
//		conn.forward( message  )
//	}
//}