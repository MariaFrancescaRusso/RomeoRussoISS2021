%====================================================================================
% standingbuffetservice description   
%====================================================================================
context(ctxrbr, "localhost",  "TCP", "8050").
context(ctxfridge, "127.0.0.1",  "TCP", "8060").
context(ctxmaitre, "192.168.43.157",  "TCP", "8070").
 qactor( rbr, ctxrbr, "it.unibo.rbr.Rbr").
  qactor( fridge, ctxfridge, "it.unibo.fridge.Fridge").
  qactor( maitre, ctxmaitre, "it.unibo.maitre.Maitre").
