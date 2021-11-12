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

class PrepareTest {
		
	companion object {
		var rbrActor : ActorBasic? = null
		var systemStarted = false
		var testingObserverRbr: CoapObserverForTest ? = null
		var testingObserverTable: CoapObserverForTest ? = null
		var testingObserverFridge: CoapObserverForTest ? = null
		var testingObserverPantry: CoapObserverForTest ? = null
		val channelSyncStart = Channel<String>()
		var ip = "localhost"
		var ctx = "ctxsystem"
		var actname = "rbr"
		var actname2 = "pantry"
		var actname3 = "table"
		var actname4 = "fridge"
		var port = "8040"

		@JvmStatic
		@BeforeClass
		fun systemSetUp() {
			println ("TEST Init | Running context")

			GlobalScope.launch { 
				it.unibo.ctxsystem.main()
			}			
			
			println ("===============TEST Init | Activating Observers")
			GlobalScope.launch {
				rbrActor = QakContext.getActor("rbr")
				while(  rbrActor == null ) {
					delay(500)
					rbrActor = QakContext.getActor("rbr")
				}
				if( testingObserverRbr == null){
					 testingObserverRbr= CoapObserverForTest("testingObserver$actname","$ip", "$ctx", "$actname", "$port") 		
					println ("testingObserverRbr=$testingObserverRbr")
				}
				channelSyncStart.send("starttesting")
			}
			
			GlobalScope.launch {
				if( testingObserverPantry == null){
					 testingObserverPantry = CoapObserverForTest("testingObserver$actname2","$ip", "$ctx", "$actname2", "$port") 		
					println ("testingObserverPantry=$testingObserverPantry")
				}
				channelSyncStart.send("starttesting")
			} 			
			GlobalScope.launch {
				if( testingObserverTable == null){
					 testingObserverTable = CoapObserverForTest("testingObserver$actname3","$ip", "$ctx", "$actname3", "$port") 		
					println ("testingObserverTable=$testingObserverTable")
				}
				channelSyncStart.send("starttesting")
			}
			GlobalScope.launch {
				if( testingObserverFridge == null){
					 testingObserverFridge = CoapObserverForTest("testingObserver$actname4","$ip", "$ctx", "$actname4", "$port") 		
					println ("testingObserverFridge=$testingObserverFridge")
				}
				channelSyncStart.send("starttesting")
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

		if( ! systemStarted ) {
			runBlocking {
				channelSyncStart.receive()
				channelSyncStart.receive()
				channelSyncStart.receive()
				channelSyncStart.receive()
				systemStarted = true
				println ("===============TEST | checkSystemStarted resumed")
			}
		}				
  	}

	@After
	fun removeObs() {
		println ("+++++++++AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserverRbr!!.name}")
		testingObserverRbr!!.terminate()
	}

// Send a prepare and wait the end of the task prepare
	@Test
	fun PrepareTest() {
		var Crockerys =  arrayListOf(arrayListOf("dishes", "10"))
		var Foods= arrayListOf(arrayListOf("s001", "bread", "1")) 
		var msg = MsgUtil.buildDispatch("tester", "prepare", "prepare($Crockerys, $Foods)", "rbr")
		var State = ""
		var expected = "(0,0)"
		var expectedRBRPantry = "(0,6)"
		var expectedRBRFridge = "(5,0)"
		var expectedRBRTableP = "(1,3)"
		var expectedRBRTableF = "(4,2)"
		var expectedFridge = "Removed Food [[s001,bread,1]] with success!"
		var expectedPantry = "Removed Crockery [[dishes,10]] with success!"
		var expectedTableDish= "Added [[dishes,10]] with success!"
		var expectedTableFood= "Added [[s001,bread,1]] with success!"
		var PrevisionRBRPantry = expectedRBRPantry
		var PrevisionRBRFridge = expectedRBRFridge
		var PrevisionRBRTableP = expectedRBRTableP
		var PrevisionRBRTableF = expectedRBRTableF
		var Prevision = expected
		var PrevisionPantry = "Removed Crockery [[dishes,10]] with success!"
		var PrevisionTableDish = expectedTableDish
		var PrevisionFridge = "Removed Food [[s001,bread,1]] with success!"
		var PrevisionTableFood = expectedTableFood
		var channelForObserver = Channel<String>()

		runBlocking {
			//Sending msg prepare
			testingObserverRbr!!.addObserver( channelForObserver,expectedRBRPantry)
			
			delay (500)
			println ("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, rbrActor!!)
			
			//check rbrwalker arrives to the pantry
			//testingObserverRbr!!.addObserver( channelForObserver,expectedRBRPantry)		
			
			State = channelForObserver.receive()
			testingObserverRbr!!.removeObserver()
			assertEquals(PrevisionRBRPantry,State)

			//check state pantry
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverPantry!!.addObserver( channelForObserver,expectedPantry)
			
			State = channelForObserver.receive()
			assertEquals(PrevisionPantry,State)
			testingObserverPantry!!.removeObserver()
			
			//check rbrwalker arrives to the table		
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverRbr!!.addObserver( channelForObserver,expectedRBRTableP )
			
			State = channelForObserver.receive()
			assertEquals(PrevisionRBRTableP,State)
			testingObserverRbr!!.removeObserver()

			//check state table Dish
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverTable!!.addObserver( channelForObserver,expectedTableDish)
			
			State = channelForObserver.receive()
			assertEquals(PrevisionTableDish,State)
			testingObserverTable!!.removeObserver()
			
			//check rbrwalker arrives to the fridge
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverRbr!!.addObserver( channelForObserver,expectedRBRFridge )		
			
			State = channelForObserver.receive()
			testingObserverRbr!!.removeObserver()
			assertEquals(PrevisionRBRFridge,State)
			
			//check state fridge
			channelForObserver.cancel()
			channelForObserver= Channel<String>()
			testingObserverFridge!!.addObserver( channelForObserver,expectedFridge)

			State = channelForObserver.receive()
			assertEquals(PrevisionFridge,State)			
			testingObserverFridge!!.removeObserver()
			
			//check rbrwalker arrives to the table 
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverRbr!!.addObserver( channelForObserver,expectedRBRTableF )
			
			State = channelForObserver.receive()
			assertEquals(PrevisionRBRTableF,State)
			testingObserverRbr!!.removeObserver()

			//check state table Food
			channelForObserver.cancel()
			channelForObserver= Channel<String>()
			testingObserverTable!!.addObserver( channelForObserver,expectedTableFood)
			
			State = channelForObserver.receive()
			assertEquals(PrevisionTableFood,State)
			testingObserverTable!!.removeObserver()
			
			//check rbrwalker arrives to the RH
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverRbr!!.addObserver( channelForObserver,expected )
		
			State = channelForObserver.receive()
			assertEquals(Prevision,State)
		}
		channelForObserver.close()
	}	
}