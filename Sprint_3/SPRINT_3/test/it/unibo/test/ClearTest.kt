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

class ClearTest {
	companion object {
		var maitreActor : ActorBasic? = null
		var systemStarted = false
		var testingObserverMaitre: CoapObserverForTest ? = null
		val channelSyncStart = Channel<String>()
		var ip = "localhost"
		var ctx = "ctxsystem"
		var actname = "maitre"
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
				maitreActor = QakContext.getActor("maitre")
				while(  maitreActor == null ) {
					delay(500)
					maitreActor = QakContext.getActor("maitre")
				}
				if( testingObserverMaitre== null){
					 testingObserverMaitre= CoapObserverForTest("testingObserverMaitre","$ip", "$ctx", "$actname", "$port") 		
					println ("testingObserverMaitre=$testingObserverMaitre")
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
		println ("+++++++++AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserverMaitre!!.name}")
		testingObserverMaitre!!.terminate()
	}

	// Send a prepare and wait the "Sent Prepare" maitre update state
	fun waitPrepare(){
		var Crockerys =  arrayListOf(arrayListOf("dishes", "10"))
		var Foods= arrayListOf(arrayListOf("s001", "bread", "1"))
		var msg = MsgUtil.buildDispatch("tester", "prepare", "prepare($Crockerys, $Foods)", "maitre")
		var State = ""
		var expected= "Sent Prepare"
		var channelForObserver = Channel<String>()

		runBlocking {
			testingObserverMaitre!!.addObserver( channelForObserver,expected)
		
			println ("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, maitreActor!!)
			State = channelForObserver.receive()
			testingObserverMaitre!!.removeObserver()
		}
		channelForObserver.close()
		println ("===============TEST | RESULT=$State for $msg")
	}
		
	@Test
	fun ClearTest() {
		var msg = MsgUtil.buildDispatch("tester", "clear", "clear(0)", "maitre")
		var State = ""
		var expected = "Sent Clear"
		var Prevision = expected
		var channelForObserver = Channel<String>()

		//waiting the "Sent Prepare" maitre update state
		waitPrepare()
		println ("===============TEST | Prepare finished")
		
		// Send a clear and wait the "Sent Clear" maitre update state
		runBlocking {
			testingObserverMaitre!!.addObserver( channelForObserver,expected)
		
			println ("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, maitreActor!!)
			State = channelForObserver.receive()
			testingObserverMaitre!!.removeObserver()
			assertEquals(Prevision,State)
		}
		println ("===============TEST | RESULT=$State for $msg")
	}	
}