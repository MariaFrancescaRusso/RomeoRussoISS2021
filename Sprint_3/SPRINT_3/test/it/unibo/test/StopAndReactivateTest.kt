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

class StopAndReactivateTest {
	companion object {
		var rbrWalkerActor : ActorBasic? = null
		var systemStarted = false
		var testingObserverRbrWalker: CoapObserverForTest ? = null
		val channelSyncStart = Channel<String>()
		var ip = "localhost"
		var ctx = "ctxsystem"
		var actname = "rbrwalker"
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
				rbrWalkerActor= QakContext.getActor("rbrwalker")
				while(  rbrWalkerActor== null ) {
					delay(500)
					rbrWalkerActor= QakContext.getActor("rbrwalker")
				}
				if( testingObserverRbrWalker == null){
					 testingObserverRbrWalker= CoapObserverForTest("testingObserverRbrWalker","$ip", "$ctx", "$actname", "$port") 		
					println ("testingObserverRbrWalker=$testingObserverRbrWalker")
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
		println ("+++++++++AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserverRbrWalker!!.name}")
		testingObserverRbrWalker!!.terminate()
	}

	@Test
	fun StopReactivateTest() {

		var msg = MsgUtil.buildRequest("tester", "setGoal", "setGoal(5,5, downDir)", "rbrwalker")
		var msgStop = MsgUtil.buildRequest("tester", "stop", "stop(0)", "rbrwalker")
		var msgReactivate = MsgUtil.buildDispatch("tester", "reactivate", "reactivate(0)", "rbrwalker")
		var State = ""
		var expected = "(5, 5)"
		var expectedStop = "Stopped"
		var expectedStopFail = "Stop Fail"
		var expectedReactivate = "Reactivated"
		var Prevision = expected
		var PrevisionStop = expectedStop
		var PrevisionStopFail = expectedStopFail
		var PrevisionReactivate = expectedReactivate
		var channelForObserver = Channel<String>()

		
		runBlocking {
			//check that a stop request fail when the rbrwalker is in RH
			testingObserverRbrWalker!!.addObserver( channelForObserver,expectedStopFail)
			println ("===============TEST | sending first $msgStop")
			MsgUtil.sendMsg(msgStop, rbrWalkerActor!!)

			State = channelForObserver.receive()
			assertEquals(PrevisionStopFail,State)			
			testingObserverRbrWalker!!.removeObserver()
			
			//send setGoal request
			println ("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, rbrWalkerActor!!)
			delay(100)

			//check rbrWalker is stopped
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverRbrWalker!!.addObserver( channelForObserver,expectedStop)
			println ("===============TEST | sending second $msgStop")
			MsgUtil.sendMsg(msgStop, rbrWalkerActor!!)

			State = channelForObserver.receive()
			assertEquals(PrevisionStop,State)			
			testingObserverRbrWalker!!.removeObserver()
			
			delay (100)
			//check rbrWalker is reactivated
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverRbrWalker!!.addObserver( channelForObserver,expectedReactivate)
			println ("===============TEST | sending $msgReactivate")
			MsgUtil.sendMsg(msgReactivate, rbrWalkerActor!!)
			
			State = channelForObserver.receive()
			assertEquals(PrevisionReactivate,State)			
			testingObserverRbrWalker!!.removeObserver()

			//check rbrwalker arrives to position (5,5)
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverRbrWalker!!.addObserver( channelForObserver,expected)
			
			State = channelForObserver.receive()
			assertEquals(Prevision,State)
		}
	}	
}