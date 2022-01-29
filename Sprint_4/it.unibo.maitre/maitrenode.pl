%====================================================================================
% maitrenode description   
%====================================================================================
context(ctxmaitre, "localhost",  "TCP", "8070").
context(ctxfridge, "localhost",  "TCP", "8060").
context(ctxrbr, "localhost",  "TCP", "8050").
 qactor( fridge, ctxfridge, "external").
  qactor( rbr, ctxrbr, "external").
  qactor( rbrwalker, ctxrbr, "external").
  qactor( maitre, ctxmaitre, "it.unibo.maitre.Maitre").
  qactor( foodconsumer, ctxmaitre, "it.unibo.foodconsumer.Foodconsumer").
  qactor( pantry, ctxmaitre, "it.unibo.pantry.Pantry").
  qactor( table, ctxmaitre, "it.unibo.table.Table").
  qactor( dishwasher, ctxmaitre, "it.unibo.dishwasher.Dishwasher").
