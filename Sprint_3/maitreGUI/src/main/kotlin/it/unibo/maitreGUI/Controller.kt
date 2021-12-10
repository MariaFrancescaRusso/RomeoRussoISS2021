package it.unibo.maitreGUI

//import com.andreapivetta.kolor.Color
//import it.unibo.actor0.sysUtil
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.web.bind.annotation.*


@Controller
class Controller {
    @Value("\${spring.application.name}")
    var appName: String?    = null
    
//	var applicationModelRep = "waiting"
//    init{
//        RobotResource.initRobotResource() //we want a local BasicStepRobotActor
//    }

//    @GetMapping("/")    //defines that the method handles GET requests
//    fun homePage(model: Model): String {
//    fun entry(model: Model): String {
//        model.addAttribute("arg", appName+"xxx")
//        println("HumanInterfaceController | entry model=$model")
//        return "naiveRobotGui"
//    }

//    @ExceptionHandler
//    fun handle(ex: Exception): ResponseEntity<*> {
//        val responseHeaders = HttpHeaders()
//        return ResponseEntity(
//            "BaseController ERROR ${ex.message}", 
//			responseHeaders, HttpStatus.CREATED
//        )
//    }
}