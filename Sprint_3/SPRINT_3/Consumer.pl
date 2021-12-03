%% Food elements that will be consumed by people in the room
%% and that are represented with their codes, names and quantities to consume specified:
%% 	 food(CODE, NAME, QUANTITY).

%% ONLY for "https://swish.swi-prolog.org/"
%:- dynamic (food/3).

food(s001, bread, 1).
food(d001, water, 5).
food(s002, sandwich, 10).
food(d007, beer, 2).
food(d005, wine, 0.5).
food(k007, muffin, 5).

%% To obtain a list of foods    
getFood(Res) :-
    findall(food(CODE, NAME, QUANTITY), food(CODE, NAME, QUANTITY), Res).    
%% To obtain a list of foods elements
getFoodEl(Res) :-
    findall([CODE, NAME, QUANTITY], food(CODE, NAME, QUANTITY), Res). 