package util

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import kotlinx.coroutines.channels.Channel
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.CoAP
import kotlinx.coroutines.runBlocking
import org.eclipse.californium.core.CoapObserveRelation

class UpdateHandler(val name : String, val channel : Channel<String>,
					val expected:String?=null) : CoapHandler {

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("	$name | content=$content  expected=$expected RESP-CODE=${response.code} " )
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

class CoapObserverForTest(val name: String      = "testingobs",
							 val ip: String      = "127.0.0.1",
							 val context: String   = "ctxfridge",
							 val observed : String = "fridge",
							 val port: String      = "8060") {

   private var client  : CoapClient?  = null
   private lateinit var handler : CoapHandler
   private val uriStr = "coap://$ip:$port/$context/$observed"
   private var observation : CoapObserveRelation? = null
   private var isStarted = false
  
   fun setup(channel : Channel<String>, expected:String?=null ) {
	    client     = CoapClient()
	    client!!.uri = uriStr
   }
	     
   fun addObserver(  channel : Channel<String>, expected:String?=null ) {
	   if (!isStarted){
		   setup(channel, expected)
	   }
	   handler = UpdateHandler( "h_$name", channel, expected)
	   observation = client!!.observe( handler )
   	   println("	$name | START uriStr: $uriStr - expected=$expected"  ) 
	}		 
   
	fun removeObserver(){
		observation!!.proactiveCancel()
	}
	
   fun terminate() {
	   removeObserver()
//	   client!!.shutdown()
//	   client = null
	}		
}