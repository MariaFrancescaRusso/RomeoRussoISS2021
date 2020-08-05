# Requirement analysis
1. Our software system is a distribuited system composed by three main entities: 
- Room Butler Robot (**RBR**), running on its own **robotNode** and that can be a real or a virtual robot;
- Maitre de salle (**maitre**), running on a **maitreNode** and that can be a smartphone application or a user-GUI in a browser;
- Smart-Fridge (**fridge**), running on a **fridgeNode**. 
3. [To understand (or formally define) what is the meaning of the term robot, we must read UsingRobots2019.pdf]
2. The software running on **maitreNode** must allow Maitre de sall to:
- send the commands to **RBR** to execute tasks or to stop and reactive them.
- to known the object related to each resources (smart and non-smart), so he must be able to comunicate with **fridge**.

4. The software running on **robotNode** must be able to:
- execute the **task** associated to command received by the **maitre**. 
- return to his *start position* at the end of every **task**.
- comunicate with **fridge** and **maitre**. **-------------------------------------------**
- avoid the impact with mobile obstacles (e.g. a sonar, a camera, a proximity sensor).

5. The software running on **fridgeNode** must be able to:
- comunicate with **robot** and **maitre**. **-------------------------------------------**

Comportamento:
	mappaggio (giro + mapping) ???
	torna a posizione iniziale ?? 
	è in grado di eseguire i task senza collisioni 
	si ferma e riparte se riceve i comandi da parte del maitre

	
Comunication Maitre - RBR 
	command Task (prepare addfood(food-code) clear) 
	command handle (stop/restart)
	task ("Prepare the room" "Add food" "Clear the room")

Comunication Maitre - Fridge/Maitre (Maitre knowns the state of non-smart elements, and asks to the smart devices to know their state)
	command consult (ROOM)


Comunication RBR - Maitre
	warning if there isn't food=food-code in the fridge

Comunication RBR - Fridge
	command ask(food-code) after receiving command addfood(food-code) from Maitre

Comunication Fridge -  Maitre
	command expose after receiving command consult from Maitre

Comunication Fridge - RBR
	command answer after receiving command ask(food-code) from RBR



----------------------------------------------------------------
Appunti da ste: il prof nell'analisi dei requisiti dice 
"The software running on the robotNode must execute the commands sent by the **human-user** via the console and must be sensible to a set of conditions. Let us name, from now on, such a set with the term envConds."
Noi dovremmo dire che deve eseguire i comandi inviati dal maitre via smartphone ?

Guarda lab12 per vedere come affronta lui i 3 Sprint 
What
