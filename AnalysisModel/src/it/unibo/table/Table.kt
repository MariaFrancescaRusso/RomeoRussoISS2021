/* Generated by AN DISI Unibo */ 
package it.unibo.table

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Table ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		  var Dishes = 0
				var Foods = emptyArray<Int>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("TABLE| loading initial state ")
						solve("consult('TableInit.pl')","") //set resVar	
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("TABLE| working")
					}
					 transition(edgeName="t09",targetState="exposeState",cond=whenDispatch("consult"))
					transition(edgeName="t010",targetState="handleChangeState",cond=whenDispatch("changeState"))
				}	 
				state("exposeState") { //this:State
					action { //it:State
						println("TABLE| sending state informations")
						forward("expose", "expose($Dishes,$Foods)" ,"maitre" ) 
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleChangeState") { //this:State
					action { //it:State
						
									var Nd= 0
									var Fs =  emptyArray<Int>()
						if( checkMsgContent( Term.createTerm("changeState(X)"), Term.createTerm("addFood(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Fs= payloadArg(0).map{ it.toInt() }.toTypedArray()  
								println("TABLE| add $Fs...")
						}
						if( checkMsgContent( Term.createTerm("changeState(X)"), Term.createTerm("removeFood(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 	Fs = payloadArg(0).map{ it.toInt() }.toTypedArray()  
								println("TABLE| remove $Fs...")
						}
						if( checkMsgContent( Term.createTerm("changeState(X)"), Term.createTerm("addDishes(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Nd= payloadArg(0).toInt()  
								println("TABLE| add $Nd...")
								 Dishes = Dishes + Nd   
						}
						if( checkMsgContent( Term.createTerm("changeState(X)"), Term.createTerm("removeDishes(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 	Nd = payloadArg(0).toInt() 
								println("TABLE| remove $Nd...")
								 Dishes = Dishes - Nd   
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
