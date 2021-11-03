/* Generated by AN DISI Unibo */ 
package it.unibo.pantry

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Pantry ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				val PantryObserver = util.ActorCoapObserver("localhost",8040,"ctxsystem","pantry")
		//		val PantryObserver = util.ActorCoapObserver("192.168.1.171",8070,"ctxmaitre","pantry")
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("PANTRY | STARTS and it's filled with a proper set of items...")
						solve("consult('PantryState.pl')","") //set resVar	
						println("PANTRY | loaded initial state")
						 PantryObserver.activate(myself, arrayListOf("Added", "Removed", "Fail"))  
						println("PANTRY | activated PantryObserver")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("PANTRY| working...")
					}
					 transition(edgeName="t015",targetState="exposeState",cond=whenDispatch("consult"))
					transition(edgeName="t016",targetState="handleChangeState",cond=whenDispatch("changeState"))
				}	 
				state("exposeState") { //this:State
					action { //it:State
						solve("getAllEl(Crockery)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("PANTRY | Crockery : ${getCurSol("Crockery")}")
						updateResourceRep( "${getCurSol("Crockery")}"  
						)
						}
						else
						{println("PANTRY | Error consulting pantry...")
						updateResourceRep( "ERROR"  
						)
						}
						println("PANTRY| sending state informations/exposed content to maitre...")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleChangeState") { //this:State
					action { //it:State
						 var Crockery = ""  
						if( checkMsgContent( Term.createTerm("changeState(X,ARG)"), Term.createTerm("changeState(add,ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Crockery = payloadArg(1)  
								solve("add($Crockery)","") //set resVar	
								if( currentSolution.isSuccess() ) {println("PANTRY | added Crockery : $Crockery...")
								updateResourceRep( "Added Crockery $Crockery with success!"  
								)
								}
								else
								{println("PANTRY | Error adding Crockery : $Crockery...")
								updateResourceRep( "Fail adding Crockery $Crockery!"  
								)
								}
						}
						if( checkMsgContent( Term.createTerm("changeState(X,ARG)"), Term.createTerm("changeState(remove,ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Crockery = payloadArg(1)  
								solve("remove($Crockery)","") //set resVar	
								if( currentSolution.isSuccess() ) {println("PANTRY | removed Crockery : $Crockery...")
								updateResourceRep( "Removed Crockery $Crockery with success!"  
								)
								}
								else
								{println("PANTRY | Error removing Crockery : $Crockery...")
								updateResourceRep( "Fail removing Crockery $Crockery!"  
								)
								}
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
