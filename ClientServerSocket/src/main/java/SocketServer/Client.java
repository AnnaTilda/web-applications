package SocketServer;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Client {

    public static List<String> main(String[] request) {
        final int serverPort = 6666;
        final String address = "localhost";
        InetAddress ipAddress;
        List<String> answers = new ArrayList<>();
        if (request == null || request.length == 0) {
            return answers;
        }
        try {
            ipAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            return answers;
        }
        try (Socket socket = new Socket(ipAddress, serverPort);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        ) {
            String line;
            for (int i = 0; i < request.length; i++) {
                line = request[i];
                System.out.println("Sending this line to the server..." + line);
                if(line.equalsIgnoreCase("stop")){
                    System.out.println("SocketServer.Server stopped");
                    ThreadServer.Close();
                    ThreadServer.stopServer();
                    out.writeUTF(line);
                    out.flush();
                    break;
                }
                out.writeUTF(line);
                out.flush();
                if (line.equalsIgnoreCase("quit")) {
                    System.out.println("SocketServer.Client stopped");
                    break;
                }
                line = in.readUTF();
                System.out.println("Server answer is : " + line);
                answers.add(line);
                if (i + 1 == request.length) {
                    System.out.println("SocketServer.Client stopped");
                    out.writeUTF("quit");
                    out.flush();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
        return answers;
    }
}

