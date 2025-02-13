package org.chomookun.arch4j.core.common.cli;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InteractiveUtil {

    /**
     * Asks input question
     * @param question question
     * @return answer value
     */
    public static String askInput(String question) {
        Scanner scanner = new Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()) {
            System.out.print(question + ':');
            String answer = scanner.nextLine();
            if(answer != null && !answer.isBlank()) {
                return answer;
            }
        }
        return null;
    }

    public static String askSelect(String question, Map<String,String> options) {
        Scanner scanner = new Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(question);
            for(Map.Entry<String,String> entry : options.entrySet()) {
                System.out.printf("[%s]: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.print("Select:");
            String answer = scanner.nextLine();
            if(answer != null && !answer.isBlank()) {
                if (options.containsKey(answer)) {
                    return answer;
                }
            }
        }
        return null;
    }

    /**
     * Asks confirm question
     * @param message message
     */
    public static boolean askConfirm(String message) {
        Scanner scanner = new Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()) {
            System.out.print(message + "[Y/n]:");
            String answer = scanner.nextLine();
            if("Y".equals(answer)) {
                return true;
            }else if("n".equals(answer)) {
                return false;
            }
        }
        return false;
    }

}
