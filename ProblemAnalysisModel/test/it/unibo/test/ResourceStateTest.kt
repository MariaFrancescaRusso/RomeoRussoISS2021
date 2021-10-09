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

class ResourceStateTest{
		
	companion object{
		var myactor : ActorBasic? = null
		val FridgeObserver = util.ActorCoapObserver("localhost",8060,"ctxfridge","fridge")
		val TableObserver = util.ActorCoapObserver("localhost",8080,"ctxres","table")
		val PantryObserver = util.ActorCoapObserver("localhost",8080,"ctxres","pantry")
		val DishwasherObserver = util.ActorCoapObserver("localhost",8080,"ctxres","dishwasher")
	
		@JvmStatic
		@BeforeClass
		fun systemSetUp() {
			println("Test Init | Running context ")
			GlobalScope.launch{ 
				it.unibo.ctxfridge.main()
			}
			GlobalScope.launch{
				it.unibo.ctxrbr.main()
			}	
			println("Test Init | Activating Observers")
			GlobalScope.launch{
				myactor=QakContext.getActor("rbr")
				while(  myactor == null ){
					delay(500)
					myactor=QakContext.getActor("rbr")
				}
			}
		}
	
			@Before
		fun activateObservers(){
			runBlocking{
				FridgeObserver.activate()
				TableObserver.activate()
				PantryObserver.activate()
				DishwasherObserver.activate()
			}
		}
	
	    @After
		fun terminate() {
			println("Test | terminate the testing")
		}				
	}
	
	@Test
	fun prepareTest(){
		var prepare= MsgUtil.buildDispatch("tester", "prepare", "prepare(0)", "rbr")
		var fridgeState = ""
		var fridgePrevision = "State:a"
		var tableOState = ""
		var tablePrevision = ""
		var pantryState = ""
		var pantryPrevision = ""
		var dishwasherState = ""
		var dishwasherPrevision = ""
		val channelForObserver = Channel<String>()
//		testingObserver!!.addObserver( channelForObserver,"step" )
		
		println("Test | starting")
		while( true ){
			MsgUtil.sendMsg(prepare, myactor!!)
			result = channelForObserver.receive()
			if( result == "step(350)") {
				result = channelForObserver.receive()
			}
			println("+++++++++  stepUntilObstacle RESULT=$result for $stepRequest")
 			if( result.contains("stepFail")) break
			delay(300)  //just to slow down a bit and allow to do a backstep...
		}
		runBlocking{
			delay(5000)
			
			delay(5000)
			fridgeState = FridgeObserver.getCoapState();
			println("Test |ricevuto $fridgeState")
		
			assertEquals(fridgePrevision,fridgeState)
		}	
	}	
}