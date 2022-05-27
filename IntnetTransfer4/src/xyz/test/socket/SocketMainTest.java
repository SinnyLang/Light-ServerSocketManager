package xyz.test.socket;

import xyz.sl.socket.ServerSocketManager;

public class SocketMainTest {
    static ServerSocketManager serverMan = ServerSocketManager.getServerMan();
    public static void main(String[] args) {

        try {
            serverMan.setWorkerImpl(WorkerImplTest.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            System.out.println("开启服务器");
            serverMan.accept();
        }).start();


        new Thread(() -> {
            System.out.println("开启客户端");
            for (int i = 0; i < 6; i++) {
                new WorkClientImpl("client"+i, "localhost", 5439).conn();
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Thread.sleep(2000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("关闭端口");
        serverMan.interruptAll();

    }
}
