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

class AvoidMobileObstacleTest {
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
	fun AvoidMobileObstacleTest() {

		var msg = MsgUtil.buildRequest("tester", "setGoal", "setGoal(0,5, downDir)", "rbrwalker")
		var State = ""
		var expected = "(0, 5)"
		var expectedObstacle= "Obstacle Detected"
		var Prevision = expected
		var PrevisionObstacle= expectedObstacle
		var channelForObserver = Channel<String>()
		
		runBlocking {
			testingObserverRbrWalker!!.addObserver( channelForObserver,expectedObstacle)

			//send setGoal request
			println ("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, rbrWalkerActor!!)
			delay(100)

			//check rbrWalker is stopped
			State = channelForObserver.receive()
			assertEquals(PrevisionObstacle,State)			
			testingObserverRbrWalker!!.removeObserver()
			
			//check rbrwalker arrives to position (0,6)
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserverRbrWalker!!.addObserver( channelForObserver,expected)
			
			State = channelForObserver.receive()
			assertEquals(Prevision,State)
		}
	}
}