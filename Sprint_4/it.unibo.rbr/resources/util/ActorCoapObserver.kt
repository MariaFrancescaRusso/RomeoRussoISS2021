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
import java.util.ArrayList
import it.unibo.kactor.QakContext 
 
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

//ignore allows to ignore state update not usefull in a particular step
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	 fun activate( owner: ActorBasic? = null, ignores:ArrayList<String>?= null) { 
       val uriStr = "coap://$ipaddr/$context/$destactor"
    	var ignored = false
//       val ownerName = owner?.getName()
	   println("observer$destactor | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				content = response.responseText
                println("observer$destactor | GET RESP-CODE= " + response.code + " content:" + content)
                if(ignores!=null){
					for(i in ignores!!){
						if(content.contains(i)){
							ignored = true
							break
						}
					}
				}
 				if(  owner!== null && !ignored) owner.scope.launch {
 					val event = MsgUtil.buildEvent( "observer$destactor","observer$destactor","observer$destactor('$content')")
					owner.emit( event , false) 
				}
				ignored = false
           } 
            override fun onError() {
                println("actortQakCoapObserver | FAILED")
            }
        })		
	}
}