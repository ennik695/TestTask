import java.io.IOException;
import java.util.Scanner;


public class Task {
    public static void main(String[] args)  {
        String strUser;
        String strResult;
        String strOperator;

        Scanner scan = new Scanner(System.in);
        String [] arrStrUser;

        System.out.print("Введи выражение для вычисления строки: ");
        strUser = scan.nextLine();

        arrStrUser = seporateString(strUser);//метод разделения строки

        OperandStr firstOp = new OperandStr(arrStrUser[0]);
        OperandStr secondOp = new OperandStr(arrStrUser[2]);
        strOperator = arrStrUser[1];
        strResult = firstOp.calculateTheString(secondOp,strOperator);
        if(strResult.length()> 40){
            System.out.println("Получилась строка: " + "\"" + strResult.substring(0,40) + "...\"");
        } else {
            System.out.println("Получилась строка: " + "\"" + strResult + "\"");
        }

    }

    //метод разделения строки и выявления ошибок при вводе
    public static String[] seporateString(String strUser)  {
        String[] arrayFin = new String[3];
        boolean isStr = false;
        boolean isNum = false;
        StringBuilder stringBuilder = new StringBuilder();
        if (strUser.charAt(0) != '"'){
            try {
                throw new IOException();
            } catch (IOException e) {
                System.out.println("Первый аргумент не строка");
                System.exit(2);
            }

        }
        int j = 0;
        for(int i = 0; i < strUser.length(); i++ ){
            if (strUser.charAt(i) == '"' && !isStr) {
                isStr = true;
                stringBuilder.append(strUser.charAt(i));
            } else if (strUser.charAt(i) == '"' && isStr){
                isStr = false;
                stringBuilder.append(strUser.charAt(i));
                arrayFin[j] = stringBuilder.toString();
                j +=1;
                stringBuilder = new StringBuilder();
            } else if (isStr){
                stringBuilder.append(strUser.charAt(i));
                if (i == strUser.length() - 1 && isNum){
                    arrayFin[j] = stringBuilder.toString();
                    isStr = false;
                    isNum = false;
                }
            }  else if (!isStr){
                if (strUser.charAt(i) == '+' || strUser.charAt(i) == '-' || strUser.charAt(i) == '*' || strUser.charAt(i) == '/'){
                    if (j == 1) {
                        stringBuilder.append(strUser.charAt(i));
                        arrayFin[j] = stringBuilder.toString();
                        j += 1;
                        stringBuilder = new StringBuilder();
                    }else {
                        try {
                            throw new IOException();
                        } catch (IOException e) {
                            System.out.println("неверно составлено выражение");
                            System.exit(2);
                        }
                    }

                } else if (Character.isDigit(strUser.charAt(i))){
                    stringBuilder.append(strUser.charAt(i));
                    isStr = true; //подразумевается что число не имеет пробелов
                    isNum = true;
                    if (i == strUser.length() - 1){

                        arrayFin[j] = stringBuilder.toString();
                        isStr = false;
                        isNum = false;
                    }

                } else if (strUser.charAt(i) != ' '){
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("не известный символ или отсутствует оператор");
                        System.exit(2);
                    }
                }
            }

        }
        if(isStr) {
            try {
                throw new IOException();
            } catch (IOException e) {
                System.out.println("строка не закрыта");
                System.exit(2);
            }
        }

        //проверка первого операнда
        if (arrayFin[0] != null){
            if (arrayFin[0].length()>12){
                try {
                    throw new IOException();
                } catch (IOException e) {
                    System.out.println("Слишком длинная строка, максимум символов в строке: 10");
                    System.exit(4);
                }
            }
        } else {
            try {
                throw new IOException();
            } catch (IOException e) {
                System.out.println("нет первого операнда");
                System.exit(2);
            }
        }

        // проверка оператора
        if (arrayFin[1] != null){
            if (!arrayFin[1].equals("+") && !arrayFin[1].equals("-") && !arrayFin[1].equals("*") && !arrayFin[1].equals("/")) {
                try {
                    throw new IOException();
                } catch (IOException e) {
                    System.out.println("отсутствует оператор");
                    System.exit(1);
                }
            }
        } else {
            try {
                throw new IOException();
            } catch (IOException e) {
                System.out.println("отсутствует оператор!");
                System.exit(1);
            }
        }



        //проверка второго операнда
        if (arrayFin[2] != null) {
            if (arrayFin[2].charAt(0) != '"') {
                if (Integer.parseInt(arrayFin[2]) > 10 || Integer.parseInt(arrayFin[2]) < 1) {
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("ведено недоступное число");
                        System.exit(3);
                    }
                }
            } else {
                if (arrayFin[2].length()>12){
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("Слишком длинная строка, максимум символов в строке: 10");
                        System.exit(4);
                    }
                }
            }
        }  else {
            try {
                throw new IOException();
            } catch (IOException e) {
                System.out.println("нет второго операнда");
                System.exit(2);
            }
        }


        return arrayFin;
    }
}

class OperandStr {

    //свойства: флаг число или строка, семантика строки
    private final boolean isNum;
    private final String opStr;
    //конструктор
    public OperandStr(String strUser){ //проверить строка или число, убрать ковычки
        if (strUser.charAt(0)=='"' && strUser.charAt(strUser.length()-1)=='"'){
            this.opStr = strUser.substring(1,strUser.length()-1);
            this.isNum = false;
        } else {
            this.opStr = strUser;
            this.isNum = true;
        }

    }

    public  String getOperand(){

        return this.opStr;

    }

    public boolean isOperandADigit(){
        return this.isNum;
    }

    public String calculateTheString(OperandStr secOperand, String operator){
        String resultStr ="";
        switch (operator){
            case ("+"):
                if (!secOperand.isOperandADigit()){
                    resultStr = this.opStr + secOperand.getOperand();
                } else {
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("Вычисление не возможно,введен не коректный оператор.");
                        System.exit(1);
                    }

                }
                break;
            case ("-"):
                if (!secOperand.isOperandADigit()){
                    resultStr = this.opStr.replace(secOperand.getOperand(),"");
                } else {
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("Вычисление не возможно,введен не коректный оператор.");
                        System.exit(1);
                    }

                }
                break;
            case ("*"):
                if (secOperand.isOperandADigit()){
                    for(int i = 0; i< Integer.parseInt(secOperand.getOperand()); i++){
                        resultStr += this.opStr;
                    }
                } else {
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("Вычисление не возможно,введен не коректный оператор.");
                        System.exit(1);
                    }

                }
                break;
            case ("/"):
                if (secOperand.isOperandADigit()){
                    int newLength = this.opStr.length() / Integer.parseInt(secOperand.getOperand());
                    resultStr = this.opStr.substring(0,newLength);
                } else {
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        System.out.println("Вычисление не возможно,введен не коректный оператор.");
                        System.exit(1);
                    }

                }
                break;
            default:
                try {
                    throw new IOException();
                } catch (IOException e) {
                    System.out.println("Вычисление не возможно,введен не коректный оператор.");
                    System.exit(1);
                }

                break;
        }

        return resultStr;

    }

}