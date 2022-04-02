package calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CalcEngine calcEngine = new CalcEngine();
        while (true) {
            var input = scanner.nextLine().trim();
            if (!input.trim().isEmpty()) {
                if (input.contains("/")) {
                    if ("/help".contains(input)) {
                        calcEngine.help();
                    } else if ("/exit".contains(input)) {
                        calcEngine.exit();
                        return;
                    } else {
                        System.out.println("Unknown command");
                    }
                } else {
                    calcEngine.calc(input.trim());
                }
            }
        }
    }
}