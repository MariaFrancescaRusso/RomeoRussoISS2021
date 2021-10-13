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

class ResourceStateTest{
		
	companion object{
		var myactor : ActorBasic? = null
		var systemStarted         = false
		var testingObserver   : CoapObserverForTest ? = null
		val channelSyncStart      = Channel<String>()
	
		@JvmStatic
		@BeforeClass
		fun systemSetUp() {
			println("===============TEST Init | Running context")
			
			GlobalScope.launch{ 
				it.unibo.ctxsystem.main()
			}			

/*		
			GlobalScope.launch{ 
				it.unibo.ctxfridge.main()
			}
*/
/*			
			GlobalScope.launch{ 
				it.unibo.ctxrbr.main()
			}
*/
/*		
			GlobalScope.launch{ 
				it.unibo.ctxmaitre.main()
			}
*/			
			println("===============TEST Init | Activating Observers")
			
			GlobalScope.launch{
				myactor=QakContext.getActor("fridge")
				while(  myactor == null ){
					delay(500)
					myactor=QakContext.getActor("fridge")
				}
			channelSyncStart.send("starttesting")
			}
		}
			
		@JvmStatic
	    @AfterClass
		fun terminate() {
			println("===============TEST | terminate the testing")
		}				
	}
	
	@Before
	fun checkSystemStarted()  {
		var ip = "localhost"
//		var ip = "127.0.0.1" 
		var ctx = "ctxsystem"
//		var ctx = "ctxfridge"
		var actname = "fridge"
		var port = "8040"
//		var port = "8060"
	    println("testingObserver=$testingObserver")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
				println("===============TEST | checkSystemStarted resumed ")
				
			}			
		} 
		if( testingObserver == null) testingObserver = CoapObserverForTest("testingObserver","$ip", "$ctx", "$actname", "$port")
  	}

	@After
	fun removeObs(){
		println("${testingObserver!!.name}")
		testingObserver!!.terminate()
		testingObserver = null
	}
    
	@Test
	fun prepareTest(){
		//var	PrepareDish = arrayListOf(arrayListOf("dishes", "10"), arrayListOf("glasses", "10")) 
		var	Food = arrayListOf(arrayListOf("s001", "bread", "10"))
		var fridgePrevision = "Remove Food [[s001,bread,10]] with success!"
//		var fridgePrevision = "Remove Food $Food with success!"
		var msg= MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Food)", "fridge")
		var fridgeState = ""
		var expected = "Remove "
		val channelForObserver = Channel<String>()		
		testingObserver!!.addObserver( channelForObserver,expected )
		runBlocking{
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, myactor!!)
			fridgeState = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$fridgeState for $msg")
			assertEquals(fridgePrevision,fridgeState)
		}
	}
}
//FIXME il risultato in Food viene pulito dagli spazi; se si pulisse dagli spazi anche PrepareFood il test riuscirebbe
//Remove Food [[s001,[ bread, 10], [d001, water, 10], [p003, pasta, 10], [s002, sandwich, 20], [d005, wine, 5], [k007, muffin, 20], [s005, salad, ]10]] with success!>
//Remove Food [[s001,[bread,10],[d001,water,10],[p003,pasta,10],[s002,sandwich,20],[d005,wine,5],[k007,muffin,20],[s005,salad,]10]]
	