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

class FridgeTest {
		
	companion object {
		var fridgeActor : ActorBasic? = null
		var systemStarted         = false
		var testingObserverFridge   : CoapObserverForTest ? = null
		val channelSyncStart      = Channel<String>()
	
		@JvmStatic
		@BeforeClass
		fun systemSetUp() {
			println("===============TEST Init | Running context ")

			GlobalScope.launch{ 
				it.unibo.ctxsystem.main()
			}			
/*		
			GlobalScope.launch{ 
				it.unibo.ctxfridge.main()
			}
*/
/*			
			GlobalScope.launch{ 
				it.unibo.ctxrbr.main()
			}
*/
/*		
			GlobalScope.launch{ 
				it.unibo.ctxmaitre.main()
			}
*/									
			println("===============TEST Init | Activating Observers")

			GlobalScope.launch {
				fridgeActor = QakContext.getActor("fridge")
				while(  fridgeActor == null ) {
					delay(500)
					fridgeActor = QakContext.getActor("fridge")
				}
				channelSyncStart.send("starttesting1")
			}
		}
			
		@JvmStatic
	    @AfterClass
		fun terminate() {
			println("${testingObserverFridge!!.name}")
			testingObserverFridge!!.terminate()
			testingObserverFridge = null
						
			println("===============TEST | terminate the testing")				
		}
	}
	
	@Before
	fun checkSystemStarted()  {
		var ip = "localhost"
//		var ip = "127.0.0.1"
		var ctx = "ctxsystem"
//		var ctx = "ctxfridge"
		var actname = "fridge"
		var port = "8040"
//		var port = "8060"
		
		if( ! systemStarted ) {
			runBlocking {
				channelSyncStart.receive()
				systemStarted = true
				println("===============TEST | checkSystemStarted resumed")
			}
		}
		
		if( testingObserverFridge == null) testingObserverFridge = CoapObserverForTest("testingObserverFridge","$ip", "$ctx", "$actname", "$port")
		println("testingObserverFridge=$testingObserverFridge")
  	}

		
	@Test
	fun AddFoodFridgeTest(){
		var	Food = arrayListOf(arrayListOf("s034", "cheddar", "10"))
		var fridgePrevision = "Add Food [[s034,cheddar,10]] with success!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(add, $Food)", "fridge")
		var fridgeState = ""
		var expected = fridgePrevision
		val channelForObserver = Channel<String>()		
		
		testingObserverFridge!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, fridgeActor!!)
			fridgeState = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$fridgeState for $msg")
			assertEquals(fridgePrevision,fridgeState)
		}
	}
	
	@Test
	fun RemoveFoodFridgeTest() {
		var	Food = arrayListOf(arrayListOf("s001", "bread", "10"))
		var fridgePrevision = "Remove Food [[s001,bread,10]] with success!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Food)", "fridge")
		var fridgeState = ""
		var expected = fridgePrevision
		val channelForObserver = Channel<String>()		
		
		testingObserverFridge!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, fridgeActor!!)
			fridgeState = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$fridgeState for $msg")
			assertEquals(fridgePrevision,fridgeState)
		}
	}
	
	@Test
	fun RemoveFoodFridgeFailTest() {
		var	Food = arrayListOf(arrayListOf("x999", "mysteryfood", "1"))
		var Prevision = "Fail removing Food [[x999,mysteryfood,1]]!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Food)", "fridge")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverFridge!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, fridgeActor!!)
			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun RemoveFoodFridgeFailQuantityTest() {
		var	Food = arrayListOf(arrayListOf("s001", "bread", "16"))
		var Prevision = "Fail removing Food [[s001,bread,16]]!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Food)", "fridge")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverFridge!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, fridgeActor!!)
			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
 }