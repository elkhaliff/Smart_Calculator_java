package calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CalcEngine calcEngine = new CalcEngine();
        while (true) {
            var input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                if (input.startsWith("/")) {
                    if ("/help".contains(input)) {
                        calcEngine.help();
                    } else if ("/exit".contains(input)) {
                        calcEngine.exit();
                        return;
                    } else {
                        System.out.println("Unknown command");
                    }
                } else {
                    input = input.replaceAll("\\s+", "")
                            .replaceAll("\\+\\++|(--)+", "+")
                            .replaceAll("\\+-", "-");
                    calcEngine.calc(input);
                }
            }
        }
    }
}