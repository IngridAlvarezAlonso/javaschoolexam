package com.tsystems.javaschool.tasks.spreadsheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spreadsheet {
    private final String REGEX_CELL = "^=([A-Z])([0-9])$";
    private final String REGEX_OPERATION = "^=(([A-Z][1-9])|[0-9]+)(\\+|\\-|\\*|\\/)(([A-Z][1-9])|[0-9]+)";
    private final String REGEX_TEXT = "^'.+";
    private final String REGEX_FORMULA = "(^=|^')";
    private final String REGEX_IS_NUMERIC = "[0-9]+";
    private final HashMap<Character, Integer> letters;

    public Spreadsheet(){
        letters = new HashMap<>();
        createdCorrespondenceLetters();
    }

    private void createdCorrespondenceLetters(){
        letters.put('A', 0);
        letters.put('B', 1);
        letters.put('C', 2);
        letters.put('D', 3);
        letters.put('E', 4);
        letters.put('F', 5);
        letters.put('G', 6);
        letters.put('H', 7);
        letters.put('I', 8);
        letters.put('J', 9);
        letters.put('K', 10);
        letters.put('L', 11);
        letters.put('M', 12);
        letters.put('N', 13);
        letters.put('O', 14);
        letters.put('P', 15);
        letters.put('Q', 16);
        letters.put('R', 17);
        letters.put('S', 18);
        letters.put('T', 19);
        letters.put('U', 20);
        letters.put('V', 21);
        letters.put('W', 22);
        letters.put('X', 23);
        letters.put('Y', 24);
        letters.put('Z', 25);

    }

    /**
     * Process table cells
     *
     * @param inputData unprocessed table cells
     */
    public List<String> process(List<String> inputData) {

        List<List<String>> originaltable = fillTable(inputData);
        List<List<String>> valuesTable = calculateValues(originaltable);

        return createFinalTable(valuesTable);
    }

    private List<String> createFinalTable(List<List<String>> valuesTable) {

        List<String> finalTable = new ArrayList<>();

        for(List<String> row : valuesTable){
            StringBuilder finalFormatCell = new StringBuilder();
            for(String column : row){
                finalFormatCell.append(" ");
                finalFormatCell.append(column);
            }
            finalFormatCell.delete(0,1);
            finalTable.add(finalFormatCell.toString());
        }
        return finalTable;
    }

    private List<List<String>> fillTable(List<String> inputData){
        List<List<String>> table = new ArrayList<>();

        for(String row : inputData) {
            List<String> columns;
            columns = Arrays.asList(row.split(" "));
            table.add(columns);
        }

        return table;
    }

    private List<List<String>> calculateValues(List<List<String>> originaltable) {
        List<List<String>> valuesTable = new ArrayList<>(originaltable);

        for(int cont = 0; cont < valuesTable.size(); cont++){
            for(int cont2 = 0; cont2 < valuesTable.get(cont).size(); cont2++){
                String cellValue = valuesTable.get(cont).get(cont2);
                String value2 = calculateValue(cellValue, valuesTable);
                valuesTable.get(cont).set(cont2,value2);
            }
        }
        return valuesTable;
    }



    private String calculateValue(String cell, List<List<String>> valuesTable) {
        Matcher matcherCell = Pattern.compile(REGEX_CELL).matcher(cell);
        Matcher matcherRegexOperation = Pattern.compile(REGEX_OPERATION).matcher(cell);
        Matcher matcherRegexText = Pattern.compile(REGEX_TEXT).matcher(cell);

        if (matcherCell.find()) {
            int numRow = letters.get(cell.charAt(1));
            int numColumn = Character.getNumericValue(cell.charAt(2))-1; //I rest 1 because Java arrays begin in "0" index.
            cell = valuesTable.get(numRow).get(numColumn);
        }else if(matcherRegexOperation.find()){
            String operand1 = matcherRegexOperation.group(2);
            String operator = matcherRegexOperation.group(3);
            String operand2 = matcherRegexOperation.group(4);

            if (!operand1.matches(REGEX_IS_NUMERIC))
                operand1 = calculateValue("="+operand1, valuesTable);
            if (!operand2.matches(REGEX_IS_NUMERIC))
                operand2 = calculateValue("="+operand2, valuesTable);


            Integer operand1Int = Integer.parseInt(operand1);
            Integer operand2Int = Integer.parseInt(operand2);

            int result = 0;
            switch (operator) {
                case "*":
                    result = operand1Int * operand2Int;
                    break;
                case "/":
                    result = operand1Int / operand2Int;
                    break;
                case "+":
                    result = operand1Int + operand2Int;
                    break;
                case "-":
                    result = operand1Int - operand2Int;
                    break;
            }

                cell  = String.valueOf(result);

        }else if(matcherRegexText.find()){
            cell = cell.substring(1);
        }

        Matcher matcherFormula = Pattern.compile(REGEX_FORMULA).matcher(cell);
        if(matcherFormula.find()) {
            cell = calculateValue(cell, valuesTable);
        }
        return cell;
    }

}
