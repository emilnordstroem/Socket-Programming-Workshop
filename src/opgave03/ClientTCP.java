package opgave03;

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
        String inputMessage = initialConnection();
        connectToServer(inputMessage, 10_000);

        String responseFromDNS = requestDNS();
        connectToServer(responseFromDNS, 10_001);

        Thread writeThread = new Thread( () -> writeToServer());
        Thread readThread = new Thread(() -> readFromServer());

        writeThread.start();
        readThread.start();
    }

    private static String initialConnection(){
        Scanner initialConnection = new Scanner(System.in);
        System.out.print("DNS Server IP Address: ");
        return initialConnection.nextLine().trim();
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

    private static void connectToServer(String ipAddress, int port){
        try {
            Socket clientSocket = new Socket(ipAddress, port);
            outToServer = new DataOutputStream(
                    clientSocket.getOutputStream()
            );
            inFromServer = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()
                    )
            );
        } catch (UnknownHostException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String requestDNS(){
        System.out.print("Request to DNS: ");
        Scanner clientInput = new Scanner(System.in);
        String replyFromServer;

        while (true) {
            String commandToDNS = clientInput.nextLine();
            String[] splitedCommandToDNS = commandToDNS.toLowerCase().split(" ");
            try {
                outToServer.writeBytes(commandToDNS + '\n');
                replyFromServer = inFromServer.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (splitedCommandToDNS[0].equals("connect")) {
                break;
            }
            System.out.println(replyFromServer);
        }

        return replyFromServer;
    }

}
