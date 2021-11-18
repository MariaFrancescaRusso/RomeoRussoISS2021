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

class GoalTest {
		
	companion object {
		var Actor : ActorBasic? = null
		var systemStarted = false
		var testingObserver: CoapObserverForTest ? = null
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
			
			println ("TEST Init | Activating Observers")

			GlobalScope.launch {
				Actor = QakContext.getActor("rbrwalker")
				while(  Actor == null ) {
					delay(500)
					Actor = QakContext.getActor("rbrwalker")
				}
				if( testingObserver== null) testingObserver= CoapObserverForTest("testingObserver","$ip", "$ctx", "$actname", "$port")
				println ("testingObserver=$testingObserver")
				delay(500)
				channelSyncStart.send("starttesting")
			}
		}
			
		@JvmStatic
	    @AfterClass
		fun terminate() {
			println ("TEST | terminate the testing")				
		}
	}
	
	@Before
	fun checkSystemStarted() {
		
		if( ! systemStarted ) {
			runBlocking {
				channelSyncStart.receive()
				systemStarted = true
				println ("TEST | checkSystemStarted resumed")
			}
		}
  	}	
	
	@After
	fun removeObs() {
		println ("TEST | AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserver!!.name}")
		testingObserver!!.terminate()
	}

	@Test
	fun GoToGoalTest() {
		var Prevision = "(5, 6)"
		var Prevision2 = "(0, 0)"
		var msg = MsgUtil.buildRequest("tester", "setGoal", "setGoal(5,6,downDir)", "rbrwalker")
		var msg2 = MsgUtil.buildRequest("tester", "setGoal", "setGoal(0,0,downDir)", "rbrwalker")
		var State = ""
		var expected = Prevision
		var expected2 = Prevision2
		var channelForObserver = Channel<String>()		

		runBlocking {
			//check rbrwalker arrives to the dishwasher
			testingObserver!!.addObserver( channelForObserver,expected )
			println ("TEST | sending $msg")
			MsgUtil.sendMsg(msg, Actor!!)
			State = channelForObserver.receive()			
			
			println ("TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
			
			//check rbrwalker arrives to the RH
			channelForObserver.cancel()
			channelForObserver = Channel<String>()
			testingObserver!!.addObserver( channelForObserver,expected2)
			println ("TEST | sending $msg2")
			MsgUtil.sendMsg(msg2, Actor!!)
			State = channelForObserver.receive()			
			
			println ("TEST | RESULT=$State for $msg2")
			assertEquals(Prevision2,State)
		}
		channelForObserver.close()
	}
 }