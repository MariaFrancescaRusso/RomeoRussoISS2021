package util

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import java.util.Scanner
import org.eclipse.californium.core.CoapHandler
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch 
 
class ActorCoapObserver(ip:String, port:Int, context:String, destactor:String) {

    private val client = CoapClient()
	
	private var ipaddr:String
	private var context:String
 	private var destactor:String
 	private var content:String
	
 	init {
    		this.ipaddr      = ip+":"+port.toString()
    		this.context     = context     
    		this.destactor   = destactor
    		this.content	 = ""
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	 fun activate( owner: ActorBasic? = null) { 
       val uriStr = "coap://$ipaddr/$context/$destactor"
//       val ownerName = owner?.getName()
	   println("observer$destactor | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				content = response.responseText
                println("observer$destactor | GET RESP-CODE= " + response.code + " content:" + content)
 				if(  owner!== null ) owner.scope.launch {
 					val event = MsgUtil.buildEvent( "observer$destactor","observer$destactor","observer$destactor('$content')")
					owner.emit( event, avatar=true ) //to avoid that auto-event will be discarded
				}
           } 
            override fun onError() {
                println("actortQakCoapObserver | FAILED")
            }
        })		
	}
}