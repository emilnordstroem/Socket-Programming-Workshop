package opgave03.namingServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class NamingServerUDP {
    private static final HashMap<String, String> serverList = new HashMap<>();

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(10_000)) {

            byte[] receivedDataFromClient = new byte[1024];
            byte[] sendDataToClient = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(
                        receivedDataFromClient,
                        receivedDataFromClient.length
                );
                serverSocket.receive(packet);
                receivedDataFromClient = new byte[1024];

                String commandFromClient = new String(packet.getData());
                String messageToClient = retrieveInputFromClient(commandFromClient.trim());

                sendDataToClient = messageToClient.getBytes();
                InetAddress ipAddress = packet.getAddress();
                int port = packet.getPort();

                packet = new DatagramPacket(
                        sendDataToClient,
                        sendDataToClient.length,
                        ipAddress,
                        port
                );
                sendDataToClient = new byte[1024];

                serverSocket.send(packet);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String retrieveInputFromClient(String commandFromClient) {
        String[] splitedCommand = commandFromClient.split(" ");
        String messageToClient = "";
        switch (splitedCommand[0]) {
            case "connect":
                messageToClient = getIPAddress(splitedCommand[1]);
                break;
            case "add":
                messageToClient = addToServerList(splitedCommand[1], splitedCommand[2]);
                break;
            case "list":
                messageToClient = returnServerList();
                break;
            default:
                System.out.println("Unknown prompt");
        }
        return messageToClient;
    }

    public static String getIPAddress(String domain){
        return serverList.get(domain);
    }

    public static String addToServerList(String domain, String ipAddress){
        serverList.put(domain, ipAddress);
        return domain + " " + ipAddress;
    }

    public static String returnServerList(){
        return new HashMap<>(serverList).toString();
    }

}