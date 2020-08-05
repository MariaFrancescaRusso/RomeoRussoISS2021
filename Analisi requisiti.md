(struttura interazione comportamento)
Domande:
1) chiedere se è necessario controllare l'ordine di esecuzione dei task.

## Glossario:
~***room*** = It's an environment in which the interactions between the elements happen. When system starts no people is in it, besides the maitre.~

***fridge*** = It's a smart device, having knowledge base on his storage, and a "Internet Thing". It's embedded into a wall of the **room** and it's filled with a proper set of items.

***pantry*** = It's a non-smart resource embedded into a wall of the **room** and it's filled with a proper set of items.

***dishwasher*** = It's a non-smart resource embedded into a wall of the **room** and it's empty when system starts.

***table*** = It's a non-smart resource put in the center of the **room**. 

***RBR*** *(Room Butler Robot)* = It's a DDR (Differential Drive Robot) able to work as a Room Butler. It can be a real or a virtual robot. 

**RH** *(Robot Home)* = It's the position in the **room** where the **RBR** boots up and where he comes back after every **task**.

***task*** = There are three possible tasks: **Prepare the room**, **Add food** and **Cleare the room**. 

***Prepare the room*** = This task consists in putting on the **table** dishes taken from the **pantry** and food taken from the **fridge**. The set of items to put on the table is fixed and it's properly described somewhere.

***Add food*** = This task consists in putting on the **table** some specific food taken from the **fridge** if it exists.

***Clear the room*** = This task consists in putting on the **dishwasher** and on **fridge** respectively dishes and non-consumed food taken from the **table**.

***food-code*** = Univoque code associated to a specific food.

***Maitre de Salle*** = Person who manages the **room** and its elements.

***maitre's smartphone*** = Smartphone application or a user-GUI in a browser used by **Maitre de Salle**.

~***CoAP***~ = 

## Requirement analysis
1. The software system is a distribuited system composed by three main entities: 
- **RBR**, running on its own **robotNode**;
- **maitre's smartphone**, running on a **maitreNode**;
- **fridge**, running on a **fridgeNode**. 

2. ~[To understand (or formally define) what is the meaning of the term robot, we must read UsingRobots2019.pdf]~

3. The software running on **maitreNode** must allow **Maitre de Salle** to:
- send the commands to **RBR** to execute tasks or to stop and reactive them.
- known and to show the objects related to each resources (smart and non-smart), so he must be able to comunicate with **fridge**.

4. The software running on **robotNode** must be able to:
- execute the **task** associated to command received by the **maitre's smartphone**. 
- stop/reactive a running **task** when it's required by the **maitre's smartphone**. 
- ask to **fridge** if it contains a specific food defined by a **food-code** and to receive the answer.
- send a warning to the **maitre's smartphone** when the **fridge** doesn't contain the food required.
- return to his **RH** at the end of every **task**.
- avoid the impact with mobile obstacles.

5. The software running on **fridgeNode** must be able to:
- answer to the **maitre's smartphone**, via CoAP, with his content when it's required.
- answer to the **RBR**, via CoAP, when it asks if it contains a specific food.

## A first model of the system Architecture

Comportamento:
	torna a posizione iniziale
	è in grado di eseguire i task senza collisioni 
	si ferma e riparte se riceve i comandi da parte del maitre

Comunication Maitre - RBR robotCmd : robotCmd(X) X=[**addFood(foodCode)**, clear, prepare, stop, restart]{
	command Task (prepare addfood(food-code) clear) 
	command handle (stop/restart)
	task ("Prepare the room" "Add food" "Clear the room")
}

Comunication Maitre - Fridge/Maitre : **consultCmd{**  
	command consult (ROOM)
	(Maitre knowns the state of non-smart elements, and asks to the smart devices to know their state)	
}

Comunication RBR - Maitre fail:fail(m) {
	fail if there isn't food=food-code in the fridge
}

Comunication RBR - Fridge ask:ask(foodCode) {
	command ask(food-code) after receiving command addfood(food-code) from Maitre
}

Comunication Fridge -  Maitre expose : expose(state) {
	command expose after receiving command consult from Maitre
}

Comunication Fridge - RBR answer:answer(Y) Y(Y=[yes,no]) {
	command answer after receiving command ask(food-code) from RBR
}

## Requirement tracibility
[achievied from the requirement analysis]
Using a meta-model written in a machine-understandble language, like QActor, we can obtain a rappresentation of the system architecture that describes the associations between requirements and the running elements (rappresented by the actors).

--------------------------------------------------------------------------------------------------
System StandingBuffetService

**Event 	 consult	: consult(state_room)	//from maitre**

Dispatch robotCmd	: robotCmd(X) 	 		//from maitre to robot 
Dispatch fail		: fail(m)			 	//from robot to maitre
Dispatch ask 		: ask(foodCode) 		//from robot to fridge
Dispatch answer 	: answer(Y)			 	//from fridge to robot
Dispatch expose		: expose(state)  		//from fridge to maitre

Context ctxRobotReq ip [host="localhost" port=8000] 

~//requirements: maitre interaction, prepare, add food, clear, stop, reactive ~
Qactor maitrereq context ctxRobotReq 	{/* Provides a user GUI; Forwards robotCmd to robotreq */}

~//requirements: move RBR avoiding abstacles, ask to fridge ~
Qactor robotreq context ctxRobotReq 	{/* Moves the robot and avoid obstacles; Handle robotCmd and answer; Forwards ask to fridgereq and fail to maitrereq */}

~//requirements: expose, answer ~
Qactor fridgereq context ctxRobotReq 	{/* Handle ask and consult; Forwards answer to robotreq and expose to maitrereq */}

## Test Plain
Test that the dispatcher works

## Problem analysis
The main goal of problem analysis can be summarized as follows:

1.	understand the (technical) problems posed by the requirements;
2.  identify the best tools/libraries/supports etc. necessary and/or useful for building the system;
3.	clarify the constraints (human, technical, economical, etc.) related to both the software product to build and the software production process;
4.	define a logical architecture of the system and a first working prototype to show to the customer at the end of our first sprint.

In the context of SCRUM, one or more of these steps will be redone in the sprint-review meetings. 
--------------------------------------------------------------
In our scenario:

1. The system is distribuited. To model it, we choose to use the **QAk-indrastracture** meta-model, developed and provided by our company, because it provides a built-in ~message-driven~ tecnology. In a first moment using the QAk we work just locally; to support the distribuited features we can introduce the **MQTT** and **web** tecnology and we must provide the **CoAP** support to the fridge comunications. 