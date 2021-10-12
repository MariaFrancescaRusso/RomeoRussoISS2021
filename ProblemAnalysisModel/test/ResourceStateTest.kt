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
		var systemStarted         = false
		var testingObserver   : CoapObserverForTesting ? = null
		val channelSyncStart      = Channel<String>()
	
	
		@JvmStatic
		@BeforeClass
		fun systemSetUp() {
			println("===============TEST Init | Running context ")
			GlobalScope.launch{ 
				it.unibo.ctxsystem.main()
			}
			println("===============TEST Init | Activating Observers")
			GlobalScope.launch{
				myactor=QakContext.getActor("rbr")
				while(  myactor == null ){
					delay(500)
					myactor=QakContext.getActor("rbr")
				}
			channelSyncStart.send("starttesting")
			}
		}
			
	    @After
		fun terminate() {
			println("===============TEST | terminate the testing")
		}				
	}
	
	@Before
	fun checkSystemStarted()  {
	    println("testingObserver=$testingObserver")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
				println("===============TEST | checkSystemStarted resumed ")
				
			}			
		} 
		if( testingObserver == null) testingObserver = CoapObserverForTesting("testingObserver","localhost", "ctxres", "pantry", "8080")
  	}

	@After
	fun removeObs(){
		println("${testingObserver!!.name}")
		testingObserver!!.terminate()
		testingObserver = null
	}
    
	@Test
	fun prepareTest(){
		var prepare= MsgUtil.buildDispatch("tester", "prepare", "prepare(0)", "rbr")
		var fridgeState = ""
		var fridgePrevision = "State:c"
		var expected = "State"
		val channelForObserver = Channel<String>()		
		testingObserver!!.addObserver( channelForObserver,expected )
		runBlocking{
			println("===============TEST | sending $prepare")
			MsgUtil.sendMsg(prepare, myactor!!)
			fridgeState = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$fridgeState for $prepare")
			assertEquals(fridgePrevision,fridgeState)
		}
	}
}