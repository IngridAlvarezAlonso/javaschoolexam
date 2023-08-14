package com.tsystems.javaschool.tasks.calculator;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {

        final String REGEX_PARENTHESIS = "(\\((-?[0-9]+)(\\+|\\-|\\*|\\/)(-?[0-9]+)\\))";
        final String REGEX_MULTIPLY_DIVISION = "((-?[0-9]+(\\.[0-9]+)?)([\\*|\\/])(-?[0-9]+(\\.[0-9]+)?))";
        final String REGEX_ADD_SUBS = "((-?[0-9]+)([\\+|\\-])(-?[0-9]+))";
        final String REGEX_IS_NUMERIC = "-?[0-9]+(\\.[0-9]+)?";

        String resultStatement = statement;
        String resultExit;
        boolean operationFounded;

        if(null == resultStatement)
            return null;


        Pattern pattern = Pattern.compile("--+");
        Matcher matcher = pattern.matcher(resultStatement);
        if(matcher.find())
            return null;

        while(!resultStatement.matches(REGEX_IS_NUMERIC)) {
            operationFounded = false;

            Matcher matcherparenthesis = Pattern.compile(REGEX_PARENTHESIS).matcher(resultStatement);
            while(matcherparenthesis.find()){
                String operation = matcherparenthesis.group(1);
                String operand1 = matcherparenthesis.group(2);
                String operator = matcherparenthesis.group(3);
                String operand2 = matcherparenthesis.group(4);
                String result = toOperate(operand1, operator, operand2);

                resultStatement = StringUtils.replaceOnce(resultStatement,operation,result);
                operationFounded = true;
            }

            Matcher matchersMultiplyAndDivision = Pattern.compile(REGEX_MULTIPLY_DIVISION).matcher(resultStatement);
            while (matchersMultiplyAndDivision.find()) {
                String operation = matchersMultiplyAndDivision.group(1);
                String operand1 = matchersMultiplyAndDivision.group(2);
                String operator = matchersMultiplyAndDivision.group(4);
                String operand2 = matchersMultiplyAndDivision.group(5);
                String result = toOperate(operand1, operator, operand2);

                resultStatement = StringUtils.replaceOnce(resultStatement,operation,result);
                operationFounded = true;
            }

            Matcher matchersAddAndSubs = Pattern.compile(REGEX_ADD_SUBS).matcher(resultStatement);
            while (matchersAddAndSubs.find()) {
                String operation = matchersAddAndSubs.group(1);
                String operand1 = matchersAddAndSubs.group(2);
                String operator = matchersAddAndSubs.group(3);
                String operand2 = matchersAddAndSubs.group(4);
                String result = toOperate(operand1, operator, operand2);

                resultStatement = StringUtils.replaceOnce(resultStatement,operation, result);
                operationFounded = true;
            }

            if(!operationFounded)
                return null;

        }

        resultExit = deleteZeros(resultStatement);

        return resultExit;
    }

    private String deleteZeros(String resultStatement) {
        final String REGEX_DELETE_ZEROS = "([0-9]+\\.[1-9]*[1-9])0+";

        Matcher matcherZeros = Pattern.compile(REGEX_DELETE_ZEROS).matcher(resultStatement);

        if(!matcherZeros.find()){
            return resultStatement;
        }

        resultStatement = matcherZeros.replaceAll("$1");

        return resultStatement;
    }

    public String toOperate(String operand1, String operator, String operand2){
        try {
            //BigDecimal operand1Bd = new BigDecimal(operand1);
            //BigDecimal operand2Bd = new BigDecimal(operand2);

            Double operand1Bd = Double.valueOf(operand1);
            Double operand2Bd = Double.valueOf(operand2);

            Double result = null;

            //BigDecimal result = null;
//            if (operator.equals("*")) {
//                result = operand1Bd.multiply(operand2Bd);
//            } else if (operator.equals("/")) {
//                result = operand1Bd.divide(operand2Bd,4, RoundingMode.HALF_UP);
//            } else if (operator.equals("+")) {
//                result = operand1Bd.add(operand2Bd);
//            } else if (operator.equals("-")) {
//                result = operand1Bd.subtract(operand2Bd);
//            }

            switch (operator) {
                case "*":
                    result = operand1Bd * operand2Bd;
                    break;
                case "/":
                    result = operand1Bd / operand2Bd;
                    break;
                case "+":
                    result = operand1Bd + operand2Bd;
                    break;
                case "-":
                    result = operand1Bd - operand2Bd;
                    break;
            }

            String stringValue = String.valueOf(result);

            return stringValue.replaceAll("\\.?0*$", "");

//            if (result.scale() <= 0) {
//                return result.toBigInteger().toString();
//            } else {
//                return result.stripTrailingZeros().toPlainString();
//            }

        }catch(Exception e){
            return null;
        }
    }

}




