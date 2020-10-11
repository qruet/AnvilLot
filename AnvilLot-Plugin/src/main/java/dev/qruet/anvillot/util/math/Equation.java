package dev.qruet.anvillot.util.math;

import dev.qruet.anvillot.util.java.Pair;
import dev.qruet.anvillot.util.math.function.Max;
import org.nfunk.jep.JEP;

import java.util.Arrays;

public class Equation {

    private static final JEP parser = new JEP();

    static {
        parser.addStandardFunctions();
        parser.addStandardConstants();
        parser.addFunction("max", new Max());
    }

    public static double evaluate(String equation, Pair<String, Double>... paramters) {
        Arrays.asList(paramters).forEach(p -> {
            parser.addVariable(p.getKey(), p.getValue());
        });
        parser.parseExpression(equation);
        return parser.getValue();
    }

}
