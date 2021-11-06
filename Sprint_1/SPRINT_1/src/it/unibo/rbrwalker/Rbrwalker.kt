/* Generated by AN DISI Unibo */ 
package it.unibo.rbrwalker

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Rbrwalker ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.plannerUtil.loadRoomMap( "roomMap"  )
						itunibo.planner.plannerUtil.showMap(  )
						println("WALKER | STARTS...")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("WALKER | waits a goal...")
					}
					 transition(edgeName="t025",targetState="goToGoal",cond=whenRequest("setGoal"))
					transition(edgeName="t026",targetState="terminateWalker",cond=whenEvent("endall"))
				}	 
				state("goToGoal") { //this:State
					action { //it:State
						
									var X = ""
									var Y = ""	
									var CurPos : Pair<Int, Int> ?= null
						if( checkMsgContent( Term.createTerm("setGoal(X,Y)"), Term.createTerm("setGoal(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												X = payloadArg(0)
												Y = payloadArg(1)
						}
						println("WALKER | received the goal ($X, $Y)...")
						  
									var Ac = "empty"
									itunibo.planner.plannerUtil.planForGoal(X, Y)
									Ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									while(Ac!="") {
						forward("cmd", "cmd($Ac)" ,"basicrobot" ) 
						delay(800) 
						
										itunibo.planner.plannerUtil.updateMap(Ac) 
										Ac = itunibo.planner.plannerUtil.getNextPlannedMove()
									}
						forward("cmd", "cmd(h)" ,"basicrobot" ) 
						delay(800) 
						
									CurPos = itunibo.planner.plannerUtil.get_curPos()
									itunibo.planner.plannerUtil.showMap()
						println("WALKER | arrived to the goal")
						answer("setGoal", "goalState", "goalState($CurPos)"   )  
						updateResourceRep( "$CurPos"  
						)
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("terminateWalker") { //this:State
					action { //it:State
						println("WALKER | terminate...")
						terminate(1)
					}
				}	 
			}
		}
}
