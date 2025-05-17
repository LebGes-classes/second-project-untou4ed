import java.util.Scanner;

public class Tools {
    public static void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println(E);
        }
    }

    public static void waitForInput(Scanner scanner) {
        scanner.nextLine();
    }

    public static boolean isCorrectAnswer(int answer, int min_ind, int max_ind) {
        if (answer < min_ind || answer > max_ind) {
            Tools.clearConsole();
            System.out.println("Неверный ввод");
            System.out.println("Нажмите enter чтобы вернуться");
            return false;
        }
        return true;
    }
}
