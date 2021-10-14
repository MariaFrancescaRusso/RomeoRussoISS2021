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

class PantryTest {
		
	companion object {
		var pantryActor : ActorBasic? = null
		var systemStarted         = false
		var testingObserverPantry 	: CoapObserverForTest ? = null
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
				pantryActor = QakContext.getActor("pantry")
				while(  pantryActor == null ) {
					delay(500)
					pantryActor = QakContext.getActor("pantry")
				}
				channelSyncStart.send("starttesting2")
			}
		}
			
		@JvmStatic
	    @AfterClass
		fun terminate() {			
			println("===============TEST | terminate the testing")				
		}
	}
	
	@Before
	fun checkSystemStarted()  {
		var ip = "localhost"
//		var ip = "192.168.1.211"
		var ctx = "ctxsystem"
//		var ctx = "ctxmaitre"
		var actname = "pantry"
		var port = "8040"
//		var port = "8070"
		
		if( ! systemStarted ) {
			runBlocking {
				channelSyncStart.receive()
				systemStarted = true
				println("===============TEST | checkSystemStarted resumed")
			}
		}
				
		if( testingObserverPantry == null) testingObserverPantry = CoapObserverForTest("testingObserverPantry","$ip", "$ctx", "$actname", "$port")
		println("testingObserverPantry=$testingObserverPantry")
  	}

	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserverPantry!!.name}")
		testingObserverPantry!!.terminate()
		testingObserverPantry = null

		runBlocking{
			delay(1000)
		}
	}
	@Test
	fun AddDishPantryTest() {
		var	Dishes = arrayListOf(arrayListOf("dishes", "10"))
		var Prevision = "Add Crockery [[dishes,10]] with success!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(add, $Dishes)", "pantry")
		var State = ""
		var expected = "Add "
		val channelForObserver = Channel<String>()
		
		testingObserverPantry!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			delay(200)
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, pantryActor!!)
			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun RemoveDishPantryTest() {
		var	Dishes = arrayListOf(arrayListOf("dishes", "10"))
		var Prevision = "Remove Crockery [[dishes,10]] with success!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Dishes)", "pantry")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverPantry!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			delay(200)
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, pantryActor!!)
			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun RemoveDishPantryFailTest() {
		var	Dishes = arrayListOf(arrayListOf("cristal_glasses", "10"))
		var Prevision = "Fail removing Crockery [[cristal_glasses,10]]!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Dishes)", "pantry")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverPantry!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			delay(200)
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, pantryActor!!)
			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun RemoveDishPantryFailQuantityTest() {
		var	Dishes = arrayListOf(arrayListOf("dishes", "31"))
		var Prevision = "Fail removing Crockery [[dishes,31]]!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Dishes)", "pantry")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverPantry!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			delay(200)
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, pantryActor!!)
			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
 }