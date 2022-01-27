%% The table is initially empty 
%% (also at the end of the "Clear the room" task); 
%% but at the end of the "Prepare the room" task 
%% (and eventually at the and of "Add food" task)
%% it will contain:
%% - some crockeries (with their names and quantities specified):
%%   crockery(NAME, QUANTITY);
%% - some food (with their codes, names and quantities specified):
%% 	 food(CODE, NAME, QUANTITY).

%% ONLY for "https://swish.swi-prolog.org/"
%:- dynamic (crockery/2, food/3).
%crockery(empty, 0).
%food(empty, empty, 0).

%% To obtain a list of crockery
getCrockery(Res) :-
    findall(crockery(NAME, QUANTITY), crockery(NAME, QUANTITY), Res).
%% To obtain a list of crockery elements
getCrockeryEl(Res) :-
    findall([NAME, QUANTITY], crockery(NAME, QUANTITY), Res).
%% To obtain the crockery by its name
getCrockeryByName(NAME, Res) :-
    findall(crockery(NAME, QUANTITY), crockery(NAME, QUANTITY), Res).
%% To obtain the crockery element by its name
getCrockeryElByName(NAME, Res) :-
    findall([NAME, QUANTITY], crockery(NAME, QUANTITY), Res).  

%% To obtain a list of foods    
getFood(Res) :-
    findall(food(CODE, NAME, QUANTITY), food(CODE, NAME, QUANTITY), Res).    
%% To obtain a list of foods elements
getFoodEl(Res) :-
    findall([CODE, NAME, QUANTITY], food(CODE, NAME, QUANTITY), Res). 
%% To obtain the food by its code and name
getFoodByCodeAndName(CODE, NAME, Res) :-
    findall(food(CODE, NAME, QUANTITY), food(CODE, NAME, QUANTITY), Res).
%% To obtain the food element by its code and name
getFoodElByCodeAndName(CODE, NAME, Res) :-
    findall([CODE, NAME, QUANTITY], food(CODE, NAME, QUANTITY), Res). 

%% To obtain a list of crockery and a list of foods
getAll(ResCrockery, ResFood) :-
	getCrockery(ResCrockery),
    getFood(ResFood). 
%% To obtain a list of crockery elements and a list of food elements
getAllEl(ResCrockery, ResFood) :-
	getCrockeryEl(ResCrockery),
    getFoodEl(ResFood).
%% To obtain a list of two list: crockery list and foods list
getAll([H, T]) :-
	getCrockery(H),
    getFood(T).      
%% To obtain a list of two list: crockery elements list and food elements list
getAllEl([H, T]) :-
	getCrockeryEl(H),
    getFoodEl(T).

%% To add some elements to the table
add([]).
%% To add elements that are crockery
	%% if the element is already present 
	%% --> sum of their quantity to update the element
add([[NAME, QUANTITY] | T]) :-
    checkAllCrockeryByName([[NAME, QUANTITY] | T]), !,
    getCrockeryElByName(NAME, [[NAME, QUANTITYel] | _T]),
    QuantityNew is QUANTITYel + QUANTITY,
    assertz(crockery(NAME, QuantityNew)),
    retractall(crockery(NAME, QUANTITYel)),
    add(T).
	%% else --> add new element
add([[NAME, QUANTITY] | T]) :-
    assertz(crockery(NAME, QUANTITY)),
    add(T).
%% To add elements that are foods
	%% if the element is already present 
	%% --> sum of their quantity to update the element
add([[CODE, NAME, QUANTITY] | T]) :-
    checkAllFoodByCodeAndName([[CODE, NAME, QUANTITY] | T]), !,
    getFoodElByCodeAndName(CODE, NAME, [[CODE, NAME, QUANTITYel] | _T]),
    QuantityNew is QUANTITYel + QUANTITY,
    assertz(food(CODE, NAME, QuantityNew)),
    retractall(food(CODE, NAME, QUANTITYel)),
    add(T).
	%% else --> add new element
add([[CODE, NAME, QUANTITY] | T]) :-
    assertz(food(CODE, NAME, QUANTITY)),
    add(T).

%% To remove some elements from the table 
%% only if all they exist in less quantity then the available
remove([]).
%% To remove elements that are crockery
	%% if the element is already present 
	%% --> subtraction of their quantity to update the element
remove([[NAME, QUANTITY] | T]) :-
	checkAllCrockeryByName([[NAME, QUANTITY] | T]), !,
	checkQuantity([[NAME, QUANTITY] | T]), !,
	removeEl([[NAME, QUANTITY] | T]). 
%% To remove elements that are foods
	%% if the element is already present 
	%% --> subtraction of their quantity to update the element
remove([[CODE, NAME, QUANTITY] | T]) :-
	checkAllFoodByCodeAndName([[CODE, NAME, QUANTITY] | T]), !,
	checkQuantity([[CODE, NAME, QUANTITY] | T]), !,
	removeEl([[CODE, NAME, QUANTITY] | T]).
%% To remove some elements
removeEl([]).
	%% of crockery
removeEl([[NAME, QUANTITY] | T]) :-
	getCrockeryElByName(NAME, [[NAME, QUANTITYel] | _T]),
    QuantityNew is QUANTITYel - QUANTITY,
    assertz(crockery(NAME, QuantityNew)),
    retractall(crockery(NAME, QUANTITYel)),
	removeEl(T).
	%% of food
removeEl([[CODE, NAME, QUANTITY] | T]) :-
	getFoodElByCodeAndName(CODE, NAME, [[CODE, NAME, QUANTITYel] | _T]),
    QuantityNew is QUANTITYel - QUANTITY,
    assertz(food(CODE, NAME, QuantityNew)),
    retractall(food(CODE, NAME, QUANTITYel)),
	removeEl(T).

%% To check if the quantity of each element is less then the available
checkQuantity([]).
	%% for crockery elements
checkQuantity([[NAME, QUANTITY] | T]) :-
    getCrockeryElByName(NAME, [[NAME, QUANTITYel] | _T]),
    QUANTITYel >= QUANTITY, !,
    checkQuantity(T).
	%% for food elements
checkQuantity([[CODE, NAME, QUANTITY] | T]) :-
    getFoodElByCodeAndName(CODE, NAME, [[CODE, NAME, QUANTITYel] | _T]),
    QUANTITYel >= QUANTITY, !,
    checkQuantity(T).
%% To check if the name of each crockery element is present
checkAllCrockeryByName([]).
checkAllCrockeryByName([[NAME, _QUANTITY] | T]) :-
    checkCrockeryByName(NAME),
    checkAllCrockeryByName(T).
%% To check if the crockery defined by its name is present
checkCrockeryByName(NAME) :-
    getCrockeryEl(Crockery),
    member([NAME, _QUANTITY], Crockery), !.
%% To check if the element El is in the list
member(El,[El|_]).
member(El,[_|T]):- 
    member(El,T).
%% To check if the code and name of each food element is present
checkAllFoodByCodeAndName([]).
checkAllFoodByCodeAndName([[CODE, NAME, _QUANTITY] | T]) :-
    checkFoodByCodeAndName(CODE, NAME),
    checkAllFoodByCodeAndName(T).
%% To check if the food defined by its code and name is present
checkFoodByCodeAndName(CODE, NAME) :-
    getFoodEl(Food),
    member([CODE, NAME, _QUANTITY], Food), !.