package opgave01;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientTCP {

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("10.10.138.49", 10_000);
            DataOutputStream outToServer = new DataOutputStream(
                    clientSocket.getOutputStream()
            );
            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()
                    )
            );

            Scanner messageScanner = new Scanner(System.in);
            String messageToServer;
            String messageFromServer;

            while (true) {
                messageToServer = messageScanner.nextLine();

                outToServer.writeBytes(messageToServer + '\n');
                System.out.println("Send: " + messageToServer);

                messageFromServer = inFromServer.readLine();
                System.out.println("Received: " + messageFromServer);
            }

        } catch (UnknownHostException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
