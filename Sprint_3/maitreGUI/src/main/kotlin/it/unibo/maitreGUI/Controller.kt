package it.unibo.maitreGUI

//import it.unibo.connQak.ConnectionType
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

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
	
	var PantryEl = ArrayList<List<String>>()
	var FridgeEl = ArrayList<List<String>>()
	var DishwasherEl = ArrayList<List<String>>()
	var TableDishes = ArrayList<List<String>>()
	var TableFood = ArrayList<List<String>>()
		
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/")
	suspend fun home(viewmodel : Model) : String {
		// At homepage load, a consult command is sent
//		var ConsultStr = maitreResource.execConsult()
		var ConsultStr = "{fridge:\"[[s001,bread,15],[d001,water,12]]\"}+{dishwasher:\"[]\"}+{pantry:\"[[dishes,30],[glasses,30]]\"}+{table:\"[];[]\"}+"
		saveConsultRes(ConsultStr)
		
		// To fill the prepare selection for the homepage "MaitreGUI"
		showPrepareEl(viewmodel)
		
		// To fill the consult output for the homepage "MaitreGUI"
		showConsult(viewmodel)
		
		//TODO: capire come caricare elementi all'avvio di una pagina che non sia la homepage
		// To fill the add fodd output  for the page "MaitreGUI_page2"
		showFoodCodes(viewmodel)
					
		return  "MaitreGUI"
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/prepare")
	suspend fun prepare(@RequestParam prepareButton : String,
						@RequestParam elURI : Map<String, String>) : String {
		var Crockery = ""
		var Food = ""
		
		println("CONTROLLER | managing prepare button \"$prepareButton\"...")
		when(prepareButton) {
			"Default Prepare" ->  {}
			"Prepare the Room" -> {
				var Res = takePrepareSelEl(elURI)
				Crockery = Res.get(0)
				Food = Res.get(1)
			}
			else -> throw Exception("No prepare button selected")			
		}
		
//		maitreResource!!.execPrepare(Crockery, Food)
		println("CONTROLLER | sent prepare...")
		
		return "MaitreGUI_page2"
	}
	
	//TODO: decidere se usare un task unico o due diversi e quindi dividere anche la grafica
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/addFoodClearTask")
	suspend fun  addFoodClearTask (viewmodel : Model,
								   @RequestParam addFoodClearButton : String,
								   @RequestParam foodCode : String) : String {
		var NextPage = ""
		
		println("CONTROLLER | managing prepare button \"$addFoodClearButton\"...")
		when(addFoodClearButton) {
			"Add Food" ->  {
				//TODO: stampa errore su pagina se foodCode è stringa vuota
//				maitreResource!!.execAddFood(foodCode)
				println("CONTROLLER | sent add food with food-code $foodCode...")
				NextPage = "MaitreGUI_page2"
			}
			"Clear the Room" -> {
//				maitreResource!!.execClear("")
				println("CONTROLLER | sent clear...")
				NextPage = "MaitreGUI_page3"
			}
			else -> throw Exception("No add food or clear button selected")			
		}
				
		return "$NextPage"
	}
	
	//TODO: fare tutto
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/consultStopTask")
	suspend fun  consultStopTask (viewmodel : Model) : String {
//	 	maitreResource!!.execAddFood()
		return "pag2"
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
					println("CONTROLLER| ERROR=${el} exception=$e")
				}
			}
		}
	}
	
	// To convert the resource string to an array
	fun stringToArray(s : String) : ArrayList<List<String>> {
		var Res = ArrayList<List<String>>()
		
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
		println("CONTROLLER | Consult result:$ConsultRes")
		viewmodel.addAttribute("consultRes", ConsultRes)
		
		var ConsultRes2 = arrayListOf<String>()
		ConsultRes2.add("Pantry: ${checkEl(PantryEl)}\n")
		ConsultRes2.add("Fridge: ${checkEl(FridgeEl)}\n")
		ConsultRes2.add("Dishwasher: ${checkEl(DishwasherEl)}\n")
		ConsultRes2.add("Table: ${checkEl(TableDishes)} and ${checkEl(TableFood)}")
		viewmodel.addAttribute("consultRes2", ConsultRes2)
	}
	
	// To check if the resource array is empty
	fun checkEl(el : ArrayList<List<String>>) : String {		
		if (el.isEmpty())
			return "empty"
		else
			return "$el"
	}
	
	// To fill the prepare selection for the homepage
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun showPrepareEl(viewmodel : Model) {
		//each pantry el has a form [[NAME, QUANTITY],...]
		viewmodel.addAttribute("preparePantry", PantryEl)
						
		//each fridge el has a form [[CODE, NAME, QUANTITY],...]
		viewmodel.addAttribute("prepareFridge", FridgeEl)
	}
	
	// Function to obtain the crockery and food elements for prepare command
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun takePrepareSelEl(elURI: Map<String, String>) : List<String> {
		var Crockery = ArrayList<List<String>>()
		var Food = ArrayList<List<String>>()
		
		// To take pantry selected elements	
		for (el in PantryEl) {
			if (elURI.containsKey(el.get(0))) {
				var quantity = elURI.getValue("quantity"+el.get(0))
				if (quantity != "0")
					Crockery.add(listOf(el.get(0), quantity))
				else
					Crockery.add(listOf(el.get(0), "1"))
			}
		}		
		println("CONTROLLER | Crockery selected: $Crockery...")
		
		// To take fridge selected elements
		for (el in FridgeEl) {
			if (elURI.containsKey(el.get(0))) {
				var quantity = elURI.getValue("quantity"+el.get(0))
				if (quantity != "0")
					Food.add(listOf(el.get(0), quantity))
				else
					Food.add(listOf(el.get(0), "1"))
			}
		}		
		println("CONTROLLER | Food selected: $Food...")
		
		return listOf(Crockery.toString(), Food.toString())
	}
	
	// To fill the add fodd output
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun showFoodCodes(viewmodel : Model) {
		var FoodList = ""
		
		for (el in FridgeEl) {
			FoodList += "${el.get(0)} - ${el.get(1)}\n"
		}

		viewmodel.addAttribute("foodList", FoodList)
	}
}
