package it.unibo.test

import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.GlobalScope
import org.junit.BeforeClass
import org.junit.AfterClass
import org.junit.After
import org.junit.Assert.*
import kotlin.jvm.JvmStatic
import kotlinx.coroutines.channels.Channel
import util.CoapObserverForTest

class DishwasherTest {
		
	companion object {
		var dishwasherActor : ActorBasic? = null
		var systemStarted = false
		var testingObserverdishwasher : CoapObserverForTest ? = null
		val channelSyncStart = Channel<String>()
	
		@JvmStatic
		@BeforeClass
		fun systemSetUp() {
			println ("===============TEST Init | Running context")

			GlobalScope.launch { 
				it.unibo.ctxsystem.main()
			}			
/*		
			GlobalScope.launch { 
				it.unibo.ctxfridge.main()
			}
*/
/*			
			GlobalScope.launch { 
				it.unibo.ctxrbr.main()
			}
*/
/*		
			GlobalScope.launch { 
				it.unibo.ctxmaitre.main()
			}
*/									
			println("===============TEST Init | Activating Observers")
			
			GlobalScope.launch {
				dishwasherActor = QakContext.getActor("dishwasher")
				while(  dishwasherActor == null ) {
					delay(500)
					dishwasherActor = QakContext.getActor("dishwasher")
				}
				channelSyncStart.send("starttesting2")
			}
		}
			
		@JvmStatic
	    @AfterClass
		fun terminate() {			
			println ("===============TEST | terminate the testing")				
		}
	}
	
	@Before
	fun checkSystemStarted() {
		var ip = "localhost"
//		var ip = "192.168.1.211"
		var ctx = "ctxsystem"
//		var ctx = "ctxmaitre"
		var actname = "dishwasher"
		var port = "8040"
//		var port = "8070"
		
		if( ! systemStarted ) {
			runBlocking {
				channelSyncStart.receive()
				systemStarted = true
				println ("===============TEST | checkSystemStarted resumed")
			}
		}
				
		if( testingObserverdishwasher == null) testingObserverdishwasher = CoapObserverForTest("testingObserverdishwasher","$ip", "$ctx", "$actname", "$port")
		println ("testingObserverdishwasher=$testingObserverdishwasher")
  	}

	@After
	fun removeObs() {
		println ("+++++++++AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserverdishwasher!!.name}")
		testingObserverdishwasher!!.terminate()
		testingObserverdishwasher = null

		runBlocking {
			delay(1000)
		}
	}
	
	@Test
	fun AddDishdishwasherTest() {
		var	Dishes = arrayListOf(arrayListOf("dishes", "10"))
		var Prevision = "Added Crockery [[dishes,10]] with success!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(add, $Dishes)", "dishwasher")
		var State = ""
		var expected = "Added "
		val channelForObserver = Channel<String>()
		
		testingObserverdishwasher!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, dishwasherActor!!)
			State = channelForObserver.receive()			
			
			println ("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun RemoveDishdishwasherTest() {
		var	Dishes = arrayListOf(arrayListOf("dishes", "10"))
		var Prevision = "Removed Crockery [[dishes,10]] with success!"
		var msgAdd = MsgUtil.buildDispatch("tester", "changeState", "changeState(add, $Dishes)", "dishwasher")
		var msgRemove= MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Dishes)", "dishwasher")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverdishwasher!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msgAdd")
			MsgUtil.sendMsg(msgAdd, dishwasherActor!!)
			println ("===============TEST | sending $msgRemove")
			MsgUtil.sendMsg(msgRemove, dishwasherActor!!)

			State = channelForObserver.receive()			
			
			println ("===============TEST | RESULT=$State for $msgRemove")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun RemoveDishdishwasherFailTest() {
		var	Dishes = arrayListOf(arrayListOf("cristal_glasses", "10"))
		var Prevision = "Fail removing Crockery [[cristal_glasses,10]]!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Dishes)", "dishwasher")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverdishwasher!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, dishwasherActor!!)
			State = channelForObserver.receive()			
			
			println ("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun RemoveDishdishwasherFailQuantityTest() {
		var	Dishes = arrayListOf(arrayListOf("dishes", "31"))
		var Prevision = "Fail removing Crockery [[dishes,31]]!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Dishes)", "dishwasher")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverdishwasher!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, dishwasherActor!!)
			State = channelForObserver.receive()			
			
			println ("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
 }