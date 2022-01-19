package it.unibo.maitreGUI

import it.unibo.connQak.ConnectionType
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@kotlinx.coroutines.ObsoleteCoroutinesApi
@Controller
class Controller {
	@Value("\${spring.application.name}")
	var appName : String? = null
	// The change of address, port, context and protocol
	// can be done at starts at: localhost:8081/settings
	var addr = "localhost"
	var port = "8040"
	var ctx = "ctxsystem"
	var actor = "maitre"
	var caller = "spring"
	var protocol = ConnectionType.TCP
	var maitreResource : MaitreResource ?= null
//	var maitreResource = MaitreResource(caller, addr, port, ctx, actor, protocol)
	
	// To keep the status of elements, consult, stop, web page name
	var PantryEl = ArrayList<List<String>>()
	var FridgeEl = ArrayList<List<String>>()
	var DishwasherEl = ArrayList<List<String>>()
	var TableDishes = ArrayList<List<String>>()
	var TableFood = ArrayList<List<String>>()
	var Stopped = false
	var Consulted = false
	var CurPage = "MaitreGUI_page2"
	
	@GetMapping("/")
	suspend fun home(viewmodel : Model) : String {
		maitreResource = MaitreResource(caller, addr, port, ctx, actor, protocol)
		
		//TODO: rifaccio anche il consult se l ho fatto prima di setting? si perche cambio maitre? e prima quindi non andava?
//		if (!Consulted) {
			// At homepage load, a consult command is sent
			var ConsultStr = maitreResource!!.execConsult()
//			var ConsultStr = "{fridge:\"[[s001,bread,15],[d001,water,12]]\"}+{dishwasher:\"[]\"}+{pantry:\"[[dishes,30],[glasses,30]]\"}+{table:\"[];[]\"}+"
			println("CONTROLLER | sent consult to prepare the web homepage...")
			saveConsultRes(ConsultStr)
//			Consulted = true
//		}
		// To fill the prepare selection
		showPrepareEl(viewmodel)
		
		// To fill the consult output
		showConsult(viewmodel)
					
		return  "MaitreGUI"
	}
	
	@GetMapping("/prepare")
	suspend fun prepare(viewmodel : Model,
						@RequestParam prepareButton : String,
						@RequestParam elURI : Map<String, String>) : String {
		var Crockery = ""
		var Food = ""
		
		println("CONTROLLER | managing prepare button \"$prepareButton\"...")
		when(prepareButton) {
			"Default Prepare" ->  {}
			
			"Prepare the Room" -> {
				// To obtain the crockery and food elements selected,
				// checking also if one of them is empty
				// and in this case the resulting string will be "", as default case
				var Res = takePrepareSelEl(elURI)
				Crockery = Res.get(0)
				Food = Res.get(1)
			}
			
			else -> throw Exception("No prepare button selected")			
		}
		
		//TODO: posso togliere i !! perchè non necessari??
		maitreResource!!.execPrepare(Crockery, Food)
		println("CONTROLLER | sent prepare...")
		
		// At next page load, to fill the add food output
		showFoodCodes(viewmodel)
		return "MaitreGUI_page2"
	}
	
	@GetMapping("/addFood")
	suspend fun  addFood(viewmodel : Model,
						 @RequestParam addFoodButton : String,
						 @RequestParam foodCode : String) : String {			
		println("CONTROLLER | managing addFood button \"$addFoodButton\"...")
		
		// To check if a foodCode has been entered
		if (foodCode.isNullOrBlank() || foodCode.isEmpty())
			showWarning(viewmodel, "Insert a food-code!")
		else {
			var addFoodStr = maitreResource!!.execAddFood(foodCode)
//			var addFoodStr = ""
			println("CONTROLLER | sent add food with food-code $foodCode...")
			
			// To check if a warning has been received
			if (addFoodStr.startsWith("Warning!"))
				showWarning(viewmodel, addFoodStr)
		}
			
		// At next page load, to fill the add food output
		showFoodCodes(viewmodel)			
		return "MaitreGUI_page2"
	}
	
	@GetMapping("/clear")
	suspend fun  clear (@RequestParam clearButton : String) : String {		
		println("CONTROLLER | managing clear button \"$clearButton\"...")
		
		maitreResource!!.execClear()
		println("CONTROLLER | sent clear...")
		
		Consulted = false
			
		CurPage = "MaitreGUI_page3"
		return CurPage
	}
	
	@GetMapping("/consult")
	suspend fun  consult (viewmodel : Model,
						  @RequestParam consultButton : String) : String {
		println("CONTROLLER | managing consult button \"$consultButton\"...")
		var ConsultStr = maitreResource!!.execConsult()
//		var ConsultStr = "{fridge:\"[[s001,bread,15],[d001,water,12]]\"}+{dishwasher:\"[]\"}+{pantry:\"[[dishes,30],[glasses,30]]\"}+{table:\"[];[]\"}+"
		println("CONTROLLER | sent consult...")
		saveConsultRes(ConsultStr)
		
		// To fill the consult output
		stayConsulted(viewmodel)
		Consulted = true
		
		// Check of stop status to mantain its status on the web page
		if(Stopped)
			stayStopped(viewmodel)
				
		if (CurPage == "MaitreGUI_page2")
			// To fill the add food output
			showFoodCodes(viewmodel)
		return CurPage
	}
	
	@GetMapping("/stopReactivate")
	suspend fun stopReactivate (viewmodel : Model,
								@RequestParam stopReactivateButton : String) : String {
		when(stopReactivateButton) {
			"Stop Task" -> {
				println("CONTROLLER | managing stop button \"$stopReactivateButton\"...")
				var StopStr = maitreResource!!.execStop()
//				var StopStr = ""
				println("CONTROLLER | sent stop...")
				
				// To check if the stop has been executed
				if (StopStr.startsWith("There is NO"))
					viewmodel.addAttribute("stopStrRes", StopStr)
				else {					
					// To put the web page in stop mode
					stayStopped(viewmodel)
					viewmodel.addAttribute("stopStrRes", StopStr)
					Stopped = true
				}
			}
			
			"Reactivate Task" -> {
				println("CONTROLLER | managing reactivate button \"$stopReactivateButton\"...")
				maitreResource!!.execReactivate()
				println("CONTROLLER | sent reactivate...")
				
				viewmodel.addAttribute("stopReactivateValue", "Stop Task")
				viewmodel.addAttribute("disableEl", false)
				Stopped = false
			}
			
			else -> throw Exception("No stop or reactivate button selected")	
		}
		
		// To keep the consult output on the web page if it has already sent 
		if (Consulted)
			stayConsulted(viewmodel)
		
		if (CurPage == "MaitreGUI_page2")
			// To fill the add food output
			showFoodCodes(viewmodel)
				
		return CurPage
	}
	
	// To manage addr, port, ctx and protocol changes	//?? e parametri di default per prapare e consumer 
	@GetMapping("/settings")
	suspend fun  settings() : String {	
		return "Settings"
	}
	
	@GetMapping("/changeSettings")
	suspend fun  saveSettings(viewmodel : Model,
							  @RequestParam changeButton : String,
							  @RequestParam elURI : Map<String, String>) : String {
		var count = 0
		
		println("CONTROLLER | managing settings button \"$changeButton\"...")
				
		for (key in elURI.keys) {
			var value = elURI.getValue(key)
			// To check if some value has changed		
			if (value.isEmpty() || value.isNullOrBlank())
				count++
			else {
				when (key) {
					"addr" -> this.addr = value
					
					"port" -> this.port = value
					
					"ctx" -> this.ctx = value
					
					"protocol" -> this.protocol = ConnectionType.valueOf(value)
					
					else -> count--
				}
			}
		}
		
		if (count == 4) {
			showWarning(viewmodel, "Insert at least an element to change!")
			return "Settings"
		}
		else
			return home(viewmodel)
	}

//########################## UTILITY FUNCTIONS ##########################//

	// Function to split the resulting string from consult command, to obtain a string for each resource
	fun saveConsultRes(ConsultRes : String) {
		for (el in ConsultRes.split("+")) {
			if (!el.isEmpty()) {
//				println("CONTROLLER | Consult Result element: $el")
				//each el has a form {resource: content}
				try {
					val jsonContent = JSONObject(el)
//					println("CONTROLLER | Consult Result json element: $jsonContent")
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
	
	// To fill the consult output
	fun showConsult(viewmodel : Model) {		
		var ConsultRes = arrayListOf<String>()
		ConsultRes.add("Pantry: ${checkEl(PantryEl)}\n")
		ConsultRes.add("Fridge: ${checkEl(FridgeEl)}\n")
		ConsultRes.add("Dishwasher: ${checkEl(DishwasherEl)}\n")
		ConsultRes.add("Table: ${checkEl(TableDishes)} and ${checkEl(TableFood)}")
		viewmodel.addAttribute("consultRes", ConsultRes)
	}
	
	// To check if the resource array is empty
	fun checkEl(el : ArrayList<List<String>>) : String {		
		if (el.isEmpty())
			return "empty"
		else
			return "$el"
	}
	
	// To fill the prepare selection
	fun showPrepareEl(viewmodel : Model) {
		//each pantry el has a form [[NAME, QUANTITY],...]
		viewmodel.addAttribute("preparePantry", PantryEl)
						
		//each fridge el has a form [[CODE, NAME, QUANTITY],...]
		viewmodel.addAttribute("prepareFridge", FridgeEl)
	}
	
	// To obtain the crockery and food elements for prepare command
	fun takePrepareSelEl(elURI: Map<String, String>) : List<String> {
		var Crockery = ArrayList<List<String>>()
		var Food = ArrayList<List<String>>()
		var ResCrockery = ""
		var ResFood = ""
		
		// To take pantry selected elements	
		for (el in PantryEl) {
			if (elURI.containsKey(el.get(0))) {
				var quantity = elURI.getValue("quantity"+el.get(0))
				
				// To set the quantity, which is 1 by default
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

				// To set the quantity, which is 1 by default
				if (quantity != "0")
					Food.add(listOf(el.get(0), quantity))
				else
					Food.add(listOf(el.get(0), "1"))
			}
		}		
		println("CONTROLLER | Food selected: $Food...")
		
		// To check if there are selected elements,
		// otherwise it will be sent an empty string, as default case
		if(!Crockery.isEmpty())
			ResCrockery = Crockery.toString()
		
		if(!Food.isEmpty())
			ResFood = Food.toString()
			
		return listOf(ResCrockery, ResFood)
	}
	
	// To fill the add fodd output
	fun showFoodCodes(viewmodel : Model) {
		var FoodList = ""
		
		for (el in FridgeEl) {
			FoodList += "${el.get(0)} - ${el.get(1)}\n"
		}

		viewmodel.addAttribute("foodList", FoodList)
	}
		
	// To put the page in stop mode
	fun stayStopped(viewmodel : Model) {
		viewmodel.addAttribute("stopReactivateValue", "Reactivate Task")
		viewmodel.addAttribute("disableEl", true)
	}
	
	// To keep the consult output on the page if it has already sent 
	fun stayConsulted(viewmodel : Model) {
		viewmodel.addAttribute("consulthiddenAttr", false)
		viewmodel.addAttribute("consultOpenAttr", true)
		showConsult(viewmodel)
	}
	
	// To show a warning for addFood task
	fun showWarning(viewmodel : Model, addFoodStr : String) {
		viewmodel.addAttribute("warningStrRes", addFoodStr)
		viewmodel.addAttribute("addFoodOpenAttr", true)
		
		// To keep the consult output on the page if it has already sent 
		if (Consulted)
			stayConsulted(viewmodel)
	}
}