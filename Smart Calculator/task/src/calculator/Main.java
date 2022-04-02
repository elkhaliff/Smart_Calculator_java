package calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            var input = scanner.nextLine().trim();
            if (!"".equals(input)) {
                if ("/help".contains(input)) {
                    System.out.println("The program calculates the sum of numbers");
                } else if ("/exit".contains(input)) {
                    System.out.println("Bye!");
                    return;
                } else {
                    if (input.contains(" ")) {
                        String[] numb = input.split(" ");
                        int sum = 0;
                        for (String s : numb) {
                            sum += Integer.parseInt(s);
                        }
                        System.out.println(sum);
                    } else
                        System.out.println(input);
                }
            }
        }
    }
}
