%% Boolean value of map represented with a boolean value which is
%% true if the room has been already mapped, atherwise false:
%% 	 isMap(VALUE).

%% ONLY for "https://swish.swi-prolog.org/"
%:- dynamic (isMap/1).

isMap(false).

%% To obtain the value element
getValue(Res) :-
    findall([VALUE], isMap(VALUE), [[Res] | []]). 