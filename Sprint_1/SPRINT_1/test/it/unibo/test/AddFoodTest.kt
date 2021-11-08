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

class AddFoodTest {
	companion object {
		var rbrActor : ActorBasic? = null
		var systemStarted = false
		var testingObserverRbr: CoapObserverForTest ? = null
		var testingObserverTable: CoapObserverForTest ? = null
		var testingObserverFridge: CoapObserverForTest ? = null
		val channelSyncStart = Channel<String>()
		var ip = "localhost"
		var ctx = "ctxsystem"
		var actname = "rbr"
		var actname2 = "fridge"
		var actname3 = "table"
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
					 testingObserverRbr= CoapObserverForTest("testingObserverRbr","$ip", "$ctx", "$actname", "$port") 		
					println ("testingObserverRbr=$testingObserverRbr")
				}				
				channelSyncStart.send("starttesting")
			}
			GlobalScope.launch {
				if( testingObserverFridge == null){
					 testingObserverFridge= CoapObserverForTest("testingObserverFridge","$ip", "$ctx", "$actname2", "$port") 		
					println ("testingObserverFridge=$testingObserverFridge")
				}
				channelSyncStart.send("starttesting")
			}
			GlobalScope.launch {
				if( testingObserverTable== null){
					 testingObserverTable= CoapObserverForTest("testingObserverTable","$ip", "$ctx", "$actname3", "$port") 		
					println ("testingObserverTable=$testingObserverTable")
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
				systemStarted = true
				//TODO send prepare
				println ("===============TEST | checkSystemStarted resumed")
			}
		}				
  	}

	@After
	fun removeObs() {
		println ("+++++++++AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserverRbr!!.name}")
		testingObserverRbr!!.terminate()
		testingObserverFridge!!.terminate()
	}

// Send a prepare and wait the end of the task
	fun waitPrepare(){
		var Crockerys =  arrayListOf(arrayListOf("dishes", "10"))
		var Foods= arrayListOf(arrayListOf("s001", "bread", "1")) 
		val channelForObserverPrepare = Channel<String>()
		
		var msgPrepare = MsgUtil.buildDispatch("tester", "prepare", "prepare($Crockerys, $Foods)", "rbr")
		var expectedPrepare = "(0,0)"
		testingObserverRbr!!.addObserver( channelForObserverPrepare,expectedPrepare )
		
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msgPrepare")
			MsgUtil.sendMsg(msgPrepare, rbrActor!!)
			channelForObserverPrepare.receive()
			testingObserverRbr!!.removeObserver()
		}
		channelForObserverPrepare.close()
	}
	
	@Test
	fun AddFoodTest() {
		//This food is present in the fridge because it s loaded from the Fridge.pl if the food it s removed from that file also this must be changed		
		var msg = MsgUtil.buildRequest("tester", "addFood", "addFood(s002)", "rbr")
		var State = ""
		var StateFridge = ""
		var StateTable = ""
		var expected = "(0,0)"
		var expectedFridge= "Removed Food [[s002,sandwich,1]] with success!"
		var PrevisionFridge = expectedFridge
		var Prevision = expected
		var expectedTable= "Added [[s002,sandwich,1]] with success!"
		var PrevisionTable = expectedTable
		val channelForObserver = Channel<String>()
		val channelForObserverFridge = Channel<String>()
		val channelForObserverTable = Channel<String>()

		//waiting the rbr finishes the prepare task
		waitPrepare()
		println ("===============TEST | Prepare finished")
		testingObserverFridge!!.addObserver( channelForObserverFridge,expectedFridge )
		
		//reading the result of the removing operation from food
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msg")

			MsgUtil.sendMsg(msg, rbrActor!!)
			
			StateFridge = channelForObserverFridge.receive()
			
		}
		channelForObserverFridge.close()
		
		testingObserverTable!!.addObserver( channelForObserverTable,expectedTable )
		//reading the result of the operation of adding the food on the table
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msg")	
			
			StateTable = channelForObserverTable.receive()	
		}
		channelForObserverTable.close()
		testingObserverRbr!!.addObserver( channelForObserver,expected )
		//reading the position of the rbr
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msg")	
			
			State = channelForObserver.receive()	
		}

		println ("===============TEST | RESULT=$State for $msg")
		assertEquals(PrevisionFridge,StateFridge)
		assertEquals(Prevision,State)
		channelForObserver.close()
	}	
}