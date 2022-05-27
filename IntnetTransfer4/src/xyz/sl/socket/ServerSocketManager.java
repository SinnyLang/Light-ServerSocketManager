package xyz.sl.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.List;

//  import xyz.sl.util.*;

public class ServerSocketManager {
    public static Logger serverLog = LoggerFactory.getLogger("server");
    public static int LISTEN_PORT = 5439;
    private static final ServerSocketManager serverMan = new ServerSocketManager();
    public static final List<Worker> workers = new ArrayList<Worker>();

    private Thread serverThread;
    private ServerSocket serverSocket;
    private Constructor workerImplClassCtor;

    public ServerSocketManager(){
    }

    public static ServerSocketManager getServerMan() {
        return serverMan;
    }

    public static void setListenPort(int listenPort) {
        LISTEN_PORT = listenPort;
    }

    public void setWorkerImpl(Class workerImpl) throws NoSuchMethodException {
        this.workerImplClassCtor = workerImpl.getConstructor(SocketWrapper.class, int.class);
    }

    public void interruptAll() {
//        System.out.println(":"+serverThread.hashCode());
        // 特别奇怪，下一行代码，在 JUnit5 中，单独测试时，运行时会产生空指针异常
        // 但是在main方法中运行时不会产生空指针异常
        serverThread.interrupt();
        serverLog.info("关闭{}端口监听 结果", LISTEN_PORT);
        try {
            serverSocket.close();
        } catch (IOException e) {
            serverLog.error("{}",e.toString());
            e.printStackTrace();
        }
    }

    public void accept() {
        serverLog.info("[server] 开启监听在端口{}", LISTEN_PORT);

        try {
            serverSocket = new ServerSocket(LISTEN_PORT);
        } catch (IOException e) {
            serverLog.error("[{}] 在{}端口申请失败，可能端口被占用", getClass(), LISTEN_PORT);
            e.printStackTrace();
        }

        if (serverSocket == null) {
            serverLog.error("服务器打开失败, {}", new NullPointerException("serverSocket is null").toString());
            return;
        }

        // 为服务器开启一个新线程
        serverThread = new Thread(new Server(), "server");
        serverThread.start();
    }

    // server mam's server
    class Server implements Runnable{
        @Override
        public void run() {
            int index = 0;
            boolean b = false;
            // 如果 server man 没有 interrupt 服务器
            while (!b){
                b = Thread.currentThread().isInterrupted();
                try {
                    Socket socket = serverSocket.accept();
                    SocketWrapper socketWrapper = new SocketWrapper(socket);
                    workers.add( (Worker) workerImplClassCtor.newInstance(socketWrapper, index++));
                    workers.get(index - 1).start();
                } catch (ClosedByInterruptException e){
                    e.printStackTrace();
                    break;
                } catch (IOException e) {
                    if ("socket closed".equals(e.getMessage())) {
                        serverLog.info("[server] server status is {}", e.getMessage());
                    } else {
                        serverLog.error("[server] server error is {}", e.getMessage());
                    }
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                    break;
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    serverLog.error("没有实例化的worker对象，没有继承workContent方法");
                    e.printStackTrace();
                    break;
                }
            }

            // interrupt all workers
            for (Worker worker : workers) {
                worker.interrupt();
            }

        }
    }




//    public static void main(String[] args) throws InterruptedException {
////        for (int i = 0; i < 100; i++) {
////            serverMan.accept();
////            Thread.sleep(1000);
////            serverMan.interruptAll();
////            Thread.sleep(1000);
////            serverMan.accept();
////            Thread.sleep(1000);
////            serverMan.interruptAll();
////            Thread.sleep(1000);
////        }
//
//        serverMan.accept();
//        new Thread(() -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            serverMan.interruptAll();
//        }).start();
//
//    }
}
