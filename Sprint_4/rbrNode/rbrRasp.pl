%====================================================================================
% rbr description   
%====================================================================================
context(ctxmaitre, "192.168.43.211",  "TCP", "8070").
context(ctxfridge, "192.168.43.157",  "TCP", "8060").
context(ctxrbr, "localhost",  "TCP", "8050").
context(ctxbasicrobot, "192.168.43.228",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( pantry, ctxmaitre, "external").
  qactor( table, ctxmaitre, "external").
  qactor( dishwasher, ctxmaitre, "external").
  qactor( fridge, ctxfridge, "external").
  qactor( rbr, ctxrbr, "it.unibo.rbr.Rbr").
  qactor( rbrmapper, ctxrbr, "it.unibo.rbrmapper.Rbrmapper").
  qactor( rbrwalker, ctxrbr, "it.unibo.rbrwalker.Rbrwalker").
