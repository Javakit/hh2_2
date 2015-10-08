package net.compitek;


import java.math.BigInteger;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

/**
 * 2. Бесконечная последовательность
 * Возьмём бесконечную цифровую последовательность, образованную склеиванием последовательных положительных чисел: S = 123456789101112131415...
 * Определите первое вхождение заданной последовательности A в бесконечной последовательности S (нумерация начинается с 1).
 * <p>
 * Пример входных данных:
 * 6789
 * 111
 * <p>
 * Пример выходных данных:
 * 6
 * 12
 */
public class Main_hh_2_2 {

    /*maximum number of digits. (if it will very long,  "java heap" will trows.
    You can add ram to jvm, but you really don't need so long number)*/
    private final static int maxDigits = 2000;

    public static void main(String[] args) {
        printInstructions();
        String inputString;
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                inputString = in.nextLine().trim();

                if (inputString.matches("random")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append((int) Math.floor(9 * Math.random() + 1));
                    for (int i = 0; i < maxDigits - 1; i++) {
                        stringBuilder.append((int) Math.floor(10 * Math.random()));
                    }
                    inputString = stringBuilder.toString();
                    System.out.println("generated number:");
                    System.out.println(inputString);
                    System.out.println("");
                }


                if (inputString.length() > maxDigits) {
                    System.out.println("it's too long number, pls. try another;");
                } else if (inputString.matches("^[1-9]{1,1}[0-9]{0,}$")) {
                    BigInteger originNumber = findOriginNumber(inputString);
                    BigInteger entryNumber = findNumberOfEntry(originNumber);
                    System.out.println(/*"result:" +*/ entryNumber);
                    printInstructions();
                } else if (inputString.matches("exit")) {
                    break;
                } else {
                    System.out.println("wrong command;");
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new NumberFormatException(e.getMessage());
        } catch (IllegalArgumentException illegalArgException) {
            illegalArgException.printStackTrace();
        }
    }

    /**
     * Prints the list of instructions in System.out stream.
     */
    private static void printInstructions() {
        System.out.println(" ");
        System.out.println("please, write your number. It must be natural number, not longer than " + maxDigits
                + " digits and not start from \"0\" )");
        System.out.println("write \"random\" to generate random number with  " + maxDigits + " digits");
        System.out.println("write \"exit\" to exit ");

    }

    /**
     * find number (as BigInteger object) which  forms "inputString" number by listing natural numbers.
     * e.g.  if inputString=14151 then result=14
     * <p>
     * 1. получаем из  inputString массив цифр inputArray
     * 2. Забираем первые i цифр и получаем число testedValue
     * 3. Копируем testedValue в testedValueTemp
     * чтобы использовать testedValueTemp в итерации (приплюсовывая по еденице), но помнить об исходном testedValue
     * 4. Заполняем (в цикле по j) массив testedArray, соответствующий  testedValue,  для сравнения с inputArray
     * путем переписывания цифр из testedValueList в testedArray.
     * При этом генерируем новый testedValueList цифр из testedValueTemp если старый testedValueList пройден (по k).
     * 5. на каждом шаге сравниваем testedArray[j] и inputArray[j], в случае неудачи повторяем все с большим набором цифр в testedValue,
     * в случае прохода полного цикла по j возвращаем testedValue
     *
     * @param inputString - initial number as string
     * @return BigInteger result
     */
    private static BigInteger findOriginNumber(String inputString) throws IllegalArgumentException {
        if (!inputString.matches("^[1-9]{1,1}[0-9]{0,}$"))
            throw new IllegalArgumentException(" Wrong inputString format (can be only digits, and not start from 0)");

        // transfer from string to byte[]
        int length = inputString.length();
        byte[] inputArray = new byte[length];
        try {
            for (int i = 0; i < inputString.length(); i++) {
                inputArray[i] = Byte.valueOf(inputString.substring(i, i + 1));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }

        // array of testedValueTemporary digits
        byte[] testedArray;

        // "origin number" value
        BigInteger testedValue = BigInteger.ZERO;

        // correspond to testedValue
        List<Byte> testedValueList = new ArrayList<Byte>();

        // value to generate number from "origin number"
        BigInteger testedValueTemporary;

        //temporary variable to store results of division
        BigInteger[] divideAndReminderArray = new BigInteger[2];


        // i - number of digits in testedValue ("origin number")
        for (int i = 1; i <= inputArray.length; i++) {

            testedArray = new byte[inputArray.length];

            // generate "origin number" candidate
            testedValue = BigInteger.ZERO;
            for (int j = 0; j < i; j++) {
                testedValue = testedValue.multiply(BigInteger.TEN).add(BigInteger.valueOf(inputArray[j]));
            }


            testedValueTemporary = testedValue;

            int k = 0;
            testedValueList.clear();
            divideAndReminderArray[0] = testedValueTemporary;
            // voil?
            int j;
            for (j = 0; j < inputArray.length; j++) {

                if (k == testedValueList.size()) {
                    k = 0;
                    //convert testedValueTemporary to testedValueList
                    testedValueList.clear();
                    while (divideAndReminderArray[0].compareTo(BigInteger.ZERO) > 0) {
                        divideAndReminderArray = divideAndReminderArray[0].divideAndRemainder((BigInteger.TEN));
                        testedValueList.add((byte) divideAndReminderArray[1].intValue());
                    }
                    testedValueTemporary = testedValueTemporary.add(BigInteger.ONE);
                    divideAndReminderArray[0] = testedValueTemporary;
                }

                testedArray[j] = testedValueList.get(testedValueList.size() - 1 - k);
                if (inputArray[j]!=testedArray[j])
                    break;
                k++;
            }
            if (j == inputArray.length )
                return testedValue;
        }
        return testedValue;
    }

    /**
     * find number of first digit of originValue number in the sequence of natural numbers
     * считаем номер первой цифры originValue в последовательности натуральных чисел
     * @param originValue
     * @return
     */
    private static BigInteger findNumberOfEntry(BigInteger originValue) {
        BigInteger result;
        BigInteger[] divideAndReminderArray = new BigInteger[2];
        divideAndReminderArray[0] = originValue;
        int poryadok = 0;
        while (divideAndReminderArray[0].compareTo(BigInteger.ZERO) > 0) {
            divideAndReminderArray = divideAndReminderArray[0].divideAndRemainder((BigInteger.TEN));
            poryadok++;
        }
        result = originValue.subtract(BigInteger.TEN.pow(poryadok - 1)).multiply(BigInteger.valueOf(poryadok));
        poryadok--;
        while (poryadok > 0) {
            result = result.add(BigInteger.TEN.pow(poryadok).subtract(BigInteger.TEN.pow(poryadok - 1))
                    .multiply(BigInteger.valueOf(poryadok)));
            poryadok--;
        }
        return result.add(BigInteger.ONE);
    }
}
