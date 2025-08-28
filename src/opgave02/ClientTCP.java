package opgave02;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientTCP {
    private static DataOutputStream outToServer;
    private static BufferedReader inFromServer;

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("10.10.138.49", 10_000);
            outToServer = new DataOutputStream(
                    clientSocket.getOutputStream()
            );
            inFromServer = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()
                    )
            );

            Thread writeThread = new Thread( () -> writeToServer());
            Thread readThread = new Thread(() -> readFromServer());

            writeThread.start();
            readThread.start();
        } catch (UnknownHostException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeToServer(){
        Scanner messageScanner = new Scanner(System.in);
        String messageToServer;
        while (true) {
            try {
                System.out.print("Input message: ");
                messageToServer = messageScanner.nextLine();
                outToServer.writeBytes(messageToServer + '\n');
                System.out.println("Send: " + messageToServer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void readFromServer(){
        String messageFromServer;
        while (true) {
            try {
                messageFromServer = inFromServer.readLine();
                System.out.println("Received: " + messageFromServer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}