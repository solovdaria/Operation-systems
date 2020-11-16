package CancelKey;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {

    public int f_is;
    public int g_is;

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_0) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
            System.out.println("Computation was cancelled.");
            if (f_is == -1)
                System.out.println("We couldn't compute function F so fast. Sorry.");
            if (g_is == -1)
                System.out.println("We couldn't compute function G so fast. Sorry.");
            else System.out.println("But result was computed: " + (f_is | g_is));
            System.exit(0);
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {}

    public void nativeKeyTyped(NativeKeyEvent e) {}

}