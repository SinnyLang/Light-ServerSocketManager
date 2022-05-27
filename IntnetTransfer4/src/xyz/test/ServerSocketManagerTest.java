package xyz.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.sl.socket.ServerSocketManager;
import xyz.test.socket.WorkerImplTest;

import java.io.IOException;
import java.net.Socket;

class ServerSocketManagerTest {
    private static ServerSocketManager serverMan ;

    @BeforeAll
    static void setServerMan(){
        serverMan = ServerSocketManager.getServerMan();
    }

    @Test
    void accept() throws NoSuchMethodException {
        System.out.println(serverMan.hashCode());
        serverMan.setWorkerImpl(WorkerImplTest.class);
        serverMan.accept();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        serverMan.interruptAll();
//        System.out.println();
//        serverMan.accept();
//        serverMan.interruptAll();
//        System.out.println();
    }

    @Test
    void client(){
        try {
            Socket socket1 = new Socket("localhost", 5439);
            Socket socket2 = new Socket("localhost", 5439);
            Socket socket3 = new Socket("localhost", 5439);
            socket2.close();
            socket1.close();
            socket3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterAll
    static void interruptAll(){
        serverMan.interruptAll();
    }



    public static void main(String[] args) throws InterruptedException {
        ServerSocketManager serverMan = ServerSocketManager.getServerMan();
        serverMan.accept();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serverMan.interruptAll();
        }).start();
    }
}