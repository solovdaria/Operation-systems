package functionG;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class FuncG {
    public static void main(String[] args) {
        Socket clientG = null;

        DataOutputStream output = null;
        DataInputStream input = null;

        try{
            clientG = new Socket("localhost", 2323);
            output = new DataOutputStream(clientG.getOutputStream());
            input = new DataInputStream(clientG.getInputStream());
            int testNumber = Integer.parseInt(input.readUTF());
            switch(testNumber) {
                case 1:
                    Thread.sleep(3000);
                    output.writeByte(1);
                    break;
                case 2:
                    Thread.sleep(1000);
                    output.writeByte(1);
                    break;
                case 3:
                    Thread.sleep(1000);
                    while(true);
                case 4:
                    Thread.sleep(3000);
                    output.writeByte(0);
                    break;
                case 5:
                    while(true) {
                        Thread.sleep(1000);
                    }
                case 6:
                    Thread.sleep(1000);
                    output.writeByte(1);
                    break;
            }

            output.close();
            clientG.close();
        } catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}