/* Generated by AN DISI Unibo */ 
package it.unibo.dishwasher

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Dishwasher ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				val DishwasherObserver = util.ActorCoapObserver("localhost",8040,"ctxsystem","dishwasher")
		//		val DishwasherObserver = util.ActorCoapObserver("192.168.1.171",8070,"ctxmaitre","dishwasher")	
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("DISHWASHER | STARTS and it's empty...")
						solve("consult('DishwasherState.pl')","") //set resVar	
						println("DISHWASHER | loaded initial state")
						 DishwasherObserver.activate(myself, arrayListOf("Added", "Removed", "Fail"))  
						println("DISHWASHER | activated DishwasherObserver")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("DISHWASHER| working...")
					}
					 transition(edgeName="t057",targetState="exposeState",cond=whenDispatch("consult"))
					transition(edgeName="t058",targetState="handleChangeState",cond=whenDispatch("changeState"))
				}	 
				state("exposeState") { //this:State
					action { //it:State
						solve("getAllEl(Crockery)","") //set resVar	
						if( currentSolution.isSuccess() ) {println("DISHWASHER | Crockery = ${getCurSol("Crockery")}")
						updateResourceRep( "${getCurSol("Crockery")}"  
						)
						}
						else
						{println("DISHWASHER | Error consulting dishwasher...")
						updateResourceRep( "ERROR"  
						)
						}
						println("DISHWASHER | sending state informations/exposed content to maitre...")
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
								if( currentSolution.isSuccess() ) {println("DISHWASHER | added Crockery : $Crockery...")
								updateResourceRep( "Added Crockery $Crockery with success!"  
								)
								}
								else
								{println("DISHWASHER | Error adding Crockery : $Crockery...")
								updateResourceRep( "Fail adding Crockery $Crockery!"  
								)
								}
						}
						if( checkMsgContent( Term.createTerm("changeState(X,ARG)"), Term.createTerm("changeState(remove,ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Crockery = payloadArg(1)  
								solve("remove($Crockery)","") //set resVar	
								if( currentSolution.isSuccess() ) {println("DISHWASHER | removed Crockery : $Crockery...")
								updateResourceRep( "Removed Crockery $Crockery with success!"  
								)
								}
								else
								{println("DISHWASHER | Error removing Crockery : $Crockery...")
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
