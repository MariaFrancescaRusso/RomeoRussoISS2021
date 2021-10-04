%====================================================================================
% standingbuffetservice description   
%====================================================================================
context(ctxsystem, "localhost",  "TCP", "8040").
 qactor( rbr, ctxsystem, "it.unibo.rbr.Rbr").
  qactor( maitre, ctxsystem, "it.unibo.maitre.Maitre").
  qactor( pantry, ctxsystem, "it.unibo.pantry.Pantry").
  qactor( table, ctxsystem, "it.unibo.table.Table").
  qactor( dishwasher, ctxsystem, "it.unibo.dishwasher.Dishwasher").
  qactor( fridge, ctxsystem, "it.unibo.fridge.Fridge").
