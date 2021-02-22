package senberg.portals.stargate.calculator;

import java.io.Serializable;

public class CalculatorResponse implements Serializable {
    int result;

    public CalculatorResponse(int result) {
        this.result = result;
    }
}
