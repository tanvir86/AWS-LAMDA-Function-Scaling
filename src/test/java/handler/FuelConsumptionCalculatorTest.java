package handler;

import dto.RequestData;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class FuelConsumptionCalculatorTest {
  private static final Logger logger = LoggerFactory.getLogger(FuelConsumptionCalculatorTest.class);

  @Test
  void fuelConsumptionCalculatorTest() {
    logger.info("FuelConsumptionCalculator TEST");
    RequestData event = new RequestData();
    Context context = new TestContext();
    FuelConsumptionCalculator handler = new FuelConsumptionCalculator();
    RequestData result = handler.handleRequest(event, context);
    assertNotNull(result);
  }

}
