package main;

import CancelKey.KeyListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class Main {
    public static Process processF;
    public static Process processG;

    public static void terminate() {
        processF.destroy();
        processG.destroy();
    }

    public static void resultsInZero(String funcName) {
        System.out.println(funcName + " finished with zero value. Process is terminated.");
        terminate();
        System.exit(0);
    }

    public static void main(String[] args) {
        int test = Integer.parseInt(args[0]);
        System.out.println("Press '0' at any time to cancel computation.");
        KeyListener listener = null;

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
        try {
            GlobalScreen.registerNativeHook();
            listener = new KeyListener();
            GlobalScreen.addNativeKeyListener(listener);
        } catch (NativeHookException ex) {
            System.err.println(ex.getMessage());
        }

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
            DataInputStream inputG = new DataInputStream(socketG.getInputStream());;

            int resultOfF = -1;
            int resultOfG = -1;

            while(true) {
                if (inputF.available() > 0 && resultOfF == -1) {
                    resultOfF = inputF.readByte();
                    listener.f_is = resultOfF;
                    if (resultOfF == 0) {
                        resultsInZero("F");
                        break;
                    }
                }

                if (inputG.available() > 0 && resultOfG == -1) {
                    resultOfG = inputG.readByte();
                    listener.g_is = resultOfG;
                    if (resultOfG == 0) {
                        resultsInZero("G");
                        break;
                    }
                }

                if (resultOfF > 0 && resultOfG >0) break;
            }
            System.out.print("\nResult: " + (resultOfF | resultOfG) + "\n");

            inputF.close();
            inputG.close();
            terminate();
            socketF.close();
            socketG.close();
            manager.close();
            GlobalScreen.unregisterNativeHook();
        } catch (IOException e) {
            System.out.println(e);
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }
}
