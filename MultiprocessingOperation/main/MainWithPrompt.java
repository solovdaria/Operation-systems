package main;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MainWithPrompt {
    public static Process processF;
    public static Process processG;
    public static long lastPromptTime = 5000;
    public static boolean isNextPrompt = true;

    public static void terminate() {
        processF.destroy();
        processG.destroy();
    }

    public static void resultsInZero(String funcName) {
        System.out.println(funcName + " finished with zero value. Process is terminated.");
        terminate();
        System.exit(0);
    }

    public static void prompt(int f_is, int g_is) {
        System.out.println("Press: 1 - continue, 2 - continue without prompt, 3 - cancel computation.");
        Scanner sc = new Scanner(System.in);
        if(sc.hasNextInt()){
            int i = sc.nextInt();

            switch (i) {
                case 1:
                    break;
                case 2:
                    isNextPrompt = false;
                    return;
                case 3:
                    System.out.println("Computation was cancelled.");
                    if (f_is == -1)
                        System.out.println("We couldn't compute function F so fast. Sorry.");
                    if (g_is == -1)
                        System.out.println("We couldn't compute function G so fast. Sorry.");
                    if (f_is > 0 && g_is > 0)System.out.println("But result was computed: " + (f_is | g_is));
                    isNextPrompt = false;
                    System.exit(0);
                    break;
                default:
                    System.out.println("Wrong input.");
                    break;
            }
        }
        else{
            System.out.println("Wrong input.");
        }

        lastPromptTime = System.currentTimeMillis();
        return;
    }

    public static void main(String[] args) {
        int test = Integer.parseInt(args[0]);

        try {
            ServerSocket manager = new ServerSocket(2323);

            ProcessBuilder pbF = new ProcessBuilder("java", "functionF.FuncF");
            pbF.directory(new File("/Volumes/STUDY/University/5thTerm/SPOS/labs/Lab1/out/production/Lab1"));
            processF = pbF.start();
            ProcessBuilder pbG = new ProcessBuilder("java", "functionG.FuncG");
            pbG.directory(new File("/Volumes/STUDY/University/5thTerm/SPOS/labs/Lab1/out/production/Lab1"));
            processG = pbG.start();

            Socket socketF = manager.accept();
            Socket socketG = manager.accept();

            DataOutputStream outputF = new DataOutputStream(socketF.getOutputStream());
            outputF.writeUTF(Integer.toString(test));
            DataOutputStream outputG = new DataOutputStream(socketG.getOutputStream());
            outputG.writeUTF(Integer.toString(test));

            DataInputStream inputF = new DataInputStream(socketF.getInputStream());
            DataInputStream inputG = new DataInputStream(socketG.getInputStream());

            int resultOfF = -1;
            int resultOfG = -1;

            while (true) {
                if (inputF.available() > 0 && resultOfF == -1) {
                    resultOfF = inputF.readByte();
                    if (resultOfF == 0) {
                        resultsInZero("F");
                        break;
                    }
                }

                if (inputG.available() > 0 && resultOfG == -1) {
                    resultOfG = inputG.readByte();
                    if (resultOfG == 0) {
                        resultsInZero("G");
                        break;
                    }
                }

                if (resultOfF > 0 && resultOfG > 0) break;

                if (isNextPrompt && System.currentTimeMillis() - lastPromptTime >= 2000) {
                    prompt(resultOfF, resultOfG);
                }
            }

            System.out.print("\nResult: " + (resultOfF | resultOfG) + "\n");

            inputF.close();
            inputG.close();

            terminate();

            socketF.close();
            socketG.close();
            manager.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
