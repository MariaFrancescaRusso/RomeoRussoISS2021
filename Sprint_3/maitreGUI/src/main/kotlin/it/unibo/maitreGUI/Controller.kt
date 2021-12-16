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


@Controller
class Controller {
	@Value("\${spring.application.name}")
	var appName : String? = null
	var ctx = "ctxsystem"
	var actor = "maitre"
	var port = "8040"
	var channel= Channel<String>()
	var caller = "spring"
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	var maitreResource = MaitreResource("maitreresource")

	//TODO: gestire HOMEPAGE
	@GetMapping("/")
	suspend fun home() : String {
		return  "MaitreGUI"
	}

	//TODO definire argomenti in ingresso
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/prepare")
	suspend fun prepare(viewmodel : Model,
	@RequestParam(name="prepareChoose", required=false, defaultValue="")v : String) : String {
		viewmodel.addAttribute("arg", appName )
		//maitreResource!!.execPrepare()
		//		maitreResource!!.execConsult()
		return  "MaitreGUI"
	}

	//TODO definire argomenti in ingresso
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/addofood")
	suspend fun  addofood(viewmodel : Model,
			@RequestParam(name="fodeCode", required=false, defaultValue="0")v : String) : String{
				// maitreResource!!.execAddFood()
				return "pag2"
			}

	//TODO definire argomenti in ingresso
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/clear")
	suspend fun  clear(viewmodel : Model): String{
				viewmodel.addAttribute("arg", appName )
				//maitreResource.execClear()
				return  "pag3"
			}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/consult")
	suspend fun consult(model: Model): String {
		var res = maitreResource.execConsult()
		println(res)
		//model.addAttribute("arg", appName )
		return  "MaitreGUI"
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/stop")
	suspend fun stop(model: Model): String {
		maitreResource.execStop()

		//model.addAttribute("arg", appName )
		return  "pag4"
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/reactivate")
	suspend fun reactivate(model: Model): String {
		maitreResource.execReactivate()
		//model.addAttribute("arg", appName )
		//TODO come scelgo in che pag andare??
		return  "pag1"
	}

	@ExceptionHandler
	fun handle(ex: Exception): ResponseEntity<*> {
		val responseHeaders = HttpHeaders()
				return ResponseEntity(
						"BaseController ERROR ${ex.message}", 
						responseHeaders, HttpStatus.CREATED
						)
	}
}