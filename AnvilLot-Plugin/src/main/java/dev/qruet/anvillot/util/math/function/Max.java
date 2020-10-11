package dev.qruet.anvillot.util.math.function;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import java.util.Stack;

/**
 * @author qruet
 * Calculates maximum from two parameters
 */
public class Max extends PostfixMathCommand {

    public Max() {
        this.numberOfParameters = 2;
    }

    public void run(Stack stack) throws ParseException {
        this.checkStack(stack);
        stack.push(this.max(stack.pop(), stack.pop()));
    }

    public double max(Object param1, Object param2) throws ParseException {
        if (param1 instanceof Number && param2 instanceof Number) {
            return Math.max(((Number) param1).doubleValue(), ((Number) param2).doubleValue());
        } else {
            System.out.println("Invalid parameter type");
            throw new ParseException("Invalid parameter type");
        }
    }

}
