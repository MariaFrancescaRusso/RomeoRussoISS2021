package it.unibo.test

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import kotlinx.coroutines.channels.Channel
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.CoAP
import kotlinx.coroutines.runBlocking

class UpdateHandler(val name : String, val channel : Channel<String>,
					val expected:String?=null) : CoapHandler {

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("	%%%%%% $name | content=$content  expected=$expected RESP-CODE=${response.code} " )
				/*
                    2.05 means content (like HTTP 200 "OK" but only used in response to GET requests)
 					4.04 means NOT FOUND
				*/
				if( response.code == CoAP.ResponseCode.NOT_FOUND ) return
				//DISCARD the content not related to testing
				if( content.contains("START") || content.contains("created")) return
				if( expected != null &&  content.contains(expected) )
					runBlocking{ channel.send(content) }
 			} 
            override fun onError() {
                println("$name | FAILED")
            }
        }

class CoapObserverForTesting(val name: String      = "testingobs",
							 val ip: String      = "localhost",
							 val context: String   = "ctxfridge",
							 val observed : String = "fridge",
							 val port: String      = "8060") {

   private var client  : CoapClient?  = null
   private lateinit var handler : CoapHandler
   private val uriStr = "coap://$ip:$port/$context/$observed"   
  
   fun setup( channel : Channel<String>, expected:String?=null ){
 	   client     = CoapClient()
	   println("	%%%%%% $name | START uriStr: $uriStr - expected=$expected"  )
       client!!.uri = uriStr	   
	   handler = UpdateHandler( "h_$name", channel, expected)
   }
	     
   fun addObserver(  channel : Channel<String>, expected:String?=null ){
	   setup(channel,expected)
	   client!!.observe( handler )
	}		 
 
   fun terminate(){
	   println("	%%%%%% $name | terminate $handler"  )
	   //client!!.delete( handler )
	   //client!!.shutdown()
	}		
}