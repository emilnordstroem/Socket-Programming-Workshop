package opgave03;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

public class ClientTCP {
    private static DataOutputStream outToServer;
    private static BufferedReader inFromServer;

    public static void main(String[] args) {
        String inputMessage = initialConnection();
//        connectToServer(inputMessage, 10_000);

//        String responseFromDNS = requestDNS_TCP();
        String responseFromDNS = requestDNS_UDP(inputMessage, 10_000);
        connectToServer(responseFromDNS, 10_001);

        Thread writeThread = new Thread(() -> writeToServer());
        Thread readThread = new Thread(() -> readFromServer());

        writeThread.start();
        readThread.start();
    }

    private static String initialConnection(){
        Scanner initialConnection = new Scanner(System.in);
        System.out.print("DNS Server IP Address: ");
        return initialConnection.nextLine().trim();
    }

    private static String requestDNS_TCP(){
        System.out.print("Request to DNS: ");
        Scanner clientInput = new Scanner(System.in);
        String replyFromDNSServer;

        while (true) {
            String commandToDNS = clientInput.nextLine();
            String[] splitedCommandToDNS = commandToDNS.toLowerCase().split(" ");
            try {
                outToServer.writeBytes(commandToDNS + '\n');
                replyFromDNSServer = inFromServer.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (splitedCommandToDNS[0].equals("connect")) {
                break;
            }
            System.out.println(replyFromDNSServer);
        }

        return replyFromDNSServer;
    }

    private static String requestDNS_UDP(String ipAddress, int port){
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            System.out.print("Request to DNS: ");
            Scanner inputFromClient = new Scanner(System.in);
            String requestToDNS;
            DatagramPacket packet;
            byte[] byteArray = new byte[1024];
            String responseFromDNS = "";

            while(true){
                requestToDNS = inputFromClient.nextLine();
                byteArray = requestToDNS.getBytes();
                packet = new DatagramPacket(
                        byteArray,
                        byteArray.length,
                        InetAddress.getByName(ipAddress),
                        port
                );
                clientSocket.send(packet);
                byteArray = new byte[1024];

                clientSocket.receive(packet);
                responseFromDNS = new String(packet.getData()).trim();
                String[] splittedResponseFromDNS = responseFromDNS.toLowerCase().split(" ");

                if (splittedResponseFromDNS[0].equals("connect")) {
                    break;
                }
                System.out.println(responseFromDNS);
            }
            return responseFromDNS;
        } catch (IOException e) {
            throw new RuntimeException(e);
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