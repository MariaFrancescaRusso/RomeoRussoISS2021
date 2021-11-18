%====================================================================================
% standingbuffetservice description   
%====================================================================================
context(ctxsystem, "localhost",  "TCP", "8040").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( maitre, ctxsystem, "it.unibo.maitre.Maitre").
  qactor( foodconsumer, ctxsystem, "it.unibo.foodconsumer.Foodconsumer").
  qactor( rbr, ctxsystem, "it.unibo.rbr.Rbr").
  qactor( rbrwalker, ctxsystem, "it.unibo.rbrwalker.Rbrwalker").
  qactor( pantry, ctxsystem, "it.unibo.pantry.Pantry").
  qactor( table, ctxsystem, "it.unibo.table.Table").
  qactor( dishwasher, ctxsystem, "it.unibo.dishwasher.Dishwasher").
  qactor( fridge, ctxsystem, "it.unibo.fridge.Fridge").
