package functionF;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class FuncF {
    public static void main(String[] args) {
        Socket clientF = null;

        DataOutputStream output = null;
        DataInputStream input = null;

        try{
            clientF = new Socket("localhost", 2323);
            output = new DataOutputStream(clientF.getOutputStream());
            input = new DataInputStream(clientF.getInputStream());
            int testNumber = Integer.parseInt(input.readUTF());
            switch(testNumber) {
                case 1:
                    Thread.sleep(1000);
                    output.writeByte(1);
                    break;
                case 2:
                    Thread.sleep(3000);
                    output.writeByte(1);
                    break;
                case 3:
                    Thread.sleep(3000);
                    output.writeByte(0);
                    break;
                case 4:
                    Thread.sleep(1000);
                    while(true);
                case 5:
                    Thread.sleep(1000);
                    output.writeByte(1);
                    break;
                case 6:
                    while(true) {
                        Thread.sleep(1000);
                    }
            }

            output.close();
            clientF.close();
        }
        catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
