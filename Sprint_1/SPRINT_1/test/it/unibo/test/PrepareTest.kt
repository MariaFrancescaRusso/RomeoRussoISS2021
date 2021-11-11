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
		val channelSyncStart = Channel<String>()
		var ip = "localhost"
		var ctx = "ctxsystem"
		var actname = "rbr"
		var actname2 = "table"
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
				if( testingObserverTable== null){
					 testingObserverTable= CoapObserverForTest("testingObserverTable","$ip", "$ctx", "$actname2", "$port") 		
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

//h_testingObserverPantry | content=moveactivated(w)  expected=giancarlo RESP-CODE=2.05
	//test movimenti
	//test risorse
	@Test
	fun PrepareTest() {
		var Crockerys =  arrayListOf(arrayListOf("dishes", "10"))
		var Foods= arrayListOf(arrayListOf("s001", "bread", "1")) 
		var msg = MsgUtil.buildDispatch("tester", "prepare", "prepare($Crockerys, $Foods)", "rbr")
		var State = ""
		var StateTable = ""
		var expected = "(0,0)"
		var Prevision = expected
		var expectedTable = "Added "
		var previsionTableFood= "Added [[s001,bread,1]] with success!"
		var previsionTableDish= "Added [[dishes,10]] with success!"
		val channelForObserver = Channel<String>()
		val channelForObserverTable = Channel<String>()
		
	
		testingObserverTable!!.addObserver( channelForObserverTable,expectedTable)
		testingObserverRbr!!.addObserver( channelForObserver,expected )
		//reading the result of the operation of adding the food on the table
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msg")	
			MsgUtil.sendMsg(msg, rbrActor!!)
			StateTable = channelForObserverTable.receive()
			assertEquals(previsionTableDish,StateTable)
		}

		runBlocking {
			delay(200)	
			
			StateTable = channelForObserverTable.receive()
			assertEquals(previsionTableFood,StateTable)	
		}
		channelForObserverTable.close()
	
		runBlocking {
			delay(200)
			State = channelForObserver.receive()	
			
			println ("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
		channelForObserver.close()
	}	
}