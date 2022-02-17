%====================================================================================
% maitre description   
%====================================================================================
context(ctxmaitre, "localhost",  "TCP", "8070").
context(ctxfridge, "172.20.128.2",  "TCP", "8060").
context(ctxrbr, "172.20.128.5",  "TCP", "8050").
 qactor( rbr, ctxrbr, "external").
  qactor( rbrwalker, ctxrbr, "external").
  qactor( fridge, ctxfridge, "external").
  qactor( maitre, ctxmaitre, "it.unibo.maitre.Maitre").
  qactor( foodconsumer, ctxmaitre, "it.unibo.foodconsumer.Foodconsumer").
  qactor( pantry, ctxmaitre, "it.unibo.pantry.Pantry").
  qactor( table, ctxmaitre, "it.unibo.table.Table").
  qactor( dishwasher, ctxmaitre, "it.unibo.dishwasher.Dishwasher").
