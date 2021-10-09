%====================================================================================
% standingbuffetservice description   
%====================================================================================
context(ctxrbr, "127.0.0.1",  "TCP", "8050").
context(ctxfridge, "localhost",  "TCP", "8060").
context(ctxres, "localhost",  "TCP", "8080").
 qactor( rbr, ctxrbr, "it.unibo.rbr.Rbr").
  qactor( pantry, ctxres, "it.unibo.pantry.Pantry").
  qactor( table, ctxres, "it.unibo.table.Table").
  qactor( dishwasher, ctxres, "it.unibo.dishwasher.Dishwasher").
  qactor( fridge, ctxfridge, "it.unibo.fridge.Fridge").
