package senberg.portals.stargate.calculator;

import java.io.Serializable;

public class CalculatorAddRequest implements Serializable {
    int a;
    int b;

    public CalculatorAddRequest(int a, int b) {
        this.a = a;
        this.b = b;
    }
}
