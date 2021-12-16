package it.unibo.maitreGUI

import kotlinx.coroutines.channels.Channel
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.json.JSONObject

@Controller
class Controller {
	@Value("\${spring.application.name}")
	var appName : String? = null
	//TODO: change port and ctx
	var addr = "localhost"
	var port = "8040"
	var ctx = "ctxsystem"
	var actor = "maitre"
	var caller = "spring"
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	var maitreResource = MaitreResource(caller, addr, port, ctx, actor)

	//TODO: gestire HOMEPAGE
	@GetMapping("/")
	suspend fun home() : String {
		var ConsultStr = maitreResource.execConsult()
		var jsonContent = JSONObject(ConsultStr)
		return  "MaitreGUI"
	}

	//TODO definire argomenti in ingresso
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/prepare")
	suspend fun prepare(viewmodel : Model,
	@RequestParam(name="prepareChoose", required=false, defaultValue="")v : String) : String {
		
		viewmodel.addAttribute("prepareMenù", appName )
//		maitreResource!!.execPrepare()
		return "MaitreGUI"
	}

//	//TODO definire argomenti in ingresso
//	//TODO leggere risposta
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	@GetMapping("/addfood")
//	suspend fun  addfood(viewmodel : Model,
//		@RequestParam(name="sonarvalue", required=false, defaultValue="0")v : String) : String {
//	// maitreResource!!.execAddFood()
//		//Leggo la risposta dal maitre
//		coap.readResource()
//		return "pag2"
//	}
//
//	//TODO definire argomenti in ingresso
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	@GetMapping("/clear")
//	suspend fun clear(viewmodel : Model): String{
//				viewmodel.addAttribute("arg", appName )
//				//maitreResource.execClear()
//				return "pag3"
//			}
//
//	//TODO: leggere risposta
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	@GetMapping("/consult")
//	suspend fun consult(model: Model): String {
//		maitreResource!!.execConsult()
//		var res = maitreResource.execConsult()
//		println(res)
//		//Leggo la risposta dal maitre
//		coap.readResource()
//		model.addAttribute("arg", appName )
//		//TODO come scelgo in che pag andare??
//		return "pag1"
//	}
//
//	//TODO leggere risposta
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	@GetMapping("/stop")
//	suspend fun stop(model: Model): String {
//		maitreResource!!.execStop()
//		//Leggo la risposta dal maitre
//		coap.readResource()
//		model.addAttribute("arg", appName )
//		return "pag4"
//	}
//
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	@GetMapping("/reactivate")
//	suspend fun reactivate(model: Model): String {
//		maitreResource!!.execReactivate()
//		model.addAttribute("arg", appName )
//		//TODO come scelgo in che pag andare??
//		return "pag1"
//	}
//	
//	//TODO eventualmente potremmo fare una pag @GetMapping("/settings") che possa gestire il cambio addres, port, e contesto
//	
//	@ExceptionHandler
//	fun handle(ex: Exception): ResponseEntity<*> {
//		val responseHeaders = HttpHeaders()
//		return ResponseEntity(
//			"BaseController ERROR ${ex.message}", 
//			responseHeaders, HttpStatus.CREATED
//			)
//	}
}
