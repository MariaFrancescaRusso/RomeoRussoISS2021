%% Initial state for the PREPARE THE ROOM task.
%% There are:
%% - some crockeries (with their names and quantities specified):
%%   crockery(NAME, QUANTITY);
%% - some food (with their codes, names and quantities specified):
%% 	 food(CODE, NAME, QUANTITY).

%% ONLY for "https://swish.swi-prolog.org/"
%:- dynamic (crockery/2, food/3).

crockery(dishes, 10).
crockery(glasses, 10).

food(s001, bread, 10).
food(d001, water, 10).
food(p003, pasta, 10).
food(s002, sandwich, 20).
food(d007, beer, 5).
food(d005, wine, 3).
food(k007, muffin, 20).
food(s005, salad, 10).

%% Initial state for the ADD FOOD task:
%% foodCode(FOODCODE).

%% Existing food_code that isn't available
%foodCode(p003).	
%% Not existing food_code
foodCode(c034).
%% Existing and available food_code
%foodCode(s001).

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
    
%% To obtain a list of food_code
getFoodCode(Res) :-
	findall(foodCode(FOOD_CODE), foodCode(FOOD_CODE), Res).
%% To obtain a list of food_code elements
getFoodCodeEl(Res) :-
    findall(FOOD_CODE, foodCode(FOOD_CODE), Res).