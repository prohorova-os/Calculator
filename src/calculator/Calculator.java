/**
 * калькулятор реализован через два класса и два перечисления enum:
 * 1) class Calculator - получение строки арифметической операции через консоль,
 *      удаление пробелов и невидимых символов; перевод строки в верхний регистр;
 *      проверка, не пустая ли строка; проверка, является ли строчка арифметическим выражением:
 *      вывод исключений, если строка не подходит под заявленные условия; разложение строки в массив строк;
 *      преобразование массива строк в массив чисел; вычисление результата; вывод результата на экран,
 *      если он удовлетворяет условиям, или вывод исключения;
 * 2) class RomanNumbers - класс для перевода арабского числа в римское и вывод на экран;
 * 3) enum Operation - перечисление и реализация математических операций;
 * 4) enum Number - словарь арабских и римских цифр до 10 и десятки от 10 до 100, включающий функции
 *      поиск элемента по входящему римскому числу, поиск элемента по входящему арабскому числу.
 * Реализовано и изучено:
 * - Основы синтаксиса Java, простые (примитивные) типы данных: Integer, String;
 * - Арифметические операции в java: +, -, *, /;
 * - Методы
 * - Преобразование строки в число
 * - Класс String, работа со строками
 * - Циклы в Java
 * - Работа с массивами
 * - Логические операторы
 * - Условные операторы, сравнение, switch case
 * - Enum
 * - Работа с консолью - ввод/вывод, класс Scanner
 * - Java и ООП
 * - Обработка ошибок и создание своих исключений
 */

package calculator;

import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        try {
            //ввод арифметической операции в консоль
            Scanner in = new Scanner(System.in);
            System.out.print("Введите арифметическую операцию\n" +
                    "(арифметическая операция может быть только из чисел:\n" +
                    "- только из арабских чисел от 1 до 10,\n" +
                    "- только из римских чисел от I до X;\n" +
                    "включать только операции +, -, *, /;\n" +
                    "результат арифметической операции, состоящей из римских цифр, должен быть > 0;\n" +
                    "для ввода арифметической операции нажмите enter): ");
            String str = in.nextLine();
            //подготовка строки к разложению на операторы и операнд
            //удаляем пробелы и скрытые символы из строки
            str = str.replaceAll("\\s+","");
            //переводим в верхний регистр
            str = str.toUpperCase();
            //проверка, не пустая ли строка
            if(str.isEmpty()) {
                //создаем исключение, что строка пустая
                throw new Exception("Ошибка: Введенная строка - пустая.");
            }
            else {
                //подготавливаемся к вычислению значения
                System.out.printf("Введенная арифметическая операция: %s \n", str);
                in.close();
                String arifmOperat = str;
                //проверка строчки, является ли она арифметическим выражением
                boolean bol = validationArifmOperat(arifmOperat);
                if (bol){
                    //разложение строки в массив строк:
                    //0 уровень - arrOpers[0] - две ячейки, хранят строчные значения операторов
                    //1 уровень- arrOpers[1][0] - одна ячейка, хранит операнд
                    String[][] arrOpers = arrayOperand(arifmOperat);
                    //преобразование массива строк в массив чисел
                    Integer[][] arrNums = new Integer[2][];
                    arrNums[0] = new Integer[2]; //числовые значения операндов
                    arrNums[1] = new Integer[1]; //хранит значение 0 - если цифры арабские, 1 - если римские
                    arrNums = convertingArrStrToArrNumbers(arrOpers); //преобразование массива строк в массив чисел
                    //вычисляем результат
                    Integer result = 1000;
                    try {
                        if((arrNums[0][0] > 10) | (arrNums[0][1]  > 10)){
                            throw new Exception("Ошибка: Числа не должны быть > 10.");
                        }
                        else {
                            switch (arrOpers[1][0]) {
                                case "+":
                                    Operation op = Operation.SUM;
                                    result = op.action(arrNums[0][0], arrNums[0][1]);
                                    break;
                                case "-":
                                    op = Operation.SUBTRACT;
                                    result = op.action(arrNums[0][0], arrNums[0][1]);
                                    break;
                                case "*":
                                    op = Operation.MULTIPLY;
                                    result = op.action(arrNums[0][0], arrNums[0][1]);
                                    break;
                                case "/":
                                    op = Operation.DIVISION;
                                    result = op.action(arrNums[0][0], arrNums[0][1]);
                                    break;
                            }
                            //печатаем если когда (числа до 10) и (не римское число)
                            if((result != 1000)&&(arrNums[1][0] != 1)){
                                System.out.println("Значение арифметической операции: " + result);
                            } else if((result >= 1)&&(arrNums[1][0] == 1)){ //или если результат >1 и (римское число)
                                //вывод результата на экран
                                RomanNumbers resultRoman = new RomanNumbers(result);
                                resultRoman.displayInfo();
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        } catch(Exception ex){ //вывод ошибки, если строка пустая
            System.out.println(ex.getMessage());
        }
        System.out.println("Программа завершена.");
    }

    //проверка строчки, является ли она арифметическим выражением
    static Boolean validationArifmOperat(String str){
        try {
            //правильные
            boolean result1 = str.matches("^(\\s*)\\d{1,2}(\\s*)(\\+|\\-|\\*|\\/)(\\s*)\\d{1,2}(\\s*)$");
            boolean result2 = str.matches("^(\\s*)(I|II|III|IV|V|VI|VII|VIII|IX|X)(\\s*)(\\+|\\-|\\*|\\/)(\\s*)(I|II|III|IV|V|VI|VII|VIII|IX|X)(\\s*)$");
            //Ошибка: Используются одновременно разные системы счисления
            boolean result3 = str.matches("^(\\s*)(\\-*)(\\s*)((I|II|III|IV|V|VI|VII|VIII|IX|X)|[IVXLCDM]{2,100})(\\s*)(\\+|\\-|\\*|\\/)(\\s*)(\\-*)(\\s*)\\d{1,2}(\\s*)$");
            boolean result4 = str.matches("^(\\s*)(\\-*)(\\s*)\\d{1,2}(\\s*)(\\+|\\-|\\*|\\/)(\\s*)(\\-*)(\\s*)((I|II|III|IV|V|VI|VII|VIII|IX|X)|[IVXLCDM]{2,100})(\\s*)$");
            //Ошибка: Используется недопустимый оператор.
            boolean result5 = str.matches("^(\\s*)\\d{1,2}(\\s*)([^\\+\\-\\*\\/])(\\s*)\\d{1,2}(\\s*)$");
            boolean result6 = str.matches("^(\\s*)((I|II|III|IV|V|VI|VII|VIII|IX|X)|\\d{1,2})(\\s*)([^\\+\\-\\*\\/])(\\s*)((I|II|III|IV|V|VI|VII|VIII|IX|X)|\\d{1,2})(\\s*)$");
            //Ошибка: Числа должны быть целыми
            boolean result7 = str.matches("^(\\s*)\\d{1,2}(\\.)\\d{0,16}(\\s*)(\\+|\\-|\\*|\\/)(\\s*)\\d{1,2}(\\s*)$");
            boolean result8 = str.matches("^(\\s*)\\d{1,2}(\\s*)(\\+|\\-|\\*|\\/)(\\s*)\\d{1,2}(\\.)\\d{0,16}(\\s*)$");
            //Ошибка: Числа не должны быть отрицательными
            boolean result9 = str.matches("^(\\s*)((\\-))(\\s*)\\d{1,2}(\\s*)(\\+|\\-|\\*|\\/)(\\s*)((\\-)*)(\\s*)\\d{1,2}(\\s*)$");
            boolean result10 = str.matches("^(\\s*)((\\-))(\\s*)(I|II|III|IV|V|VI|VII|VIII|IX|X)(\\s*)(\\+|\\-|\\*|\\/)(\\s*)((\\-)*)(\\s*)(I|II|III|IV|V|VI|VII|VIII|IX|X)(\\s*)$");
            boolean result11 = str.matches("^(\\s*)((\\-)*)(\\s*)\\d{1,2}(\\s*)(\\+|\\-|\\*|\\/)(\\s*)((\\-))(\\s*)\\d{1,2}(\\s*)$");
            boolean result12 = str.matches("^(\\s*)((\\-)*)(\\s*)(I|II|III|IV|V|VI|VII|VIII|IX|X)(\\s*)(\\+|\\-|\\*|\\/)(\\s*)((\\-))(\\s*)(I|II|III|IV|V|VI|VII|VIII|IX|X)(\\s*)$");
            //Ошибка: Римские числа должны быть из диапазона от I до X - лучше было бы конечно написать обработчик римских цифр
            boolean result13 = str.matches("^(\\s*)[M*C*M*D*C*D*L*C*D*C*C*X*C*C*L*L*X*X*L*X*I*X*VI*I*I*V*I*I*V*I*V*I*V*I*I*I*I*I*I*]*(\\s*)(\\+|\\-|\\*|\\/)(\\s*)[M*C*M*D*C*D*L*C*D*C*C*X*C*C*L*L*X*X*L*X*I*X*VI*I*I*V*I*I*V*I*V*I*V*I*I*I*I*I*I*]*(\\s*)$");
            //Ошибка: Формат арифметической операции не удовлетворяет заданию, должно быть два операнда и один оператор
            boolean result14 = str.matches("^(\\s*)\\d{1,2}(\\s*)(\\+|\\-|\\*|\\/)(\\s*)\\d{1,2}(\\s*)((\\s*)(\\+|\\-|\\*|\\/)(\\s*)\\d{1,2}(\\s*)){1,100}$");
            boolean result15 = str.matches("^(\\s*)(I|II|III|IV|V|VI|VII|VIII|IX|X)(\\s*)(\\+|\\-|\\*|\\/)(\\s*)(I|II|III|IV|V|VI|VII|VIII|IX|X)(\\s*)((\\s*)(\\+|\\-|\\*|\\/)(\\s*)(I|II|III|IV|V|VI|VII|VIII|IX|X)(\\s*)){1,100}$");

            if(result1|result2) {
                return true;
            } else if(result3|result4) {
                throw new Exception("Ошибка: Используются одновременно разные системы счисления.");
            } else if(result5|result6) {
                throw new Exception("Ошибка: Используется недопустимый оператор.");
            } else if(result7|result8) {
                throw new Exception("Ошибка: Числа должны быть целыми.");
            } else if(result9|result10|result11|result12){
                throw new Exception("Ошибка: Числа не должны быть отрицательными.");
            } else if(result13){
                throw new Exception("Ошибка: Римские числа должны быть из диапазона от I до X.");
            } else if(result14|result15){
                throw new Exception("Ошибка: Формат арифметической операции не удовлетворяет заданию, должно быть два операнда и один оператор.");
            } else{
                throw new Exception("Ошибка: Строка не является арифметической операцией, удовлетворяющей условиям.");
            }
        } catch(Exception ex){ //вывод ошибки
            System.out.println(ex.getMessage());
            return false;
        }
    }

    //разложение строки в массив строк
    static String[][] arrayOperand(String str){
        String[] arrFromStrs = new String[2];
        String oper = "";
        //выделяем знак, по которому разбиваем строчку
        int index1 = str.indexOf('+');
        int index2 = str.indexOf('-');
        int index3 = str.indexOf('*');
        int index4 = str.indexOf('/');
        //разбиваем строчку
        if(index1 != -1){
            arrFromStrs = str.split("\\+");
            oper = "+";
        }
        else if(index2 != -1){
            arrFromStrs = str.split("\\-");
            oper = "-";
        }
        else if(index3 != -1){
            arrFromStrs = str.split("\\*");
            oper = "*";
        }
        else if(index4 != -1){
            arrFromStrs = str.split("\\/");
            oper = "/";
        }
        //формируем массив строк из значений и операнда
        String[][] results = new String[2][1];
        results[0] = new String[]{"", ""};
        results[1] = new String[1];
        results[1][0] = oper;
        for (int i = 0; i < 2; i++) {
            results[0][i] = arrFromStrs[i];
        }
        return results;
    }

    //преобразование массива строк в массив чисел
    static Integer[][] convertingArrStrToArrNumbers(String[][] arrValues){
        Boolean bol = true;
        Integer[][] results = new Integer[2][];
        results[0] = new Integer[2];
        results[1] = new Integer[1];
        //проверяем первое число, если оно арабское число, то преобразуем преобразованием строки в число,
        //если нет - то преобразуем иначе
        try {
            results[0][0] = Integer.valueOf(arrValues[0][0]);
        } catch (NumberFormatException e) {
            bol = false;
        }
        //если число
        try {
            if(bol){
                results[0][1] = Integer.valueOf(arrValues[0][1]);
                results[1][0] = 0; //передаем 0, т.к. число арабское, нужно для вывода результата на экран
            }
            //если это римские цифры
            else{
                //преобразуем значения римских цифр
                results[1][0] = 1; //передаем 1, т.к. число римское, нужно для вывода результата на экран
                String operandStrOne = new String(arrValues[0][0]);
                String operandStrTwo = new String(arrValues[0][1]);
                Number operandOne = Number.toRom(operandStrOne);
                results[0][0] = operandOne.getArab();
                Number operandTwo = Number.toRom(operandStrTwo);
                results[0][1] = operandTwo.getArab();
                //проверяем, чтобы разница значений римских цифр была больше 0
                if((results[0][0] < (results[0][1] + 1)) && (arrValues[1][0] == "-")) {
                    throw new Exception("Ошибка: Разница римских цифр должна быть >= 1.");
                } else if(((results[0][0] / results[0][1]) == 0) && (arrValues[1][0] == "/")) {
                    throw new Exception("Ошибка: Результат частного римских цифр должен быть >= 0.");
                }
            }
            return results;
        } catch(Exception ex){ //вывод ошибки
            System.out.println(ex.getMessage());
        }
        return results;
    }
}

//перечисление операций
enum Operation{
    SUM{
        public int action(int x, int y){return x + y;}
    },
    SUBTRACT{
        public int action(int x, int y){return x - y;}
    },
    MULTIPLY{
        public int action(int x, int y){return x * y;}
    },
    DIVISION{
        public int action(int x, int y){return x / y;}
    };
    public abstract int action(int x, int y);
}

//словарь арабских и римских цифр до 10 и десятки от 10 до 100
enum Number{
    One("I", 1), Two("II", 2), Tree("III", 3), Four("IV", 4), Five("V", 5),
    Six("VI", 6), Seven("VII", 7), Eight("VIII", 8), Nine("IX", 9), Ten("X", 10),
    Twenty("XX", 20), Thirty("XXX", 30), Forty("XL", 40), Fifty("L", 50),
    Sixty("LX", 60), Seventy("LXX", 70), Eighty("LXXX", 80), Ninety("XC", 90),
    OneHundred("C", 100);

    String rom;
    Integer arab;

    //конструктор
    Number(String rom, Integer arab) {
        this.rom = rom;
        this.arab = arab;
    }

    public String getRom() {return rom;}

    public Integer getArab() {return arab;}

    //поиск элемента по входящему римскому числу
    public static Number toRom(String rom) {
        for (Number i: Number.values()) {
            if (i.getRom().equals(rom)) {
                return i;
            }
        }
        return null;
    }

    //поиск элемента по входящему арабскому числу
    public static Number toRom(Integer arab) {
        for (Number i: values()) {
            if (i.arab == arab) {
                return i;
            }
        }
        return null;
    }
}

//класс римских чисел: перевод арабского числа в римское для вывода результата на экран
class RomanNumbers {

    StringBuffer val;

    //конструктор перевода арабского числа в римское для вывода результата на экран
    RomanNumbers(Integer number){
        StringBuffer result = new StringBuffer("");
        Integer n1 = number / 10; //находим десятки
        Integer n2 = number - n1*10; //находим единицы
        if((n1 > 0)&&(n2 != 0)){ //если есть десятки и единицы, то преобразуем десятки и единицы в римское число
            Integer[] arrNum = new Integer[2];
            arrNum[0] = n1*10;
            arrNum[1] = number - n1*10;
            for (int i = 0; i < 2; i++){
                //перевод арабского числа в римское через использование enum
                Number partNumber = Number.toRom(arrNum[i]);
                result.append(partNumber.getRom());
            }
        } else{ //если нет десятков
            //перевод арабского числа в римское через использование enum
            Number partNumber = Number.toRom(number);
            result.append(partNumber.getRom());
        }
        this.val = result;
    }

    //вывод римского числа на экран
    public void displayInfo(){
        System.out.println("Значение арифметической операции: " + val);
    }
}