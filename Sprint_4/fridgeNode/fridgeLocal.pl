%====================================================================================
% fridge description   
%====================================================================================
context(ctxmaitre, "172.20.128.3",  "TCP", "8070").
context(ctxrbr, "172.20.128.5",  "TCP", "8050").
context(ctxfridge, "localhost",  "TCP", "8060").
 qactor( rbr, ctxrbr, "external").
  qactor( maitre, ctxmaitre, "external").
  qactor( fridge, ctxfridge, "it.unibo.fridge.Fridge").
