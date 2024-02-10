package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    private static ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            int maxSizeA = 0;
            String maxTextA = "";
            try {
                String text = queueA.take();
                int countA = howMany(text, 'a');
                if (countA > maxSizeA) {
                    maxSizeA = countA;
                    maxTextA = text;
                }
            } catch (InterruptedException e) {
                return;
            }
//            System.out.println(maxTextA);
//            System.out.println(maxSizeA);
        }).start();

        new Thread(() -> {
            int maxSizeB = 0;
            String maxTextB = "";
            try {
                String text = queueB.take();
                int countB = howMany(text, 'b');
                if (countB > maxSizeB) {
                    maxSizeB = countB;
                    maxTextB = text;
                }
            } catch (InterruptedException e) {
                return;
            }
//            System.out.println(maxTextB);
//            System.out.println(maxSizeB);
        }).start();

        new Thread(() -> {
            int maxSizeC = 0;
            String maxTextC = "";
            try {
                String text = queueC.take();
                int countC = howMany(text, 'a');
                if (countC > maxSizeC) {
                    maxSizeC = countC;
                    maxTextC = text;
                }
            } catch (InterruptedException e) {
                return;
            }
//            System.out.println(maxTextC);
//            System.out.println(maxSizeC);
        }).start();
    }

    public static int howMany(String text, char letter) {
        int result = text.length() - text.replace(String.valueOf(letter), "").length();
        return result;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
