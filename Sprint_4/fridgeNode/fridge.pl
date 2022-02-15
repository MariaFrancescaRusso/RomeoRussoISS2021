%====================================================================================
% fridge description   
%====================================================================================
context(ctxmaitre, "192.168.1.211",  "TCP", "8070").
context(ctxrbr, "192.168.1.93",  "TCP", "8050").
context(ctxfridge, "localhost",  "TCP", "8060").
 qactor( fridge, ctxfridge, "it.unibo.fridge.Fridge").
