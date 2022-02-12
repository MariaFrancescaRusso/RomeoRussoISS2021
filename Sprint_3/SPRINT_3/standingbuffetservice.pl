%====================================================================================
% standingbuffetservice description   
%====================================================================================
context(ctxmaitre, "192.168.1.171",  "TCP", "8070").
context(ctxfridge, "localhost",  "TCP", "8060").
context(ctxrbr, "192.168.1.171",  "TCP", "8050").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( maitre, ctxmaitre, "it.unibo.maitre.Maitre").
  qactor( foodconsumer, ctxmaitre, "it.unibo.foodconsumer.Foodconsumer").
  qactor( rbr, ctxrbr, "it.unibo.rbr.Rbr").
  qactor( rbrmapper, ctxrbr, "it.unibo.rbrmapper.Rbrmapper").
  qactor( rbrwalker, ctxrbr, "it.unibo.rbrwalker.Rbrwalker").
  qactor( pantry, ctxmaitre, "it.unibo.pantry.Pantry").
  qactor( table, ctxmaitre, "it.unibo.table.Table").
  qactor( dishwasher, ctxmaitre, "it.unibo.dishwasher.Dishwasher").
  qactor( fridge, ctxfridge, "it.unibo.fridge.Fridge").
