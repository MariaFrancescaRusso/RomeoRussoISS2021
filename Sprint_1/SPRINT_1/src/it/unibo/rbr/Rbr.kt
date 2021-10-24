/* Generated by AN DISI Unibo */ 
package it.unibo.rbr

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Rbr ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 	
				var IsMap = false
				var PrepareDish = ""
				var PrepareFood = ""
				var FoodCode = ""
				var FoodPresence = false 
				var Food = ""
				var ClearDish = ""
				var ClearFood = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("RBR | STARTS and it's placed in RH position...")
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.plannerUtil.loadRoomMap( "roomMap"  )
						 IsMap = true  
						itunibo.planner.plannerUtil.showMap(  )
					}
					 transition( edgeName="goto",targetState="working", cond=doswitchGuarded({ IsMap  
					}) )
					transition( edgeName="goto",targetState="mapping", cond=doswitchGuarded({! ( IsMap  
					) }) )
				}	 
				state("mapping") { //this:State
					action { //it:State
						println("RBR | initializing planner...")
						println("RBR | start mapping room...")
						delay(300) 
						 IsMap = true  
						println("RBR | end mapping step")
					}
					 transition( edgeName="goto",targetState="working", cond=doswitch() )
				}	 
				state("working") { //this:State
					action { //it:State
						println("RBR | STARTS and it's ready to work")
					}
					 transition(edgeName="t07",targetState="exPrepare",cond=whenDispatch("prepare"))
				}	 
				state("exPrepare") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("prepare(X,Y)"), Term.createTerm("prepare(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 	
									 			PrepareDish = payloadArg(0)
												PrepareFood = payloadArg(1)
						}
						println("RBR | executing task 'Prepare the room' ( Crockery = $PrepareDish; Foods = $PrepareFood ) :")
						println("RBR | going to pantry...")
						delay(300) 
						 
									var ac = "empty"
									itunibo.planner.plannerUtil.planForGoal("0","6")
						//			itunibo.planner.plannerUtil.getPosX()!=2 || itunibo.planner.plannerUtil.getPosY()!=2 
									ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									while(ac!=""){
										itunibo.planner.plannerUtil.updateMap(ac) 
										println ("RBR | $ac")
										ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									}
									itunibo.planner.plannerUtil.showMap()
						println("RBR | ...reached pantry. Taking dishes...")
						forward("changeState", "changeState(remove,$PrepareDish)" ,"pantry" ) 
						delay(300) 
						println("RBR | going to table...")
						delay(300) 
						 
									ac = "empty"
									itunibo.planner.plannerUtil.planForGoal("2","4")
						//			itunibo.planner.plannerUtil.getPosX()!=2 || itunibo.planner.plannerUtil.getPosY()!=2 
									ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									while(ac!=""){
										itunibo.planner.plannerUtil.updateMap(ac) 
										println ("RBR | $ac")
										ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									}
						println("RBR | ...reached table. Adding dishes...")
						forward("changeState", "changeState(add,$PrepareDish)" ,"table" ) 
						delay(300) 
						println("RBR | going to fridge...")
						delay(300) 
						 
									ac = "empty"
									itunibo.planner.plannerUtil.planForGoal("6","0")
						//			itunibo.planner.plannerUtil.getPosX()!=2 || itunibo.planner.plannerUtil.getPosY()!=2 
									ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									while(ac!=""){
										itunibo.planner.plannerUtil.updateMap(ac) 
										println ("RBR | $ac")
										ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									}
									itunibo.planner.plannerUtil.showMap()
						println("RBR | ...reached fridge. Taking food...")
						forward("changeState", "changeState(remove,$PrepareFood)" ,"fridge" ) 
						delay(300) 
						println("RBR | going to table...")
						delay(300) 
						 
									ac = "empty"
									itunibo.planner.plannerUtil.planForGoal("4","2")
						//			itunibo.planner.plannerUtil.getPosX()!=2 || itunibo.planner.plannerUtil.getPosY()!=2 
									ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									while(ac!=""){
										itunibo.planner.plannerUtil.updateMap(ac) 
										println ("RBR | $ac")
										ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									}
									itunibo.planner.plannerUtil.showMap()
						println("RBR | ...reached table. Adding food...")
						forward("changeState", "changeState(add,$PrepareFood)" ,"table" ) 
						delay(300) 
						println("RBR | coming back to RH...")
						delay(300) 
						 
									ac = "empty"
									itunibo.planner.plannerUtil.planForGoal("0","0")
						//			itunibo.planner.plannerUtil.getPosX()!=2 || itunibo.planner.plannerUtil.getPosY()!=2 
									ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									while(ac!=""){
										itunibo.planner.plannerUtil.updateMap(ac) 
										println ("RBR | $ac")
										ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									}
									itunibo.planner.plannerUtil.showMap()
						println("RBR | ...reached RH. Finished executing task")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("RBR | is placed in RH position and it's waiting for a command...")
					}
					 transition(edgeName="t18",targetState="checkFood",cond=whenRequest("addFood"))
					transition(edgeName="t19",targetState="exClear",cond=whenDispatch("clear"))
				}	 
				state("checkFood") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("addFood(FOODE_CODE)"), Term.createTerm("addFood(ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 FoodCode = payloadArg(0).removeSurrounding("[", "]")  
						}
						forward("askFood", "askFood($FoodCode)" ,"fridge" ) 
						println("RBR | asked fridge if it contains the food with food-code = $FoodCode")
					}
					 transition(edgeName="t210",targetState="handleAnswer",cond=whenEvent("observerfridge"))
				}	 
				state("handleAnswer") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("observerfridge(X)"), Term.createTerm("observerfridge(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
									 			var Temp = payloadArg(0).split(";")
									 			FoodPresence = Temp.get(0).toBoolean()
								if(  Temp.size == 2  
								 ){ Food = Temp.get(1)  
								}
						}
						println("RBR | received answer from fridge via CoAP: $FoodPresence")
					}
					 transition( edgeName="goto",targetState="fail", cond=doswitchGuarded({ FoodPresence == false  
					}) )
					transition( edgeName="goto",targetState="exAddFood", cond=doswitchGuarded({! ( FoodPresence == false  
					) }) )
				}	 
				state("fail") { //this:State
					action { //it:State
						answer("addFood", "warning", "warning(w)"   )  
						println("RBR | send warning to maitre")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("exAddFood") { //this:State
					action { //it:State
						println("RBR | executing task 'Add food' for food $Food with food_code $FoodCode :")
						println("RBR | going to fridge...")
						delay(300) 
						println("RBR | ...reached fridge. Taking food...")
						forward("changeState", "changeState(remove,$Food)" ,"fridge" ) 
						delay(300) 
						println("RBR | going to table...")
						delay(300) 
						println("RBR | ...reached table. Adding food...")
						forward("changeState", "changeState(add,$Food)" ,"table" ) 
						delay(300) 
						println("RBR | coming back to RH...")
						delay(300) 
						println("RBR | ...reached RH. Finished executing task")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("exClear") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("clear(X,Y)"), Term.createTerm("clear(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 	
									 			ClearDish = payloadArg(0)
												ClearFood = payloadArg(1)
						}
						println("RBR | executing task 'Clear the room':")
						println("RBR | going to table...")
						delay(300) 
						println("RBR | ...reached table. Taking food...")
						forward("changeState", "changeState(remove,$ClearFood)" ,"table" ) 
						delay(300) 
						println("RBR | going to fridge...")
						delay(300) 
						println("RBR | ...reached fridge. Adding food...")
						forward("changeState", "changeState(add,$ClearFood)" ,"fridge" ) 
						delay(300) 
						println("RBR | going to table...")
						delay(300) 
						println("RBR | ...reached table. Taking dishes...")
						forward("changeState", "changeState(remove,$ClearDish)" ,"table" ) 
						delay(300) 
						println("RBR | going to dishwasher...")
						delay(300) 
						println("RBR | ...reached dishwasher. Adding dishes...")
						forward("changeState", "changeState(add,$ClearDish)" ,"dishwasher" ) 
						delay(300) 
						println("RBR | coming back to RH...")
						delay(300) 
						println("RBR | ...reached RH. Finished executing task")
					}
				}	 
			}
		}
}
