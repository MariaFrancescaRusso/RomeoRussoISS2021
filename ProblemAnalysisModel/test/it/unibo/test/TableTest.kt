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

class TableTest {
		
	companion object {
		var tableActor : ActorBasic? = null
		var systemStarted         = false
		var testingObserverTable   : CoapObserverForTest ? = null
		val channelSyncStart      = Channel<String>()
	
		@JvmStatic
		@BeforeClass
		fun systemSetUp() {
			println("===============TEST Init | Running context ")

			GlobalScope.launch{ 
				it.unibo.ctxsystem.main()
			}			
/*		
			GlobalScope.launch{ 
				it.unibo.ctxTable.main()
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

			GlobalScope.launch {
				tableActor = QakContext.getActor("Table")
				while(  tableActor == null ) {
					delay(500)
					tableActor = QakContext.getActor("Table")
				}
				channelSyncStart.send("starttesting1")
			}
		}
			
		@JvmStatic
	    @AfterClass
		fun terminate() {
			println("${testingObserverTable!!.name}")
			testingObserverTable!!.terminate()
			testingObserverTable = null
						
			println("===============TEST | terminate the testing")				
		}
	}
	
	@Before
	fun checkSystemStarted()  {
		var ip = "localhost"
//		var ip = "127.0.0.1"
		var ctx = "ctxsystem"
//		var ctx = "ctxtable"
		var actname = "table"
		var port = "8040"
//		var port = "8060"
		
		if( ! systemStarted ) {
			runBlocking {
				channelSyncStart.receive()
				systemStarted = true
				println("===============TEST | checkSystemStarted resumed")
			}
		}
		
		if( testingObserverTable == null) testingObserverTable = CoapObserverForTest("testingObserverTable","$ip", "$ctx", "$actname", "$port")
		println("testingObserverTable=$testingObserverTable")
  	}

		
	@Test
	fun AddFoodTableTest(){
		var	Food = arrayListOf(arrayListOf("s034", "cheddar", "10"))
		var TablePrevision = "Add [[s034,cheddar,10]] with success!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(add, $Food)", "table")
		var TableState = ""
		var expected = TablePrevision
		val channelForObserver = Channel<String>()		
		
		testingObserverTable!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, tableActor!!)
			TableState = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$TableState for $msg")
			assertEquals(TablePrevision,TableState)
		}
	}
	
	@Test
	fun RemoveFoodTableTest() {
		var	FoodAdd = arrayListOf(arrayListOf("s035", "cocacola", "1"))
		var	FoodRemove = arrayListOf(arrayListOf("s035", "cocacola", "1"))
		var TablePrevision = "Remove [[s035,cocacola,1]] with success!"
		var msgAdd = MsgUtil.buildDispatch("tester", "changeState", "changeState(add, $FoodAdd)", "table")
		var msgRemove = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $FoodRemove)", "table")
		var TableState = ""
		var expected = TablePrevision
		val channelForObserver = Channel<String>()		
		
		testingObserverTable!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			println("===============TEST | sending $msgAdd")
			MsgUtil.sendMsg(msgAdd, tableActor!!)
			println("===============TEST | sending $msgRemove")
			MsgUtil.sendMsg(msgRemove, tableActor!!)
			TableState = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$TableState for $msgRemove")
			assertEquals(TablePrevision,TableState)
		}
	}
	
	@Test
	fun RemoveFoodTableFailTest() {
		var	Food = arrayListOf(arrayListOf("x999", "mysteryfood", "1"))
		var Prevision = "Fail removing [[x999,mysteryfood,1]]!"
		var msg = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Food)", "table")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverTable!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, tableActor!!)
			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun RemoveFoodTableFailQuantityTest() {
		var	FoodAdd = arrayListOf(arrayListOf("s099", "sprite", "1"))
		var	FoodRemove = arrayListOf(arrayListOf("s099", "sprite", "2"))
		var Prevision = "Fail removing [[s099,sprite,2]]!"
		var msgAdd = MsgUtil.buildDispatch("tester", "changeState", "changeState(add, $FoodAdd)", "table")
		var msgRemove = MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $FoodRemove)", "table")
		var State = ""
		var expected = Prevision
		val channelForObserver = Channel<String>()		
		
		testingObserverTable!!.addObserver( channelForObserver,expected )
		
		runBlocking {
			println("===============TEST | sending $msgAdd")
			MsgUtil.sendMsg(msgAdd, tableActor!!)
			println("===============TEST | sending $msgRemove")
			MsgUtil.sendMsg(msgRemove, tableActor!!)

			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msgRemove")
			assertEquals(Prevision,State)
		}
	}
 }