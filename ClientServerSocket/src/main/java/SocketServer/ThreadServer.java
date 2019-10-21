package SocketServer;

import SocketServer.ClientServiceThread;
import SocketServer.Person;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadServer extends Thread {
    static int port = 6666;
    private static List<Person> list;
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static boolean isClose=false;

    public static List<Person> getList() {
        return list;
    }
    public static void Close(){
        isClose=true;
    }

    public static void addToList(Person person) {
        list.add(person);
    }

    public static void stopServer(){
        executorService.shutdown();
        System.out.println("Closing Server");
    }
    public ThreadServer(){
        new Thread(this).start();
    }
    public void run(){
        list = new LinkedList<>();
        System.out.println("Server started and ready to accept client requests");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            int id = 0;
            while (!isClose) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ClientServiceThread(clientSocket, id++));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopServer();
        }
    }


}
