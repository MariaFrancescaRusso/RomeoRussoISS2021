%% The fridge contains some food
%% (with their codes, names and quantities specified):
%% food(CODE, NAME, QUANTITY).

%% ONLY for "https://swish.swi-prolog.org/"
%:- dynamic (food/3).

food(s001, bread, 15).
food(d001, water, 12).
food(p003, pasta, 10).
food(s002, sandwich, 35).
food(d007, beer, 7).
food(d005, wine, 5).
food(k007, muffin, 30).
food(s005, salad, 10).

%% To obtain a list of food
getAll(Res) :-
    findall(food(CODE, NAME, QUANTITY), food(CODE, NAME, QUANTITY), Res).   
%% To obtain a list of food elements
getAllEl(Res) :-
    findall([CODE, NAME, QUANTITY], food(CODE, NAME, QUANTITY), Res).
%% To obtain the food by its code and name
getByCodeAndName(CODE, NAME, Res) :-
    findall(food(CODE, NAME, QUANTITY), food(CODE, NAME, QUANTITY), Res).
%% To obtain the food element by its code and name
getElByCodeAndName(CODE, NAME, Res) :-
    findall([CODE, NAME, QUANTITY], food(CODE, NAME, QUANTITY), Res). 
%% To obtain the food by its code
getFoodByCode(CODE, FoodRes) :-
    findall(food(CODE, NAME, QUANTITY), food(CODE, NAME, QUANTITY), FoodRes).
%% To obtain the food element by its code
getFoodElByCode(CODE, FoodRes) :-
    findall([CODE, NAME, QUANTITY], food(CODE, NAME, QUANTITY), FoodRes).

%% To add some elements
add([]).
	%% if the element is already present 
	%% --> sum of their quantity to update the element
add([[CODE, NAME, QUANTITY] | T]) :-
    checkAllByCodeAndName([[CODE, NAME, QUANTITY] | T]), !,
    getElByCodeAndName(CODE, NAME, [[CODE, NAME, QUANTITYel] | _T]),
    QuantityNew is QUANTITYel + QUANTITY,
    assertz(food(CODE, NAME, QuantityNew)),
    retractall(food(CODE, NAME, QUANTITYel)),
    add(T).
	%% else --> add new element
add([[CODE, NAME, QUANTITY] | T]) :-
    assertz(food(CODE, NAME, QUANTITY)),
    add(T).

%% To remove some foods
%% only if all they exist in less quantity then the available
remove(FoodEl) :-
	checkAllByCodeAndName(FoodEl), !,
	checkQuantity(FoodEl), !,
	removeEl(FoodEl).
%% To remove some foods
removeEl([]).
removeEl([[CODE, NAME, QUANTITY] | T]) :-
	getElByCodeAndName(CODE, NAME, [[CODE, NAME, QUANTITYel] | _T]),
    QuantityNew is QUANTITYel - QUANTITY,
    assertz(food(CODE, NAME, QuantityNew)),
    retractall(food(CODE, NAME, QUANTITYel)),
	removeEl(T).

%% To check if the quantity of each element is less then the available
checkQuantity([]).
checkQuantity([[CODE, NAME, QUANTITY] | T]) :-
    getElByCodeAndName(CODE, NAME, [[CODE, NAME, QUANTITYel] | _T]),
    QUANTITYel >= QUANTITY, !,
    checkQuantity(T).
%% To check if the code and name of each food element is present
checkAllByCodeAndName([]).
checkAllByCodeAndName([[CODE, NAME, _QUANTITY] | T]) :-
    checkFoodByCodeAndName(CODE, NAME),
    checkAllByCodeAndName(T).
%% To check if the food defined by its code and name is present
checkFoodByCodeAndName(CODE, NAME) :-
    getAllEl(Food),
    member([CODE, NAME, _QUANTITY], Food), !.
%% To check if the element El is in the list
member(El,[El|_]).
member(El,[_|T]):- 
    member(El,T).

%% To check if the food defined by its code is present in the fridge
checkFoodByCode(CODE, [[CODE, NAMEel, 1]]) :-
    getAllEl(Foods),
    member([CODE, _NAME, _QUANTITY], Foods), !,
    getFoodElByCode(CODE, [[CODE, NAMEel, QUANTITYel] | _T]),
    QUANTITYel > 0, !.
%checkFoodByCode(_CODE, [[]]).