package it.unibo.maitreGUI

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
	//TODO in next sprint: change port and ctx
	var addr = "localhost"
	var port = "8040"
	var ctx = "ctxsystem"
	var actor = "maitre"
	var caller = "spring"
//	var protocol = ConnectionType.TCP	
//	var maitreResource = MaitreResource(caller, addr, port, ctx, actor, protocol)
	
	// To keep the status of elements, consult, stop, page name
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
		// At homepage load, a consult command is sent
//		var ConsultStr = maitreResource.execConsult()
		println("CONTROLLER | sent consult...")
		var ConsultStr = "{fridge:\"[[s001,bread,15],[d001,water,12]]\"}+{dishwasher:\"[]\"}+{pantry:\"[[dishes,30],[glasses,30]]\"}+{table:\"[];[]\"}+"
		saveConsultRes(ConsultStr)
		
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
		
//		maitreResource!!.execPrepare(Crockery, Food)
		println("CONTROLLER | sent prepare...")
		
		// At next homepage load, to fill the add food output
		showFoodCodes(viewmodel)
		return "MaitreGUI_page2"
	}
	
	@GetMapping("/addFoodClearTask")
	suspend fun  addFoodClearTask (viewmodel : Model,
								   @RequestParam addFoodClearButton : String,
								   @RequestParam(required=false) foodCode : String) : String {
		var NextPage : String
		
		println("CONTROLLER | managing prepare button \"$addFoodClearButton\"...")
		when(addFoodClearButton) {
			"Add Food" ->  {
				//TODO: stampa errore su pagina se foodCode è stringa vuota??
					//altrimenti l'app prende quello di default da prolog?
						//NO --> commentare o eliminare da prolog
//				var addFoodStr = maitreResource!!.execAddFood(foodCode)
				println("CONTROLLER | sent add food with food-code $foodCode...")
				var addFoodStr = "Warning! The fridge doesn't contain the food required!"
				if (addFoodStr == "Warning! The fridge doesn't contain the food required!")
					viewmodel.addAttribute("warningStrRes", addFoodStr)
				
				// At next homepage load, to fill the add food output
				showFoodCodes(viewmodel)
				NextPage = "MaitreGUI_page2"
			}
			"Clear the Room" -> {
//				maitreResource!!.execClear("")
				println("CONTROLLER | sent clear...")
				NextPage = "MaitreGUI_page3"
				CurPage = NextPage
				Consulted = false
			}
			else -> throw Exception("No add food or clear button selected")			
		}
				
		return NextPage
	}
	
	@GetMapping("/consultStopTask")
	suspend fun  consultStopTask (viewmodel : Model,
								  @RequestParam(required=false) consultButton : String,
								  @RequestParam(required=false) stopButton : String) : String {
		
		if(consultButton == "Consult Room Resources") {
			println("CONTROLLER | managing consult button \"$consultButton\"...")
//			var ConsultStr = maitreResource.execConsult()
			println("CONTROLLER | sent consult...")
			var ConsultStr = "{fridge:\"[[s001,bread,15],[d001,water,12]]\"}+{dishwasher:\"[]\"}+{pantry:\"[[dishes,30],[glasses,30]]\"}+{table:\"[];[]\"}+"
			saveConsultRes(ConsultStr)
			
			// To fill the consult output
			stayConsulted(viewmodel)
			// Check of stop status to mantain its status on the web page
			if(Stopped)
				stayStopped(viewmodel)
		}
		
		when(stopButton) {
			"Stop Task" -> {
				//TODO: StopStr si ha solo se lo stop fallisce.
				//		Ottenere risposta anche in caso abbia successo: --> aggiornare model.qak
//				var StopStr = maitreResource!!.execStop()
				var StopStr = "There is NO activated task!"
				println("CONTROLLER | sent stop...")
				
				if (StopStr == "There is NO activated task!")
					viewmodel.addAttribute("stopStrRes", StopStr)
				else					
					// To put the page in stop mode
					stayStopped(viewmodel)
				
				// To keep the consult output on the page if it has already sent 
				if (Consulted)
					stayConsulted(viewmodel)
			}
			
			"Reactivate Task" -> {
//				maitreResource!!.execReactivate()
				println("CONTROLLER | sent reactivate...")
				
				viewmodel.addAttribute("stopValue", "Stop Task")
				viewmodel.addAttribute("disableEl", false)
				Stopped = false
			
				// To keep the consult output on the page if it has already sent 
				if (Consulted)
					stayConsulted(viewmodel)
			}
		}
				
		return CurPage
	}
	
//	//TODO: eventualmente potremmo fare una pag @GetMapping("/settings") che possa gestire
	//		il cambio addres, port, contesto, protocollo e parametri di default per prapare e consumer

//############# UTILITY FUNCTIONS #############//

	// Function to split the resulting string from consult command, to obtain a string for each resource
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
	
	// To fill the consult output
	fun showConsult(viewmodel : Model) {
//		var ConsultRes = "Pantry: ${checkEl(PantryEl)}\n"
//		ConsultRes += "Fridge: ${checkEl(FridgeEl)}\n"
//		ConsultRes += "Dishwasher: ${checkEl(DishwasherEl)}\n"
//		ConsultRes += "Table: ${checkEl(TableDishes)} and ${checkEl(TableFood)}"
//		println("CONTROLLER | Consult result:$ConsultRes")
//		viewmodel.addAttribute("consultRes", ConsultRes)
		
		var ConsultRes2 = arrayListOf<String>()
		ConsultRes2.add("Pantry: ${checkEl(PantryEl)}\n")
		ConsultRes2.add("Fridge: ${checkEl(FridgeEl)}\n")
		ConsultRes2.add("Dishwasher: ${checkEl(DishwasherEl)}\n")
		ConsultRes2.add("Table: ${checkEl(TableDishes)} and ${checkEl(TableFood)}")
		viewmodel.addAttribute("consultRes2", ConsultRes2)
		
		//TODO: - elimino la textarea o lascio commentata?
		//		- visto che con thymeleaf posso gestire tutto.. questa parte la faccio su html?
		//		- o tutto qua? divido quindi gli elementi? "pantry", "elementi".. etc?
		//		- il check lo faccio quindi su html?
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
				//TODO: mettere min a 1 invece che a 0
						//--> si può togliere il controllo se si mette a 1
						//--> Ma se si mette min=1 => è obbligatorio scegliere una quantità
												 //=> lasciare min=0 ???
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
					//TODO: mettere min a 1 invece che a 0??
			}
		}		
		println("CONTROLLER | Food selected: $Food...")
		
		//TODO: commentare
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
		viewmodel.addAttribute("stopValue", "Reactivate Task")
		viewmodel.addAttribute("disableEl", true)
		Stopped = true
	}
	
	// To keep the consult output on the page if it has already sent 
	fun stayConsulted(viewmodel : Model) {
		viewmodel.addAttribute("consulthiddenAttr", false)
		viewmodel.addAttribute("consultOpenAttr", true)
		showConsult(viewmodel)
		Consulted = true
	}
}