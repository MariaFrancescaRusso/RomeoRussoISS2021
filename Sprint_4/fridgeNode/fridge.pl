%====================================================================================
% fridge description   
%====================================================================================
context(ctxmaitre, "192.168.43.157",  "TCP", "8070").
context(ctxrbr, "192.168.43.228",  "TCP", "8050").
context(ctxfridge, "localhost",  "TCP", "8060").
 qactor( rbr, ctxrbr, "external").
  qactor( maitre, ctxmaitre, "external").
  qactor( pantry, ctxmaitre, "external").
  qactor( table, ctxmaitre, "external").
  qactor( dishwasher, ctxmaitre, "external").
  qactor( fridge, ctxfridge, "it.unibo.fridge.Fridge").
