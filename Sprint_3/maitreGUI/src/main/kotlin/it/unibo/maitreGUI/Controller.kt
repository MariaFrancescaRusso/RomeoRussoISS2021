package it.unibo.maitreGUI

//import it.unibo.connQak.ConnectionType
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
import org.springframework.web.bind.annotation.PostMapping

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
//	var protocol = ConnectionType.TCP
	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	var maitreResource = MaitreResource(caller, addr, port, ctx, actor, protocol)
	
	var PantryEl = ArrayList<List<*>>()
	var FridgeEl = ArrayList<List<*>>()
	var DishwasherEl = ArrayList<List<*>>()
	var TableDishes = ArrayList<List<*>>()
	var TableFood = ArrayList<List<*>>()
	
	//TODO: gestire HOMEPAGE
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/")
	suspend fun home(viewmodel : Model) : String {
		// At homepage load, a consult command is sent
//		var ConsultStr = maitreResource.execConsult()
		var ConsultStr = "{fridge:\"[[s001,bread,15],[d001,water,12]]\"}+{dishwasher:\"[]\"}+{pantry:\"[[dishes,30],[glasses,30]]\"}+{table:\"[];[]\"}+"
		saveConsultRes(ConsultStr)
		
		// To fill the prepare selection for the homepage
		showPrepareEl(viewmodel)
		
		// To fill the consult output for the homepage
		showConsult(viewmodel)
					
		return  "MaitreGUI"
	}

	//TODO definire argomenti in ingresso
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/prepare")
	suspend fun prepare(viewmodel : Model,
	@RequestParam(name="prepareButton", required=false, defaultValue="")v : String) : String {
		println("CONTROLLER | managing prepare button...")
		
		
		println("CONTROLLER | sent prepare...")
//		maitreResource!!.execPrepare()
		return "MaitreGUI_page2"
	}

//	//TODO definire argomenti in ingresso
//	//TODO leggere risposta
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	@GetMapping("/addfood")
//	suspend fun  addfood(viewmodel : Model,
//		@RequestParam(name="sonarvalue", required=false, defaultValue="0")v : String) : String {
//	// maitreResource!!.execAddFood()
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

//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	@PostMapping("/consult")
//	suspend fun consult (viewmodel : Model,
//	@RequestParam(name="consultButton", required=false, defaultValue="")v : String) : String {
//		println("CONTROLLER | managing consult button...")
//		maitreResource!!.execConsult()
//		var res = maitreResource.execConsult()
//		println(res)
//		model.addAttribute("arg", appName )
//		//TODO come scelgo in che pag andare??
//		return "MaitreGUI"
//	}

//	//TODO leggere risposta
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	@GetMapping("/stop")
//	suspend fun stop(model: Model): String {
//		maitreResource!!.execStop()
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
//	//TODO eventualmente potremmo fare una pag @GetMapping("/settings") che possa gestire il cambio addres, port, contesto, protocollo e parametri di default per prapare e consumer
//	
//	@ExceptionHandler
//	fun handle(ex: Exception): ResponseEntity<*> {
//		val responseHeaders = HttpHeaders()
//		return ResponseEntity(
//			"BaseController ERROR ${ex.message}", 
//			responseHeaders, HttpStatus.CREATED
//			)
//	}
	
	// Function to split the resulting string from consult command, to obtain a string for each resource
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun saveConsultRes(ConsultRes : String) {
		for (el in ConsultRes.split("+")) {
			if (!el.isEmpty()) {
				println("CONTROLLER | Consult Result element: $el")
				//each el has a form {resource: content}
				try {
					val jsonContent = JSONObject(el)
					println("CONTROLLER | Consult Result json element: $jsonContent")
					if (jsonContent.has("pantry")) {
						PantryEl = stringToArray(jsonContent.getString("pantry"))
						println("CONTROLLER | Pantry: $PantryEl")
					}
					if (jsonContent.has("table")) {
						var TableEl = jsonContent.getString("table").split(";")
						TableDishes = stringToArray(TableEl.get(0))
						TableFood = stringToArray(TableEl.get(1))
						println("CONTROLLER | Table: $TableDishes and $TableFood")
					}
					if (jsonContent.has("fridge")) {
						FridgeEl = stringToArray(jsonContent.getString("fridge"))
						println("CONTROLLER | Fridge: $FridgeEl")
					}
					if (jsonContent.has("dishwasher")) {
						DishwasherEl = stringToArray(jsonContent.getString("dishwasher"))
						println("CONTROLLER | Dishwasher: $DishwasherEl")
					}
				}
				catch(e:Exception) {
					println("CONTROLLER| ERROR=${el} e=$e")
				}
			}
		}
	}
	
	// To convert the resource string to an array
	fun stringToArray(s : String) : ArrayList<List<*>> {
		var Res = ArrayList<List<*>>()
		
		if (s == "[]")
			return Res
		else {
			var sEl = s.removeSurrounding("[[", "]]").split("],[")
			
			for (el in sEl) {
				//each el has a form:
					// [[NAME, QUANTITY],...] if crockery;
					// [[CODE, NAME, QUANTITY],...] if food.
				var Content = el.split(",")
				Res.add(Content)
			}

			return Res
		}
	}
	
	// To fill the consult output for the homepage
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun showConsult(viewmodel : Model) {
				
		var ConsultRes = "Pantry: ${checkEl(PantryEl)}\n"
		ConsultRes += "Fridge: ${checkEl(FridgeEl)}\n"
		ConsultRes += "Dishwasher: ${checkEl(DishwasherEl)}\n"
		ConsultRes += "Table: ${checkEl(TableDishes)} and ${checkEl(TableFood)}"
		var Tmp = "<textarea readonly rows=\"10\" cols=\"60\">$ConsultRes</textarea>"
		println("CONTROLLER | Consult result:$ConsultRes")
		viewmodel.addAttribute("consultRes", ConsultRes)
		viewmodel.addAttribute("consultR", Tmp)
	}
	
	// To check if the resource array is empty
	fun checkEl(el : ArrayList<List<*>>) : String {		
		if (el.isEmpty())
			return "empty"
		else
			return "$el"
	}
	
	// To fill the prepare selection for the homepage
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun showPrepareEl(viewmodel : Model) {
		var Name = ""
		var Quantity = ""
//		var Crockery = listOf(name:String, quantity:String)
		//TODO: usare una mappa(nome, quantity) o fare un oggetto crockery e uno food da poter utilizzare o trovare un altro modo ad esempio con l'array già presente. In questo modo si può sfruttare th:each e nome o quantity da html per popolare l'html. Utile anche per prendere i valori della quantity associato al nome.
		viewmodel.addAttribute("sizeSel", PantryEl.size)
		for (el in PantryEl) {
			//each el has a form [[NAME, QUANTITY],...]
			println("CONTROLLER | Pantry content:$Name")
			
		}
		viewmodel.addAttribute("preparePantry", PantryEl)
	}
	
	fun showFoodCodes() {
		
	}
}
