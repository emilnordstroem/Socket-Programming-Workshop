package opgave03.namingServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.UnexpectedException;
import java.util.HashMap;

public class NamingServerTCP {
    private static final HashMap<String, String> serverList = new HashMap<>();
    private static DataOutputStream outFromDNS;
    private static BufferedReader inFromClient;

    public static void main(String[] args) {
        try (ServerSocket welcomeSocket = new ServerSocket(10_000)) {

            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("New connection: " + connectionSocket.getInetAddress());

                inFromClient = new BufferedReader(
                        new InputStreamReader(
                                connectionSocket.getInputStream()
                        )
                );

                outFromDNS = new DataOutputStream(
                        connectionSocket.getOutputStream()
                );

                while (true) {
                    String commandToClient = inFromClient.readLine().toLowerCase();
                    String[] splitedCommand = commandToClient.split(" ");
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
                            throw new UnexpectedException("Unknown prompt");
                    }

                    outFromDNS.writeBytes(messageToClient + '\n');
                    if (splitedCommand[0].equals("connect")) {
                        break;
                    }
                }

                System.out.println("Connection closed");
                connectionSocket.close();
            }
        } catch (UnknownHostException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
