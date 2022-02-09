%% value that represents the robot type ( VALUE = [virtual,real] ):
%% 	 robotType(VALUE).

%% ONLY for "https://swish.swi-prolog.org/"
%:- dynamic (robotType/1).

robotType(virtual).
%robotType(real).

%% To obtain the value element
getValue(Res) :-
    findall([VALUE], robotType(VALUE), [[Res] | []]). 