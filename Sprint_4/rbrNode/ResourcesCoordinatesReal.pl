%% Goal coordinates near the resources that the RBR can achieve.
%% There are the resources with their coordinates (X = columns; Y = rows) 
%% and the rbr direction to be in front of the resources in a map 7x7:
%% rh(X, Y, DIRECTION).
%% pantry(X, Y, DIRECTION).
%% fridge(X, Y, DIRECTION).
%% table(X, Y, DIRECTION).
%% dishwasher(X, Y, DIRECTION).

%% ONLY for "https://swish.swi-prolog.org/"
%:- dynamic (rh/2, pantry/2, fridge/2, table/2, dishwasher/2).

rh(0, 0, downDir).

pantry(0, 4, downDir).
pantry(1, 4, downDir).

table(2, 1, downDir).
table(1, 2, rightDir).
table(3, 2, leftDir).
table(2, 3, upDir).

fridge(3, 0, upDir).
fridge(4, 0, upDir).

dishwasher(3, 4, downDir).
dishwasher(4, 4, downDir).

%% To obtain a list of pantry
getPantryCoordinate(Res) :-
    findall(pantry(X, Y), pantry(X, Y, _DIR), Res).
%% To obtain a list of pantry coordinate element
getPantryCoordinateEl(Res) :-
    findall([X, Y], pantry(X, Y, _DIR), Res).
%% To obtain a list of pantry coordinate element with the rbr direction
getPantryCoordinateAndDirEl(Res) :-
    findall([X, Y, DIR], pantry(X, Y, DIR), Res).
%% To obtain the rbr direction for pantry goal
getPantryDir(X, Y, DIR) :-
    findall(DIR, pantry(X, Y, DIR), [DIR | _T]).
%% To obtain the list of nearest pantry goal coordinates from the current position 
getListPantryFromCurPosCoordinateEl(Cp, Goal) :-
    getPantryCoordinateEl(Res),
    searchNearestGoal(Cp, Res, ResGoal),
   	sortDis(ResGoal, Goal).
%% To obtain the nearest pantry goal coordinates from the current position 
getPantryFromCurPosCoordinateEl(Cp, XYgoal) :-
    getPantryCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, XYgoal).
%% To obtain the nearest pantry goal XY coordinates from the current position 
getPantryFromCurPosXYCoordinate(Cp, Xgoal, Ygoal) :-
    getPantryCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]).
%% To obtain the nearest pantry goal 
%% XY coordinates from the current position and the rbr direction
getPantryFromCurPosXYCoordinateAndDir(Cp, Xgoal, Ygoal, Dir) :-
    getPantryCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]),
    getPantryDir(Xgoal, Ygoal, Dir).

%% To obtain a list of rh
getRHCoordinate(Res) :-
    findall(rh(X, Y), rh(X, Y, _DIR), Res).
%% To obtain a list of rh coordinate element
getRHListCoordinate(Res) :-
    findall([X, Y], rh(X, Y, _DIR), Res).
%% To obtain the rh coordinate element,
%% assuming that RH is composed by a sigle cell
getRHCoordinateEl(Res) :-
    findall([X, Y], rh(X, Y, _DIR), [Res | _Tres]).
%% To obtain a list of rh coordinate element with the rbr direction
getRHCoordinateAndDirEl(Res) :-
    findall([X, Y, DIR], rh(X, Y, DIR), Res).
%% To obtain the coordinates of rh,
%% assuming that RH is composed by a sigle cell
getRHXYCoordinates(Xres, Yres) :-
    findall([X, Y], rh(X, Y, _DIR), [[Xres, Yres] | _Tres]).
%% To obtain the coordinates of rh,
%% assuming that RH is composed by a sigle cell
getRHXYCoordinatesAndDir(Xres, Yres, DirRes) :-
    findall([X, Y, DIR], rh(X, Y, DIR), [[Xres, Yres, DirRes] | _Tres]).
%% To obtain the rbr direction for rh goal
getRHDir(X, Y, DIR) :-
    findall(DIR, rh(X, Y, DIR), [DIR | _T]).
%% To obtain the list of nearest rh goal coordinates from the current position 
getListRHFromCurPosCoordinateEl(Cp, Goal) :-
    getRHCoordinateEl(Res),
    searchNearestGoal(Cp, Res, ResGoal),
   	sortDis(ResGoal, Goal).
%% To obtain the nearest RH goal coordinates from the current position 
getRHFromCurPosCoordinateEl(Cp, XYgoal) :-
    getRHListCoordinate(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, XYgoal).
%% To obtain the nearest RH goal XY coordinates from the current position 
getRHFromCurPosXYCoordinate(Cp, Xgoal, Ygoal) :-
    getRHListCoordinate(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]).
%% To obtain the nearest RH goal 
%% XY coordinates from the current position and the rbr direction
getRHFromCurPosXYCoordinateAndDir(Cp, Xgoal, Ygoal, Dir) :-
    getRHListCoordinate(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]),
    getRHDir(Xgoal, Ygoal, Dir).

%% To obtain a list of fridge
getFridgeCoordinate(Res) :-
    findall(fridge(X, Y), fridge(X, Y, _DIR), Res).
%% To obtain a list of fridge coordinate element
getFridgeCoordinateEl(Res) :-
    findall([X, Y], fridge(X, Y, _DIR), Res).
%% To obtain a list of fridge coordinate element with the rbr direction
getFridgeCoordinateAndDirEl(Res) :-
    findall([X, Y, DIR], fridge(X, Y, DIR), Res).
%% To obtain the rbr direction for fridge goal
getFridgeDir(X, Y, DIR) :-
    findall(DIR, fridge(X, Y, DIR), [DIR | _T]).
%% To obtain the list of nearest fridge goal coordinates from the current position 
getListFridgeFromCurPosCoordinateEl(Cp, Goal) :-
    getFridgeCoordinateEl(Res),
    searchNearestGoal(Cp, Res, ResGoal),
   	sortDis(ResGoal, Goal).
%% To obtain the nearest fridge goal coordinates from the current position 
getFridgeFromCurPosCoordinateEl(Cp, XYgoal) :-
    getFridgeCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, XYgoal).
%% To obtain the nearest fridge goal XY coordinates from the current position 
getFridgeFromCurPosXYCoordinate(Cp, Xgoal, Ygoal) :-
    getFridgeCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]).
%% To obtain the nearest fridge goal 
%% XY coordinates from the current position and the rbr direction
getFridgeFromCurPosXYCoordinateAndDir(Cp, Xgoal, Ygoal, Dir) :-
    getFridgeCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]),
    getFridgeDir(Xgoal, Ygoal, Dir).

%% To obtain a list of table
getTableCoordinate(Res) :-
    findall(table(X, Y), table(X, Y, _DIR), Res).
%% To obtain a list of table coordinate element
getTableCoordinateEl(Res) :-
    findall([X, Y], table(X, Y, _DIR), Res).
%% To obtain a list of table coordinate element with the rbr direction
getTableCoordinateAndDirEl(Res) :-
    findall([X, Y, DIR], table(X, Y, DIR), Res).
%% To obtain the rbr direction for table goal
getTableDir(X, Y, DIR) :-
    findall(DIR, table(X, Y, DIR), [DIR | _T]).
%% To obtain the list of nearest table goal coordinates from the current position 
getListTableFromCurPosCoordinateEl(Cp, Goal) :-
    getTableCoordinateEl(Res),
    searchNearestGoal(Cp, Res, ResGoal),
   	sortDis(ResGoal, Goal).
%% To obtain the nearest table goal coordinates from the current position 
getTableFromCurPosCoordinateEl(Cp, XYgoal) :-
    getTableCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, XYgoal).
%% To obtain the nearest table goal XY coordinates from the current position 
getTableFromCurPosXYCoordinate(Cp, Xgoal, Ygoal) :-
    getTableCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]).
%% To obtain the nearest table goal 
%% XY coordinates from the current position and the rbr direction
getTableFromCurPosXYCoordinateAndDir(Cp, Xgoal, Ygoal, Dir) :-
    getTableCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]),
    getTableDir(Xgoal, Ygoal, Dir).

%% To obtain a list of dishwasher
getDishwasherCoordinate(Res) :-
    findall(dishwasher(X, Y), dishwasher(X, Y, _DIR), Res).
%% To obtain a list of dishwasher coordinate element
getDishwasherCoordinateEl(Res) :-
    findall([X, Y], dishwasher(X, Y, _DIR), Res).
%% To obtain a list of dishwasher coordinate element with the rbr direction
getDishwasherCoordinateAndDirEl(Res) :-
    findall([X, Y, DIR], dishwasher(X, Y, DIR), Res).
%% To obtain the rbr direction for dishwasher goal
getDishwasherDir(X, Y, DIR) :-
    findall(DIR, dishwasher(X, Y, DIR), [DIR | _T]).
%% To obtain the list of nearest dishwasher goal coordinates from the current position 
getListDishwasherFromCurPosCoordinateEl(Cp, Goal) :-
    getDishwasherCoordinateEl(Res),
    searchNearestGoal(Cp, Res, ResGoal),
   	sortDis(ResGoal, Goal).
%% To obtain the nearest dishwasher goal coordinates from the current position 
getDishwasherFromCurPosCoordinateEl(Cp, XYgoal) :-
    getDishwasherCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, XYgoal).
%% To obtain the nearest dishwasher goal XY coordinates from the current position 
getDishwasherFromCurPosXYCoordinate(Cp, Xgoal, Ygoal) :-
    getDishwasherCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]).
%% To obtain the nearest dishwasher goal 
%% XY coordinates from the current position and the rbr direction
getDishwasherFromCurPosXYCoordinateAndDir(Cp, Xgoal, Ygoal, Dir) :-
    getDishwasherCoordinateEl(Res),
    searchNearestGoal(Cp, Res, [[Dis1, XY1] | TresGoal]),
   	min([Dis1, XY1], TresGoal, _DisMin, [Xgoal, Ygoal]),
    getDishwasherDir(Xgoal, Ygoal, Dir).

%% To search the nearest resource goal coordinates 
%% from the current position 
searchNearestGoal(_Cp, [], []).
searchNearestGoal((Xcp, Ycp), [[Xres, Yres] | Tres], [[Dis, [Xres, Yres]] | Tgoal]) :-
    Dis is abs(Xcp-Xres) + abs(Ycp-Yres),
	searchNearestGoal((Xcp, Ycp), Tres, Tgoal).

%% To sort a list of resource coordinates by the distance from the current position
sortDis([], []).
sortDis([[Dis1, XY1] | TresGoal], [XYmin | Tgoal]) :-
    min([Dis1, XY1], TresGoal, DisMin, XYmin),
    removeEl(DisMin, XYmin, [[Dis1, XY1] | TresGoal], Res),
    sortDis(Res, Tgoal).

%% To find the less distance between the resouce coordinates and the current position
%% and the corresponding element
min([DisMin, XYmin], [], DisMin, XYmin).
min([Dis1, XY1], [[Dis2, _XY2] | T], DisMin, XYmin) :-
    Dis1 =< Dis2, !,
    min([Dis1, XY1], T, DisMin, XYmin).
min([Dis1, _XY1], [[Dis2, XY2] | T], DisMin, XYmin) :-
    Dis1 > Dis2, !,
    min([Dis2, XY2], T, DisMin, XYmin).

%% To find the less distance between the resouce coordinates and the current position
minD(DisMin, [], DisMin).
minD(Dis1, [[Dis2, _XY2] | T], DisMin) :-
    Dis1 =< Dis2, !,
    minD(Dis1, T, DisMin).
minD(Dis1, [[Dis2, _XY2] | T], DisMin) :-
    Dis1 > Dis2, !,
    minD(Dis2, T, DisMin).

% To find the element with the given distance
findEl(DisMin, [[DisMin, XY1] | _T], XY1).
findEl(DisMin, [_El | T], Res):- 
    findEl(DisMin, T, Res).

% To remove the found element
removeEl(_DisMin, _XYmin, [], []).
removeEl(DisMin, XYmin, [[DisMin, XYmin] | TresGoal], TresGoal).
removeEl(DisMin, XYmin, [[Dis1, XY1] | TresGoal], [[Dis1, XY1] | Tres]) :-
    removeEl(DisMin, XYmin, TresGoal, Tres).
