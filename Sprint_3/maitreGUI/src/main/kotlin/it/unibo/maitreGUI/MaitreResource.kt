//package it.unibo.maitreGUI
//
//import it.unibo.actor0.ActorBasicKotlin
//import it.unibo.actor0.sysUtil
//import it.unibo.kactor.ActorBasic
//import it.unibo.kactor.MsgUtil
//import it.unibo.kactor.QakContext
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import org.springframework.stereotype.Component
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.runBlocking
//import it.unibo.actor0.ApplMessage
//import kotlinx.coroutines.channels.Channel
//
////TODO switch librerie coroutin
//@kotlinx.coroutines.ObsoleteCoroutinesApi
//@Component
//class MaitreResource (name: String, scope:CoroutineScope, val channel: Channel<String>): ActorBasicKotlin(name, scope){
//    lateinit var myscope: CoroutineScope
//    var configured = false
//    var caller = "Spring"
////    var maitre : ActorBasic? = null
//    
//	//Ho rimosso local
//    fun initMaitreResource() {
//        if (configured) {
//            println("MaitreResource | already configured ${sysUtil.curThread()} ")
//            return
//        }
//		//Not already configured
//	    configured = true
//	    myscope = CoroutineScope(Dispatchers.Default)
////	    runBlocking{
////	    	maitre= QakContext.getActor("maitre")
////			while(  maitre == null ) {
////				delay(500)
////				maitre= QakContext.getActor("maitre")
////			}
////		}
//		
//        //TODO: define an observer that updates the HTML page
//        //TODO posso modificate ActorCoapObserver in modo che invii messaggi come CoapObserverForTest e sfruttare una receive per gestire meglio il tutto
//        //TODO mi creao un canale che riceve messaggi dall'observer che li invia o tramite l'attore o tramite una MsgUtil.sendMsg
//        //  obs = DoNothingObserver("obs", myscope)
//        println("MaitreResource | configured ${sysUtil.curThread()} ")
//	}
//
//    //TODO decidere se il messaggio viene preparato dal controller o qua
//    @kotlinx.coroutines.ObsoleteCoroutinesApi
//    suspend fun execPrepare( foodAndCrockery: String) {
//        var message = MsgUtil.buildDispatch(caller, "prepare", "prepare($foodAndCrockery)", "maitre")
//        println("exec PREPARE")
////        MsgUtil.sendMsg(message, maitre!!)
//        //maitre!!.forward("consult", "consult(0)", "maitre")
//		
//    }
//
//    @kotlinx.coroutines.ObsoleteCoroutinesApi
//    suspend fun execAddFood( foodcode: String):String {
//        var message = MsgUtil.buildDispatch(caller, "addfood", "addfood($foodcode)", "maitre")
//        var res = ""
//        println("exec ADDFOOD")
////        MsgUtil.sendMsg(message, maitre!!)
//        //TODO: define an observer that updates the HTML page
//        return res
//    }
//
//    @kotlinx.coroutines.ObsoleteCoroutinesApi
//    suspend fun execClear( foodAndCrockery: String) {
//        var message = MsgUtil.buildDispatch(caller, "clear", "clear($foodAndCrockery)", "maitre")
//        println("exec CLEAR")
////        MsgUtil.sendMsg(message, maitre!!)
//    }
//
//    @kotlinx.coroutines.ObsoleteCoroutinesApi
//    suspend fun execConsult():String {
//        var message = MsgUtil.buildDispatch(caller, "consult", "consult(0)", "maitre")
//        var res = ""
//        println("exec CONSULT")
////        MsgUtil.sendMsg(message, maitre!!)
//        //TODO: define an observer that updates the HTML page
//        return res
//    }
//
//    @kotlinx.coroutines.ObsoleteCoroutinesApi
//    suspend fun execStop():String {
//        var message = MsgUtil.buildDispatch(caller, "stop", "stop(0)", "maitre")
//        var res = ""
//        println("exec STOP")
////        MsgUtil.sendMsg(message, maitre!!)
//        //TODO: define an observer that updates the HTML page
//        return res
//    }
//
//    @kotlinx.coroutines.ObsoleteCoroutinesApi
//    suspend fun execReactivate() {
//        var message = MsgUtil.buildDispatch(caller, "reactivate", "reactivate(0)", "maitre")
//        println("exec REACTIVATE")
////        MsgUtil.sendMsg(message, maitre!!)
//    }
//
//    @kotlinx.coroutines.ObsoleteCoroutinesApi
//    suspend fun execKillMaitre() {
//        var message = MsgUtil.buildDispatch(caller, "end", "end(0)", "maitre")
//        println("exec TERMINATE")
////        MsgUtil.sendMsg(message, maitre!!)
//    }
//	 override suspend fun handleInput(msg : ApplMessage){
//        if( msg.msgContent.contains("maitreResource")){
//            return
//        } //Do not send sonar info as answers
////        owner?.send(msg)   //interact with the owner (only)
////        if( owner == null ) //not working for an owner => working for the m2m
////            channel.send( msg.msgContent )
//    }
//}