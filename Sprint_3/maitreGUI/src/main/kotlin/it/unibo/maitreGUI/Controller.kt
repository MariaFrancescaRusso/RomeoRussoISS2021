package it.unibo.maitreGUI

//import it.unibo.connQak.ConnectionType
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.bind.annotation.RequestMapping
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
	//TODO: se mettiamo questa annotazione sopra al controller,
	//		allora non c'è bisogno di scriverla sopra alle altre funzioni
	@kotlinx.coroutines.ObsoleteCoroutinesApi
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
	
		
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/")
	suspend fun home(viewmodel : Model) : String {
		// At homepage load, a consult command is sent
//		var ConsultStr = maitreResource.execConsult()
		var ConsultStr = "{fridge:\"[[s001,bread,15],[d001,water,12]]\"}+{dishwasher:\"[]\"}+{pantry:\"[[dishes,30],[glasses,30]]\"}+{table:\"[];[]\"}+"
		saveConsultRes(ConsultStr)
		
		// To fill the prepare selection //for the homepage "MaitreGUI" diciamo per quale pagina??
		showPrepareEl(viewmodel)
		
		// To fill the consult output
		showConsult(viewmodel)
					
		return  "MaitreGUI"
	}
	
	//TODO: capire come caricare elementi all'avvio di una pagina che non sia la homepage
	
//	@RequestMapping("/MaitreGUI_page2")
//	@kotlinx.coroutines.ObsoleteCoroutinesApi
//	suspend fun home2() : ModelAndView {
//		println("CONTROLLER | starts page 2...")
//
//		var Page2 = ModelAndView("MaitreGUI_page2")
//		
//		// At homepage load, to fill the add food output for the page "MaitreGUI_page2"
//		showFoodCodes2(Page2)
//		Page2.setViewName("/MaitreGUI_page2")
//					
//		return  Page2
//	}
	
	@PostMapping("/MaitreGUI_page2")
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun home3(viewmodel : Model) : String {
		println("CONTROLLER | starts page 2...")
		
		// At homepage load, to fill the add food output //for the page "MaitreGUI_page2"
		showFoodCodes(viewmodel)
					
		return "MaitreGUI_page2"
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
				//TODO: fare controllo su array vuoto (se non è stato selezionato nulla)
				// 		e in caso usare quello di default??
				//soluzione: --> nella funziona viene controllato che l'array sia vuoto
				// 				 e in caso la stringa è ""
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
								   @RequestParam(required=false) foodCode : String) : String {
		var NextPage : String //o = ""
		
		//TODO: mettere il nome dei bottoni unico "addFoodClearButton"
		//		o due diversi ("addFoodButton", "clearButton")??
		println("CONTROLLER | managing prepare button \"$addFoodClearButton\"...")
		when(addFoodClearButton) {
			//TODO: leggere warning se c'è!!
			"Add Food" ->  {
				//TODO: stampa errore su pagina se foodCode è stringa vuota??
						//altrimenti l'app prende quello di default da prolog
				//		controllo anche che esista (nella lista del cibo nel frigo ottenuta prima del prepare)?
//				maitreResource!!.execAddFood(foodCode)
				println("CONTROLLER | sent add food with food-code $foodCode...")
				NextPage = "MaitreGUI_page2"
			}
			"Clear the Room" -> {
				//TODO: modificare MaitreResource --> il clear non ha argomenti!
//				maitreResource!!.execClear("")
				println("CONTROLLER | sent clear...")
				NextPage = "MaitreGUI_page3"
				CurPage = NextPage
				Consulted = false
			}
			else -> throw Exception("No add food or clear button selected")			
		}
				
		return "$NextPage"
	}
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@GetMapping("/consultStopTask")
	suspend fun  consultStopTask (viewmodel : Model,
								  @RequestParam(required=false) consultButton : String,
								  @RequestParam(required=false) stopButton : String) : String {
//		var NextPage = CurPage
		
		if(consultButton == "Consult Room Resources") {
			println("CONTROLLER | managing consult button \"$consultButton\"...")
//			maitreResource.execConsult()
			println("CONTROLLER | sent consult...")
			
			var ConsultStr = "{fridge:\"[[s001,bread,15],[d001,water,12]]\"}+{dishwasher:\"[]\"}+{pantry:\"[[dishes,30],[glasses,30]]\"}+{table:\"[];[]\"}+"
			//TODO: dato che è stato fatto un nuovo consult,
			//		allora vengono aggiornati i dati nel controller??
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
				//		Ottenere risposta anche in caso abbia successo??
//				var StopStr = maitreResource!!.execStop()
				var StopStr = ""
				println("CONTROLLER | sent stop...")
				
				if (StopStr == "There is NO activated task!")
					viewmodel.addAttribute("stopStrRes", StopStr)
				else					
					// To put the page in stop mode
					stayStopped(viewmodel)
				// To keep the consult output on the page if it has already sent 
				if (Consulted)
					stayConsulted(viewmodel)
				
				//TODO: fare una nuova pagina per lo stop??
//				NextPage = "MaitreGUI_page4"
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
				
//		return NextPage
		return CurPage
	}
	
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
//	//TODO: eventualmente potremmo fare una pag @GetMapping("/settings") che possa gestire
	//		il cambio addres, port, contesto, protocollo e parametri di default per prapare e consumer
	
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
		
		if(!Crockery.isEmpty())
			ResCrockery = Crockery.toString()
		
		if(!Food.isEmpty())
			ResFood = Food.toString()
			
		return listOf(ResCrockery, ResFood)
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
	
	// To fill the add fodd output
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun showFoodCodes2(viewmodel : ModelAndView) {
		var FoodList = ""
		
		for (el in FridgeEl) {
			FoodList += "${el.get(0)} - ${el.get(1)}\n"
		}

		viewmodel.addObject("foodList", FoodList)
	}
	
	// To put the page in stop mode
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun stayStopped(viewmodel : Model) {
		viewmodel.addAttribute("stopValue", "Reactivate Task")
		viewmodel.addAttribute("disableEl", true)
		Stopped = true
	}
	
	// To keep the consult output on the page if it has already sent 
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	fun stayConsulted(viewmodel : Model) {
		viewmodel.addAttribute("consulthiddenAttr", false)
		viewmodel.addAttribute("consultOpenAttr", true)
		showConsult(viewmodel)
		Consulted = true
	}
}
