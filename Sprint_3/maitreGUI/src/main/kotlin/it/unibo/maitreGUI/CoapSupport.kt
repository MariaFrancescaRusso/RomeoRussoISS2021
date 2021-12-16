package it.unibo.maitreGUI

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.ApplMessageType
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapObserveRelation
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry

class CoapSupport(address: String, path: String) {
    private val client: CoapClient
    private lateinit var relation: CoapObserveRelation

    init { 
        val url = "$address/$path"
        client  = CoapClient(url)
        println("CoapSupport | STARTS url=$url client=$client")
        client.setTimeout(1000L)
        val rep = readResource()
        println("CoapSupport | initial rep=$rep")
    }

    fun readResource(): String {
        val respGet: CoapResponse = client.get()
        println("CoapSupport | readResource RESPONSE CODE: " + respGet.getCode())
        return respGet.getResponseText()
    }

    fun removeObserve() {
        relation.proactiveCancel()
    }

    fun observeResource(handler: CoapHandler?) {
        relation = client.observe(handler)
    }

    fun updateResource(msg: String): Boolean {
        println("CoapSupport | updateResource $msg")
        val resp: CoapResponse = client.put(msg, MediaTypeRegistry.TEXT_PLAIN)
        if (resp != null) System.out.println("CoapSupport | updateResource RESPONSE CODE: " + resp.getCode()) else println(
            "CoapSupport | updateResource FAILURE: $resp"
        )
        return resp != null
    }

    fun updateResourceWithValue(msgID:String, messageType:ApplMessageType=ApplMessageType.event, sender:String="support", receiver:String="none",content:String): Boolean {
        val m = ApplMessage(
            msgID, messageType.toString(),
            sender, receiver, content, "1"
        )
        return updateResource(m.toString())
    }
}