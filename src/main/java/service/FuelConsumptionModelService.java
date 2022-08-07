package service;

import java.util.HashMap;

public class FuelConsumptionModelService {
    HashMap<Integer, HashMap<String, Double>> linearRegressionModel;

    public FuelConsumptionModelService() {
        this.linearRegressionModel = new HashMap<Integer, HashMap<String, Double>>();

        HashMap<String, Double> model1 = new HashMap<>();
        model1.put("draught",0.79768868);
        model1.put("speed",4.56864941);
        model1.put("beaufort",1.70652511);
        model1.put("intercept",-42.60865707951619);
        linearRegressionModel.put(234567,model1);

        HashMap<String, Double> model21 = new HashMap<>();
        model21.put("draught",0D);
        model21.put("speed",4.52292442);
        model21.put("beaufort",1.80399462);
        model21.put("intercept",-15.470391433671104);
        linearRegressionModel.put(123456,model21);

        HashMap<String, Double> model22 = new HashMap<>();
        model22.put("draught",0.4063522);
        model22.put("speed",4.57511598);
        model22.put("beaufort",1.56973988);
        model22.put("intercept",-18.570543879610995);
        linearRegressionModel.put(9682978,model21);

        HashMap<String, Double> model3 = new HashMap<>();
        model3.put("draught",0.46995447);
        model3.put("speed",2.64914373);
        model3.put("beaufort",1.26146968);
        model3.put("intercept",-17.354305063674307);
        linearRegressionModel.put(345678,model3);

        HashMap<String, Double> model4 = new HashMap<>();
        model4.put("draught",1.62773863 );
        model4.put("speed",3.86938836);
        model4.put("beaufort",1.65737816);
        model4.put("intercept",-37.816608573472436);
        linearRegressionModel.put(456789,model4);
    }

    public Boolean isModelExist(Integer imo){
        return this.linearRegressionModel.get(imo) != null;
    }

    public Double getFuelConsumption(Integer imo,Float draught, Double speed, Float beaufort){
        HashMap<String, Double> model = this.linearRegressionModel.get(imo);

        return model.get("intercept") + model.get("draught")*draught + model.get("speed")*speed + model.get("beaufort")*beaufort;
    }
}
