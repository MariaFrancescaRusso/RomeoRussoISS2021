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


class ClaerTest {
	companion object {
		var rbrActor : ActorBasic? = null
		var systemStarted = false
		var testingObserverRbr: CoapObserverForTest ? = null
		val channelSyncStart = Channel<String>()
		var ip = "localhost"
		var ctx = "ctxsystem"
		var actname = "rbr"
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
	fun ClearTest() {
		var Crockerys =  arrayListOf(arrayListOf("dishes", "10"))
		var Foods= arrayListOf(arrayListOf("s035", "cocacola", "1")) 
		var msg = MsgUtil.buildDispatch("tester", "clear", "clear($Crockerys, $Foods)", "rbr")
		var State = ""
		var expected = "(0,0) terminating RBR"
		var Prevision = expected
		val channelForObserver = Channel<String>()

		waitPrepare()

		testingObserverRbr!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			delay(200)
			println ("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, rbrActor!!)
			State = channelForObserver.receive()	
			
			println ("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
		channelForObserver.close()
	}	
}
