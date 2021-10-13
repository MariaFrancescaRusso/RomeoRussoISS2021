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
		var fridgeActor : ActorBasic? = null
		var pantryActor : ActorBasic? = null
		var systemStarted         = false
<<<<<<< HEAD:ProblemAnalysisModel/test/test/ResourceStateTest.kt
		var testingObserverFridge   : CoapObserverForTest ? = null
		var testingObserverPantry 	: CoapObserverForTest ? = null
=======
		var testingObserver   : CoapObserverForTest ? = null
>>>>>>> 760d94d916da89b23a630b5d80c632938791f0db:ProblemAnalysisModel/test/it/unibo/test/ResourceStateTest.kt
		val channelSyncStart      = Channel<String>()
	
		@JvmStatic
		@BeforeClass
		fun systemSetUp() {
<<<<<<< HEAD:ProblemAnalysisModel/test/test/ResourceStateTest.kt
			println("===============TEST Init | Running context ")

			GlobalScope.launch{ 
				it.unibo.ctxfridge.main()
			}

			GlobalScope.launch{ 
				it.unibo.ctxmaitre.main()
			}	
/*					
			GlobalScope.launch{ 
				it.unibo.ctxrbr.main()
			}
*/									
			println("===============TEST Init | Activating Observers")

			GlobalScope.launch{
				fridgeActor=QakContext.getActor("fridge")
				while(  fridgeActor== null ){
					delay(500)
					fridgeActor=QakContext.getActor("fridge")
				}
				channelSyncStart.send("starttesting1")
			}
			
			GlobalScope.launch{
				pantryActor=QakContext.getActor("pantry")
				while(  pantryActor== null ){
					delay(500)
					pantryActor=QakContext.getActor("pantry")
=======
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
>>>>>>> 760d94d916da89b23a630b5d80c632938791f0db:ProblemAnalysisModel/test/it/unibo/test/ResourceStateTest.kt
				}
				channelSyncStart.send("starttesting2")
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
<<<<<<< HEAD:ProblemAnalysisModel/test/test/ResourceStateTest.kt
		var ip1 = "127.0.0.1"
		var ip2 = "192.168.1.211"
		var ctx1 = "ctxfridge"
		var ctx2 = "ctxmaitre"
		var actname1 = "fridge"
		var actname2 = "pantry"
		var port1 = "8060"
		var port2 = "8070"
=======
		var ip = "localhost"
//		var ip = "127.0.0.1" 
		var ctx = "ctxsystem"
//		var ctx = "ctxfridge"
		var actname = "fridge"
		var port = "8040"
//		var port = "8060"
	    println("testingObserver=$testingObserver")
>>>>>>> 760d94d916da89b23a630b5d80c632938791f0db:ProblemAnalysisModel/test/it/unibo/test/ResourceStateTest.kt
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				channelSyncStart.receive()
				systemStarted = true
				println("===============TEST | checkSystemStarted resumed ")
				
<<<<<<< HEAD:ProblemAnalysisModel/test/test/ResourceStateTest.kt
			}
		}
		if( testingObserverFridge == null) testingObserverFridge = CoapObserverForTest("testingObserverFridge","$ip1", "$ctx1", "$actname1", "$port1")
		if( testingObserverPantry == null) testingObserverPantry= CoapObserverForTest("testingObserverPantry","$ip2", "$ctx2", "$actname2", "$port2")
	    println("testingObserverFridge=$testingObserverFridge")
		println("testingObserverPantry=$testingObserverPantry")
=======
			}			
		} 
		if( testingObserver == null) testingObserver = CoapObserverForTest("testingObserver","$ip", "$ctx", "$actname", "$port")
>>>>>>> 760d94d916da89b23a630b5d80c632938791f0db:ProblemAnalysisModel/test/it/unibo/test/ResourceStateTest.kt
  	}

/*
	@After
	fun removeObsFridge(){
		println("${testingObserverFridge!!.name}")
		testingObserverFridge!!.terminate()
		testingObserverFridge = null
	}

	@After
	fun removeObsPantry(){
		println("${testingObserverPantry!!.name}")
		testingObserverPantry!!.terminate()
		testingObserverPantry = null
	}
*/  
	@Test
	fun AddDishPantryTest(){
		var	Dishes = arrayListOf(arrayListOf("dishes", "10"))
		var Prevision = "Add Crockery [[dishes,10]] with success!"
		var msg= MsgUtil.buildDispatch("tester", "changeState", "changeState(add, $Dishes)", "pantry")
		var State = ""
		var expected = "Add "
		val channelForObserver = Channel<String>()		
		testingObserverPantry!!.addObserver( channelForObserver,expected )
		runBlocking{
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, pantryActor!!)
			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun RemoveDishPantryTest(){
		var	Dishes = arrayListOf(arrayListOf("dishes", "10"))
		var Prevision = "Remove Crockery [[dishes,10]] with success!"
		var msg= MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Dishes)", "pantry")
		var State = ""
		var expected = "Remove "
		val channelForObserver = Channel<String>()		
		testingObserverPantry!!.addObserver( channelForObserver,expected )
		runBlocking{
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, pantryActor!!)
			State = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$State for $msg")
			assertEquals(Prevision,State)
		}
	}
	
	@Test
	fun AddFoodFridgeTest(){
		var	Food = arrayListOf(arrayListOf("s034", "cheddar", "10"))
		var fridgePrevision = "Add Food [[s034,cheddar,10]] with success!"
		var msg= MsgUtil.buildDispatch("tester", "changeState", "changeState(add, $Food)", "fridge")
		var fridgeState = ""
		var expected = "Add "
		val channelForObserver = Channel<String>()		
		testingObserverFridge!!.addObserver( channelForObserver,expected )
		runBlocking{
			println("===============TEST | sending $msg")
			MsgUtil.sendMsg(msg, fridgeActor!!)
			fridgeState = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$fridgeState for $msg")
			assertEquals(fridgePrevision,fridgeState)
		}
	}
	
	@Test
<<<<<<< HEAD:ProblemAnalysisModel/test/test/ResourceStateTest.kt
	fun RemoveFoodFridTest(){
=======
	fun prepareTest(){
>>>>>>> 760d94d916da89b23a630b5d80c632938791f0db:ProblemAnalysisModel/test/it/unibo/test/ResourceStateTest.kt
		//var	PrepareDish = arrayListOf(arrayListOf("dishes", "10"), arrayListOf("glasses", "10")) 
		var	Food = arrayListOf(arrayListOf("s001", "bread", "10"))
		var fridgePrevision = "Remove Food [[s001,bread,10]] with success!"
//		var fridgePrevision = "Remove Food $Food with success!"
		var msg= MsgUtil.buildDispatch("tester", "changeState", "changeState(remove, $Food)", "fridge")
		var fridgeState = ""
		var expected = "Remove "
		val channelForObserver = Channel<String>()		
		testingObserverFridge!!.addObserver( channelForObserver,expected )
		runBlocking{
			println("===============TEST | sending $msg")
<<<<<<< HEAD:ProblemAnalysisModel/test/test/ResourceStateTest.kt
			MsgUtil.sendMsg(msg, fridgeActor!!)
=======
			MsgUtil.sendMsg(msg, myactor!!)
>>>>>>> 760d94d916da89b23a630b5d80c632938791f0db:ProblemAnalysisModel/test/it/unibo/test/ResourceStateTest.kt
			fridgeState = channelForObserver.receive()			
			
			println("===============TEST | RESULT=$fridgeState for $msg")
			assertEquals(fridgePrevision,fridgeState)
		}
	}
<<<<<<< HEAD:ProblemAnalysisModel/test/test/ResourceStateTest.kt
 }
=======
}
//FIXME il risultato in Food viene pulito dagli spazi; se si pulisse dagli spazi anche PrepareFood il test riuscirebbe
//Remove Food [[s001,[ bread, 10], [d001, water, 10], [p003, pasta, 10], [s002, sandwich, 20], [d005, wine, 5], [k007, muffin, 20], [s005, salad, ]10]] with success!>
//Remove Food [[s001,[bread,10],[d001,water,10],[p003,pasta,10],[s002,sandwich,20],[d005,wine,5],[k007,muffin,20],[s005,salad,]10]]
	
>>>>>>> 760d94d916da89b23a630b5d80c632938791f0db:ProblemAnalysisModel/test/it/unibo/test/ResourceStateTest.kt
