package xyz.sl.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public abstract class WorkClient extends Thread{
    public Logger clientLog = LoggerFactory.getLogger("client");

    protected Socket socket;
    protected SocketWrapper socketWrapper;
    private int port;
    private String host;

    public WorkClient(String name, String host, int port){
        super(name);
        this.host = host;
        this.port = port;
    }



    public void conn() {
        try {
            socket = new Socket(host, port, null, 11299);
            socketWrapper = new SocketWrapper(socket, clientLog);
            clientLog.info("成功连接远程主机");
            this.start();
        } catch (IOException e) {
            clientLog.info("["+getName()+"]连接失败 "+e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        workContent();
        socketWrapper.closeSocket();
        clientLog.info("关闭远程连接");
    }

    /** 需要自己重写workContent工作内容
     需要自己处理异常 */
    public abstract void workContent();
}
