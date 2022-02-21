%% value that represents the step time duration:
%% 	 stepDuration(DURATION).

%% ONLY for "https://swish.swi-prolog.org/"
%:- dynamic (stepDuration/1).

%%HD
%stepDuration(650). 
%%FHD
%stepDuration(290).
%%Real
stepDuration(750).

%% To obtain the step time element
getDuration(Res) :-
    findall([DURATION], stepDuration(DURATION), [[Res] | []]). 
