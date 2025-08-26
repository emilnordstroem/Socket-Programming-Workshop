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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String messageFromClient;
        String messageToClient;

        try (ServerSocket welcomeSocket = new ServerSocket(10_000)) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("[Client connected to Server from IP: " + connectionSocket.getInetAddress() + "]");

            BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader(
                            connectionSocket.getInputStream()
                    )
            );
            DataOutputStream outToClient = new DataOutputStream(
                    connectionSocket.getOutputStream()
            );

            while (true) {
                messageFromClient = inFromClient.readLine();
                System.out.println("Received: " + messageFromClient);

                messageToClient = scanner.nextLine();
                outToClient.writeBytes(messageToClient + '\n');
                System.out.println("Send: " + messageToClient);
            }

        } catch (UnknownHostException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
