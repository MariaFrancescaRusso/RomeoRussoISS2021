/* Generated by AN DISI Unibo */ 
package it.unibo.mapper

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Mapper ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var CurrMov = "empty" 
				var CurrEdge =0	
				var NameFile = "roomMap"
				var Table = false
				var Step = 290 //647
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("MAPPER | STARTS...")
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.plannerUtil.showMap(  )
						delay(2000) 
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("MAPPER | waits...")
					}
					 transition(edgeName="t10",targetState="findNextWall",cond=whenRequest("map"))
					transition(edgeName="t11",targetState="endState",cond=whenDispatch("endmapper"))
				}	 
				state("findNextWall") { //this:State
					action { //it:State
						request("step", "step($Step)" ,"basicrobot" )  
						delay(1000) 
					}
					 transition(edgeName="t22",targetState="succesStep",cond=whenReply("stepdone"))
					transition(edgeName="t23",targetState="obstacleFound",cond=whenReply("stepfail"))
				}	 
				state("succesStep") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.updateMap( "w"  )
						itunibo.planner.plannerUtil.showMap(  )
					}
					 transition( edgeName="goto",targetState="findNextWall", cond=doswitchGuarded({ Table == false  
					}) )
					transition( edgeName="goto",targetState="visitInternalCell", cond=doswitchGuarded({! ( Table == false  
					) }) )
				}	 
				state("obstacleFound") { //this:State
					action { //it:State
						 var D = ""
									var C = ""
						if( checkMsgContent( Term.createTerm("stepfail(DURATION,CAUSE)"), Term.createTerm("stepfail(D,C)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 	D = payloadArg(0)
										 	C = payloadArg(1) 	
								request("step", "step($D)" ,"basicrobot" )  
						}
						delay(1000) 
					}
					 transition(edgeName="t34",targetState="turnLeft",cond=whenReplyGuarded("stepdone",{ Table == false  
					}))
					transition(edgeName="t35",targetState="tableFound",cond=whenReplyGuarded("stepdone",{ Table == true  
					}))
					transition(edgeName="t36",targetState="obstacleFound",cond=whenReply("stepfail"))
				}	 
				state("turnLeft") { //this:State
					action { //it:State
						println("WALKER | Found a wall")
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
						itunibo.planner.plannerUtil.updateMap( "l"  )
						delay(1000) 
						 CurrEdge++  
					}
					 transition( edgeName="goto",targetState="findNextWall", cond=doswitchGuarded({ CurrEdge<4  
					}) )
					transition( edgeName="goto",targetState="saveMap", cond=doswitchGuarded({! ( CurrEdge<4  
					) }) )
				}	 
				state("saveMap") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.showMap(  )
						itunibo.planner.plannerUtil.saveRoomMap( NameFile  )
						itunibo.planner.plannerUtil.loadRoomMap( NameFile  )
					}
					 transition( edgeName="goto",targetState="planNextDirty", cond=doswitch() )
				}	 
				state("planNextDirty") { //this:State
					action { //it:State
						 Table = true  
						itunibo.planner.plannerUtil.planForNextDirty(  )
						 CurrMov = itunibo.planner.plannerUtil.getNextPlannedMove()  
						println("Next move $CurrMov")
						itunibo.planner.plannerUtil.showMap(  )
					}
					 transition( edgeName="goto",targetState="endState", cond=doswitchGuarded({ CurrMov == ""  
					}) )
					transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({! ( CurrMov == ""  
					) }) )
				}	 
				state("visitInternalCell") { //this:State
					action { //it:State
						 CurrMov = itunibo.planner.plannerUtil.getNextPlannedMove()  
					}
					 transition( edgeName="goto",targetState="planNextDirty", cond=doswitchGuarded({ CurrMov == ""  
					}) )
					transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({! ( CurrMov == ""  
					) }) )
				}	 
				state("doMove") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.showMap(  )
					}
					 transition( edgeName="goto",targetState="doStep", cond=doswitchGuarded({ CurrMov == "w"  
					}) )
					transition( edgeName="goto",targetState="doTurn", cond=doswitchGuarded({! ( CurrMov == "w"  
					) }) )
				}	 
				state("doStep") { //this:State
					action { //it:State
						request("step", "step($Step)" ,"basicrobot" )  
						println("w")
						delay(1000) 
					}
					 transition(edgeName="t47",targetState="succesStep",cond=whenReply("stepdone"))
					transition(edgeName="t48",targetState="obstacleFound",cond=whenReply("stepfail"))
				}	 
				state("doTurn") { //this:State
					action { //it:State
						forward("cmd", "cmd($CurrMov)" ,"basicrobot" ) 
						println("$CurrMov")
						itunibo.planner.plannerUtil.updateMap( CurrMov  )
						delay(1000) 
					}
					 transition( edgeName="goto",targetState="visitInternalCell", cond=doswitch() )
				}	 
				state("tableFound") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.updateMapObstacleOnCurrentDirection(  )
						println("tavolo = $Table ")
					}
					 transition( edgeName="goto",targetState="visitInternalCell", cond=doswitch() )
				}	 
				state("endState") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.saveRoomMap( NameFile  )
						answer("map", "mapdone", "mapdone(0)"   )  
						println("MAPPER | terminate...")
						terminate(1)
					}
				}	 
			}
		}
}
