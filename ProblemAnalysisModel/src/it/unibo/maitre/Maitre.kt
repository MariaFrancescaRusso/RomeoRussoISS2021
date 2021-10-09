/* Generated by AN DISI Unibo */ 
package it.unibo.maitre

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Maitre ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
			
				var AddFoodtime = 3000L 
				var Nexp = 0
				var PrepareDish = emptyList<ArrayList<String>>()
				var PrepareFood = emptyList<ArrayList<String>>()
				var FoodCode = ""
				var AnsExpose1 = ""
				var AnsExpose2 = ""
				var ClearDish = ""
				var ClearFood = ""
				
				val FridgeObserver = util.ActorCoapObserver("localhost",8040,"ctxsystem","fridge")
				val TableObserver = util.ActorCoapObserver("localhost",8040,"ctxsystem","table")
				val PantryObserver = util.ActorCoapObserver("localhost",8040,"ctxsystem","pantry")
				val DishwasherObserver = util.ActorCoapObserver("localhost",8040,"ctxsystem","dishwasher")
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("MAITRE | STARTS...")
						 	
									PrepareDish = arrayListOf(arrayListOf("dishes", "10"), arrayListOf("glasses", "10")) 
									PrepareFood = arrayListOf(arrayListOf("s001", "bread", "10"), arrayListOf("d001", "water", "10"), 
										arrayListOf("p003", "pasta", "10"), arrayListOf("s002", "sandwich", "20"), arrayListOf("d005", "wine", "5"),
										arrayListOf("k007", "muffin", "20"), arrayListOf("s005", "salad", "10"))
						
						//			FoodCode = "p003"	// existing food_code that isn't available
									FoodCode = "c034"	// not existing food_code
						//			FoodCode = "s001"	//existing and available food_code
						
									FridgeObserver.activate()
									TableObserver.activate()
									PantryObserver.activate()
									DishwasherObserver.activate()
						delay(2000) 
					}
					 transition( edgeName="goto",targetState="sendPrepare", cond=doswitch() )
				}	 
				state("sendPrepare") { //this:State
					action { //it:State
						forward("prepare", "prepare($PrepareDish,$PrepareFood)" ,"rbr" ) 
						println("MAITRE | send prepare command to RBR: $PrepareDish, $PrepareFood")
						delay(2000) 
					}
					 transition( edgeName="goto",targetState="sendAddFood", cond=doswitch() )
				}	 
				state("sendAddFood") { //this:State
					action { //it:State
						request("addFood", "addFood($FoodCode)" ,"rbr" )  
						println("MAITRE | send addFood($FoodCode) command to RBR")
						stateTimer = TimerActor("timer_sendAddFood", 
							scope, context!!, "local_tout_maitre_sendAddFood", AddFoodtime )
					}
					 transition(edgeName="t14",targetState="sendConsult",cond=whenTimeout("local_tout_maitre_sendAddFood"))   
					transition(edgeName="t15",targetState="handleWarning",cond=whenReply("warning"))
				}	 
				state("handleWarning") { //this:State
					action { //it:State
						println("MAITRE | received warning from RBR")
					}
					 transition( edgeName="goto",targetState="sendConsult", cond=doswitch() )
				}	 
				state("sendConsult") { //this:State
					action { //it:State
						forward("consult", "consult(0)" ,"fridge" ) 
						println("MAITRE | send consult command to Fridge")
						forward("consult", "consult(0)" ,"dishwasher" ) 
						println("MAITRE | send consult command to Dishwasher")
						forward("consult", "consult(0)" ,"pantry" ) 
						println("MAITRE | send consult command to Pantry")
						forward("consult", "consult(0)" ,"table" ) 
						println("MAITRE | send consult command to Table")
					}
					 transition( edgeName="goto",targetState="waitExpose", cond=doswitch() )
				}	 
				state("waitExpose") { //this:State
					action { //it:State
						println("MAITRE | waiting answers from resources...")
					}
					 transition(edgeName="t26",targetState="handleExpose",cond=whenEvent("observerdishwasher"))
					transition(edgeName="t27",targetState="handleExpose",cond=whenEvent("observerfridge"))
					transition(edgeName="t28",targetState="handleExpose",cond=whenEvent("observerpantry"))
					transition(edgeName="t29",targetState="handleExpose",cond=whenEvent("observertable"))
				}	 
				state("handleExpose") { //this:State
					action { //it:State
						  
									var Sender = currentMsg.msgSender()
									Nexp++ 
						if( checkMsgContent( Term.createTerm("observerdishwasher(X)"), Term.createTerm("observerdishwasher(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 AnsExpose1 = payloadArg(0)  
						}
						if( checkMsgContent( Term.createTerm("observerfridge(X)"), Term.createTerm("observerfridge(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 AnsExpose1 = payloadArg(0)  
						}
						if( checkMsgContent( Term.createTerm("observerpantry(X)"), Term.createTerm("observerpantry(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 AnsExpose1 = payloadArg(0)  
						}
						if( checkMsgContent( Term.createTerm("observertable(X)"), Term.createTerm("observertable(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
									 			var Temp = payloadArg(0).split(";")
									 			AnsExpose1 = Temp.get(0)
									 			AnsExpose2 = Temp.get(1)
						}
						if(  Sender == "observertable"  
						 ){println("MAITRE | status of $Sender: $AnsExpose1 $AnsExpose2")
						}
						else
						 {println("MAITRE | status of $Sender: $AnsExpose1")
						 }
					}
					 transition( edgeName="goto",targetState="preSendClear", cond=doswitchGuarded({	Nexp == 4  
					}) )
					transition( edgeName="goto",targetState="waitExpose", cond=doswitchGuarded({! (	Nexp == 4  
					) }) )
				}	 
				state("preSendClear") { //this:State
					action { //it:State
						forward("consult", "consult(0)" ,"table" ) 
						println("MAITRE | send consult command to Table for 'Clear the room' task")
					}
					 transition(edgeName="t310",targetState="sendClear",cond=whenEvent("observertable"))
				}	 
				state("sendClear") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("observertable(X)"), Term.createTerm("observertable(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
									 			var Temp = payloadArg(0).split(";")
									 			ClearDish = Temp.get(0)
									 			ClearFood = Temp.get(1)
						}
						println("MAITRE | status of Table: Crockery = $ClearDish and Food = $ClearFood")
						forward("clear", "clear($ClearDish,$ClearFood)" ,"rbr" ) 
					}
				}	 
			}
		}
}
