package opgave02;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerTCP {
    private static BufferedReader inFromClient;
    private static DataOutputStream outToClient;

    public static void main(String[] args) {
        try (ServerSocket welcomeSocket = new ServerSocket(10_000)) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("[Client connected to Server from IP: " + connectionSocket.getInetAddress() + "]");

            inFromClient = new BufferedReader(
                    new InputStreamReader(
                            connectionSocket.getInputStream()
                    )
            );
            outToClient = new DataOutputStream(
                    connectionSocket.getOutputStream()
            );
            Thread readThread = new Thread(() -> readFromClient());
            Thread writeThread = new Thread(() -> writeToClient());

            readThread.start();
            writeThread.start();
        } catch (UnknownHostException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readFromClient(){
        String messageFromClient;
        while (true) {
            try {
                messageFromClient = inFromClient.readLine();
                System.out.println("Received: " + messageFromClient);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void writeToClient(){
        Scanner scanner = new Scanner(System.in);
        String messageToClient;
        while (true) {
            try {
                System.out.print("Input message: ");
                messageToClient = scanner.nextLine();
                outToClient.writeBytes(messageToClient + '\n');
                System.out.println("Send: " + messageToClient);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}