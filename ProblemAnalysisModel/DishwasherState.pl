%% The dishwasher is initially empty; 
%% but at the end of the "Clear the room" task 
%% it will contain some crockery
%% (with their names and quantities specified):
%% crockery(NAME, QUANTITY).

%% ONLY for "https://swish.swi-prolog.org/"
%:- dynamic (crockery/2).
%crockery(empty, 0).

%% To obtain a list of crockery
getAll(Res) :-
    findall(crockery(NAME, QUANTITY), crockery(NAME, QUANTITY), Res).
%% To obtain a list of crockery elements
getAllEl(Res) :-
    findall([NAME, QUANTITY], crockery(NAME, QUANTITY), Res).
%% To obtain the crockery by its name
getByName(NAME, Res) :-
    findall(crockery(NAME, QUANTITY), crockery(NAME, QUANTITY), Res).
%% To obtain the crockery element by its name
getElByName(NAME, Res) :-
    findall([NAME, QUANTITY], crockery(NAME, QUANTITY), Res).  

%% To add some crockery elements
add([]).
	%% if the element is already present 
	%% --> sum of their quantity to update the element
add([[NAME, QUANTITY] | T]) :-
    checkAllByName([[NAME, QUANTITY] | T]), !,
    getElByName(NAME, [[NAME, QUANTITYel] | _T]),
    QuantityNew is QUANTITYel + QUANTITY,
    assertz(crockery(NAME, QuantityNew)),
    retractall(crockery(NAME, QUANTITYel)),
    add(T).
	%% else --> add new element
add([[NAME, QUANTITY] | T]) :-
    assertz(crockery(NAME, QUANTITY)),
    add(T).

%% To remove some elements of crockery 
%% only if all they exist in less quantity then the available
remove(CrockeryEl) :-
	checkAllByName(CrockeryEl), !,
	checkQuantity(CrockeryEl), !,
	removeEl(CrockeryEl).
%% To remove some elements of crockery
removeEl([]).
removeEl([[NAME, QUANTITY] | T]) :-
	getElByName(NAME, [[NAME, QUANTITYel] | _T]),
    QuantityNew is QUANTITYel - QUANTITY,
    assertz(crockery(NAME, QuantityNew)),
    retractall(crockery(NAME, QUANTITYel)),
	removeEl(T).

%% To check if the quantity of each element is less then the available
checkQuantity([]).
checkQuantity([[NAME, QUANTITY] | T]) :-
    getElByName(NAME, [[NAME, QUANTITYel] | _T]),
    QUANTITYel >= QUANTITY, !,
    checkQuantity(T).
%% To check if the name of each element is present
checkAllByName([]).
checkAllByName([[NAME, _QUANTITY] | T]) :-
    checkByName(NAME),
    checkAllByName(T).
%% To check if the crockery defined by its name is present
checkByName(NAME) :-
    getAllEl(Crockery),
    member([NAME, _QUANTITY], Crockery), !.
%% To check if the element El is in the list
member(El,[El|_]).
member(El,[_|T]):- 
    member(El,T).