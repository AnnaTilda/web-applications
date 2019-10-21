package SocketServer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ClientServiceThread extends Thread {

    private Socket clientSocket;
    private int clientID;

    public ClientServiceThread(Socket socket, int id) {
        clientSocket = socket;
        clientID = id;

    }

    public void run() {

        System.out.println(
                "Accepted SocketServer.Client : ID - " + clientID + " : Address - " + clientSocket.getInetAddress().getHostName());
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            while (true) {
                String clientCommand = in.readUTF();
                System.out.println("SocketServer.Client " + clientID + " says :" + clientCommand);
                if (clientCommand.equalsIgnoreCase("quit")) {
                    System.out.println("Stopping client thread for client : " + clientID);
                    break;
                }
                if (clientCommand.equalsIgnoreCase("stop")) {
                    System.out.println("Stopping client thread for client : " + clientID);
                    ThreadServer.stopServer();
                    break;
                }
                String JSON = clientCommand.substring(4, clientCommand.length());
                if (clientCommand.startsWith("get")) {
                    List<Person> personList = ThreadServer.getList();
                    out.writeUTF(get(JSON, personList));
                } else if (clientCommand.startsWith("add")) {
                    out.writeUTF(add(JSON));
                } else {
                    out.writeUTF("Неправильный формат переданной строки");
                }

                out.flush();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Closing socket");
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String get(String request, List<Person> list) {
        int count = 0;
        Gson gson = new Gson();
        if (!isJson(request)) return "{}";
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        for (Person t : list) {
            if (jsonObject.has("firstName") && jsonObject.get("firstName").getAsString().equals(t.getFirstName()))
                count++;
            if (jsonObject.has("lastName") && jsonObject.get("lastName").getAsString().equals(t.getLastName()))
                count++;
            if (jsonObject.has("age") && jsonObject.get("age").getAsInt() == t.getAge()) count++;
            if (count == jsonObject.size()) return gson.toJson(t);
            count = 0;
        }
        return "{}";
    }

    public String add(String request) {

        if (isJson(request)) {
            JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
            if (jsonObject.has("age") && checkString(jsonObject.get("age").getAsString())) {
                if (jsonObject.has("firstName") && !(jsonObject.get("firstName").getAsString().equals(""))) {
                    if (jsonObject.has("lastName") && !(jsonObject.get("lastName").getAsString().equals(""))) {
                        Gson g = new Gson();
                        ThreadServer.addToList(g.fromJson(request, Person.class));
                        return "Персона добавлена";
                    }
                }
            }
        }
        return "Персона не добавлена";
    }

    public boolean isJson(String str) {
        try {
            new JsonParser().parse(str);
        } catch (Exception e) {
            return false;
        }
        try {
            new JsonParser().parse(str).getAsJsonObject();
        } catch (IllegalStateException ex) {
            return false;
        }

        return true;

    }

    public boolean checkString(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
