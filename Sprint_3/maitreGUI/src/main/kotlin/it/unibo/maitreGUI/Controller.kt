package it.unibo.maitreGUI

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.util.HtmlUtils

@Controller
class Controller {
    @Value("\${human.logo}")
    var appName: String?    = null
    //change port and ctx
	var ctx="ctxsystem"
	var actor="maitre"
	var port = "8040"
	
    var coap  = CoapSupport("coap://localhost:$port", "$ctx/$actor")
	//Resource that sends message to the maitre actor
    var maitreResource= MaitreResource
    //TODO gestire HOMEPAGE
    @GetMapping("/")
    suspend fun home(viewmodel : Model,
        @RequestParam(name="sonarvalue", required=false, defaultValue="0")v : String) : String{
        viewmodel.addAttribute("arg", appName )
		//maitreResource!!.execPrepare()
        return  "pag1"
    }

	
    //TODO definire argomenti in ingresso
    @GetMapping("/prepare")
    suspend fun prepare(viewmodel : Model,
        @RequestParam(name="sonarvalue", required=false, defaultValue="0")v : String) : String{
        viewmodel.addAttribute("arg", appName )
		//maitreResource!!.execPrepare()
        return  "pag2"
    }

	//TODO definire argomenti in ingresso
    //TODO leggere risposta
    @GetMapping("/addofood")
    suspend fun  addofood(viewmodel : Model,
        @RequestParam(name="sonarvalue", required=false, defaultValue="0")v : String) : String{
       // maitreResource!!.execAddFood()
	
        //Leggo la risposta dal maitre
        coap.readResource()
        return "pag2"
    }
    
	//TODO definire argomenti in ingresso
    @GetMapping("/clear")
    suspend fun  clear(viewmodel : Model,
        @RequestParam(name="sonarvalue", required=false, defaultValue="0")v : String) : String{
        viewmodel.addAttribute("arg", appName )
        //maitreResource!!.execClear()
        return  "pag3"
    }

	//TODO: leggere risposta
    @GetMapping("/consult")
    suspend fun consult(model: Model): String {
		maitreResource!!.execConsult()
		//Leggo la risposta dal maitre
        coap.readResource()
        model.addAttribute("arg", appName )
		//TODO come scelgo in che pag andare??
        return  "pag1"
    }

    //TODO leggere risposta
	@GetMapping("/stop")
	suspend fun stop(model: Model): String {
		maitreResource!!.execStop()
		//Leggo la risposta dal maitre
        coap.readResource()
        model.addAttribute("arg", appName )
		
        return  "pag4"
    }

	@GetMapping("/reactivate")
	suspend fun reactivate(model: Model): String {
		maitreResource!!.execReactivate()
        model.addAttribute("arg", appName )
        //TODO come scelgo in che pag andare??
        return  "pag1"
    }
}