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
        counter(queueA, 'a');
        counter(queueB, 'b');
        counter(queueC, 'c');
    }

    public static void counter(ArrayBlockingQueue<String> queue, char letter) {
        new Thread(() -> {
            int maxSize = 0;
            String maxText = "";
            try {
                String text = queue.take();
                int count = howMany(text, letter);
                if (count > maxSize) {
                    maxSize = count;
                    maxText = text;
                }
            } catch (InterruptedException e) {
                return;
            }
            //    System.out.println(maxText + '\n' + "Макс. кол-во " + letter + " : " + maxSize);
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
