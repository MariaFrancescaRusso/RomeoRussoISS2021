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
				var Dishes = ""
				var Food = ""
				var FoodCode = ""
				var FoodPresence = "" 
				var RHCoordinate : Pair<String,String> ?= null	// X = column; Y = row
				var RHDir = ""
				var CurrentPos = ""
				var Cleared = false
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("RBR | STARTS...")
						solve("consult('IsMap.pl')","") //set resVar	
						solve("getValue(Value)","") //set resVar	
						if( currentSolution.isSuccess() ) { IsMap = "${getCurSol("Value")}".toBoolean()  
						println("RBR | IsMap value $IsMap")
						}
						else
						{println("RBR | Error getting IsMap value...")
						}
						solve("consult('ResourcesCoordinates.pl')","") //set resVar	
						solve("getRHXYCoordinatesAndDir(XRH,YRH,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) { 
										RHCoordinate = Pair("${getCurSol("XRH")}", "${getCurSol("YRH")}") 
										RHDir = "${getCurSol("Dir")}"
						println("RBR | placed in RH position $RHCoordinate")
						}
						else
						{println("RBR | Error getting RH coordinates...")
						}
					}
					 transition( edgeName="goto",targetState="working", cond=doswitchGuarded({ IsMap  
					}) )
					transition( edgeName="goto",targetState="mapping", cond=doswitchGuarded({! ( IsMap  
					) }) )
				}	 
				state("mapping") { //this:State
					action { //it:State
						request("map", "map(0)" ,"rbrmapper" )  
						println("RBR | sent to RBR Mapper the request to map the room...")
					}
					 transition(edgeName="t015",targetState="working",cond=whenReply("mapdone"))
				}	 
				state("working") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("mapdone(ARG)"), Term.createTerm("mapdone(ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("RBR | RBR Mapper ended mapping the room")
								 IsMap = true  
						}else{
							println("RBR | terminating RBR Mapper...")
							forward("end", "end(0)" ,"rbrmapper" ) 
						}
						println("RBR | ready to work...")
					}
					 transition(edgeName="t116",targetState="exPrepareHP",cond=whenDispatch("prepare"))
				}	 
				state("exPrepareHP") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("prepare(X,Y)"), Term.createTerm("prepare(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 	
									 			Dishes = payloadArg(0)
												Food = payloadArg(1)
						}
						println("RBR | executing task 'Prepare the room' ( Crockery = $Dishes; Foods = $Food ) :")
						solve("getPantryFromCurPosXYCoordinateAndDir($RHCoordinate,XPantry,YPantry,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for pantry in (${getCurSol("XPantry")}, ${getCurSol("YPantry")})")
						}
						else
						{println("RBR | Error getting pantry coordinates...")
						}
						println("RBR | going to pantry...")
						request("setGoal", "setGoal(${getCurSol("XPantry")},${getCurSol("YPantry")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t217",targetState="exPreparePT",cond=whenReply("goalState"))
				}	 
				state("exPreparePT") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached pantry. Taking dishes...")
						forward("changeState", "changeState(remove,$Dishes)" ,"pantry" ) 
						delay(300) 
						solve("getTableFromCurPosXYCoordinateAndDir($CurrentPos,XTable,YTable,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for table in (${getCurSol("XTable")}, ${getCurSol("YTable")})")
						}
						else
						{println("RBR | Error getting table coordinates...")
						}
						println("RBR | going to table...")
						request("setGoal", "setGoal(${getCurSol("XTable")},${getCurSol("YTable")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t318",targetState="exPrepareTF",cond=whenReply("goalState"))
				}	 
				state("exPrepareTF") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached table. Adding dishes...")
						forward("changeState", "changeState(add,$Dishes)" ,"table" ) 
						delay(300) 
						solve("getFridgeFromCurPosXYCoordinateAndDir($CurrentPos,XFridge,YFridge,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for fridge in (${getCurSol("XFridge")}, ${getCurSol("YFridge")})")
						}
						else
						{println("RBR | Error getting fridge coordinates...")
						}
						println("RBR | going to fridge...")
						request("setGoal", "setGoal(${getCurSol("XFridge")},${getCurSol("YFridge")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t419",targetState="exPrepareFT",cond=whenReply("goalState"))
				}	 
				state("exPrepareFT") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached fridge. Taking food...")
						forward("changeState", "changeState(remove,$Food)" ,"fridge" ) 
						delay(300) 
						solve("getTableFromCurPosXYCoordinateAndDir($CurrentPos,XTable,YTable,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for table in (${getCurSol("XTable")}, ${getCurSol("YTable")})")
						}
						else
						{println("RBR | Error getting table coordinates...")
						}
						println("RBR | going to table...")
						request("setGoal", "setGoal(${getCurSol("XTable")},${getCurSol("YTable")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t520",targetState="addFoodTable",cond=whenReply("goalState"))
				}	 
				state("addFoodTable") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached table. Adding food...")
						forward("changeState", "changeState(add,$Food)" ,"table" ) 
						delay(300) 
					}
					 transition( edgeName="goto",targetState="goToHome", cond=doswitch() )
				}	 
				state("goToHome") { //this:State
					action { //it:State
						println("RBR | coming back to RH...")
						 request("setGoal", "setGoal(${RHCoordinate!!.first}, ${RHCoordinate!!.second}, $RHDir)", "rbrwalker")  
					}
					 transition(edgeName="t621",targetState="wait",cond=whenReplyGuarded("goalState",{ !Cleared  
					}))
					transition(edgeName="t622",targetState="terminateRbr",cond=whenReplyGuarded("goalState",{ Cleared  
					}))
				}	 
				state("wait") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached RH. Finished executing task")
						println("RBR | waiting for a command...")
					}
					 transition(edgeName="t723",targetState="checkFood",cond=whenRequest("addFood"))
					transition(edgeName="t724",targetState="exClearHT",cond=whenDispatch("clear"))
				}	 
				state("checkFood") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("addFood(FOOD_CODE)"), Term.createTerm("addFood(ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 FoodCode = payloadArg(0).removeSurrounding("[", "]")  
						}
						forward("askFood", "askFood($FoodCode)" ,"fridge" ) 
						println("RBR | asked fridge if it contains the food with food-code = $FoodCode")
					}
					 transition(edgeName="t825",targetState="handleAnswer",cond=whenEvent("observerfridge"))
				}	 
				state("handleAnswer") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("observerfridge(X)"), Term.createTerm("observerfridge(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
									 			var Temp = payloadArg(0).split(";")
									 			FoodPresence = Temp.get(0)
								if(  Temp.size == 2  
								 ){ Food = Temp.get(1)  
								}
						}
					}
					 transition( edgeName="goto",targetState="waitAnswer", cond=doswitchGuarded({ FoodPresence != "true" && FoodPresence != "false"  
					}) )
					transition( edgeName="goto",targetState="checkAnswer", cond=doswitchGuarded({! ( FoodPresence != "true" && FoodPresence != "false"  
					) }) )
				}	 
				state("waitAnswer") { //this:State
					action { //it:State
					}
					 transition(edgeName="t926",targetState="handleAnswer",cond=whenEvent("observerfridge"))
				}	 
				state("checkAnswer") { //this:State
					action { //it:State
						println("RBR | received answer from fridge via CoAP: $FoodPresence")
					}
					 transition( edgeName="goto",targetState="fail", cond=doswitchGuarded({ FoodPresence == "false"  
					}) )
					transition( edgeName="goto",targetState="exAddFoodHF", cond=doswitchGuarded({! ( FoodPresence == "false"  
					) }) )
				}	 
				state("fail") { //this:State
					action { //it:State
						answer("addFood", "warning", "warning(w)"   )  
						println("RBR | send warning to maitre")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("exAddFoodHF") { //this:State
					action { //it:State
						println("RBR | executing task 'Add food' for food $Food with food_code $FoodCode :")
						solve("getFridgeFromCurPosXYCoordinateAndDir($RHCoordinate,XFridge,YFridge,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for fridge in (${getCurSol("XFridge")}, ${getCurSol("YFridge")})")
						}
						else
						{println("RBR | Error getting fridge coordinates...")
						}
						println("RBR | going to fridge...")
						request("setGoal", "setGoal(${getCurSol("XFridge")},${getCurSol("YFridge")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t1027",targetState="exAddFoodFT",cond=whenReply("goalState"))
				}	 
				state("exAddFoodFT") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached fridge. Taking food...")
						forward("changeState", "changeState(remove,$Food)" ,"fridge" ) 
						delay(300) 
						solve("getTableFromCurPosXYCoordinateAndDir($CurrentPos,XTable,YTable,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for table in (${getCurSol("XTable")}, ${getCurSol("YTable")})")
						}
						else
						{println("RBR | Error getting table coordinates...")
						}
						println("RBR | going to table...")
						request("setGoal", "setGoal(${getCurSol("XTable")},${getCurSol("YTable")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t1128",targetState="addFoodTable",cond=whenReply("goalState"))
				}	 
				state("exClearHT") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("clear(X,Y)"), Term.createTerm("clear(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 	
									 			Dishes = payloadArg(0)
												Food = payloadArg(1)
						}
						println("RBR | executing task 'Clear the room':")
						solve("getTableFromCurPosXYCoordinateAndDir($RHCoordinate,XTable,YTable,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for table in (${getCurSol("XTable")}, ${getCurSol("YTable")})")
						}
						else
						{println("RBR | Error getting table coordinates...")
						}
						println("RBR | going to table...")
						request("setGoal", "setGoal(${getCurSol("XTable")},${getCurSol("YTable")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t1229",targetState="exClearTF",cond=whenReplyGuarded("goalState",{ Food != "[]"  
					}))
					transition(edgeName="t1230",targetState="exClearTD",cond=whenReplyGuarded("goalState",{ Food == "[]"  
					}))
				}	 
				state("exClearTF") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached table. Taking food...")
						forward("changeState", "changeState(remove,$Food)" ,"table" ) 
						delay(300) 
						solve("getFridgeFromCurPosXYCoordinateAndDir($CurrentPos,XFridge,YFridge,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for fridge in (${getCurSol("XFridge")}, ${getCurSol("YFridge")})")
						}
						else
						{println("RBR | Error getting fridge coordinates...")
						}
						println("RBR | going to fridge...")
						request("setGoal", "setGoal(${getCurSol("XFridge")},${getCurSol("YFridge")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t1331",targetState="exClearFT",cond=whenReply("goalState"))
				}	 
				state("exClearFT") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached fridge. Adding food...")
						forward("changeState", "changeState(add,$Food)" ,"fridge" ) 
						delay(300) 
						solve("getTableFromCurPosXYCoordinateAndDir($CurrentPos,XTable,YTable,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for table in (${getCurSol("XTable")}, ${getCurSol("YTable")})")
						}
						else
						{println("RBR | Error getting table coordinates...")
						}
						println("RBR | going to table...")
						request("setGoal", "setGoal(${getCurSol("XTable")},${getCurSol("YTable")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t1432",targetState="exClearTD",cond=whenReply("goalState"))
				}	 
				state("exClearTD") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached table. Taking dishes...")
						forward("changeState", "changeState(remove,$Dishes)" ,"table" ) 
						delay(300) 
						solve("getDishwasherFromCurPosXYCoordinateAndDir($CurrentPos,XDishwasher,YDishwasher,Dir)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("RBR | Found nearest goal for dishwasher in (${getCurSol("XDishwasher")}, ${getCurSol("YDishwasher")})")
						}
						else
						{println("RBR | Error getting dishwasher coordinates...")
						}
						println("RBR | going to dishwasher...")
						request("setGoal", "setGoal(${getCurSol("XDishwasher")},${getCurSol("YDishwasher")},${getCurSol("Dir")})" ,"rbrwalker" )  
					}
					 transition(edgeName="t1533",targetState="exClearD",cond=whenReply("goalState"))
				}	 
				state("exClearD") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached dishwasher. Adding dishes...")
						forward("changeState", "changeState(add,$Dishes)" ,"dishwasher" ) 
						delay(300) 
						 Cleared = true  
					}
					 transition( edgeName="goto",targetState="goToHome", cond=doswitch() )
				}	 
				state("terminateRbr") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("goalState(X)"), Term.createTerm("goalState(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentPos = payloadArg(0).removePrefix(",")  
								println("RBR | current position: $CurrentPos")
								updateResourceRep( "$CurrentPos"  
								)
						}
						println("RBR | ...reached RH. Finished executing task")
						println("RBR | terminating RBR Walker...")
						forward("end", "end(0)" ,"rbrwalker" ) 
						println("RBR | terminating...")
						terminate(1)
					}
				}	 
			}
		}
}
