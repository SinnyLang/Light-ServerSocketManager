package xyz.sl.util.socket;

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

    private MessageDispatcher dispatcher = o -> 0;

    public WorkClient(String name, String host, int port){
        super(name);
        this.host = host;
        this.port = port;
    }

    public WorkClient(String name, String host, int port, MessageDispatcher dispatcher){
        this(name, host, port);
        setDispatcher(dispatcher);
    }

    public void conn() {
        try {
            socket = new Socket(host, port);
            socketWrapper = new SocketWrapper(socket, clientLog);
            clientLog.info("成功连接远程主机 =====> " + host + ":" + port);
            this.start();
        } catch (IOException e) {
            clientLog.info("["+getName()+"]连接失败 ==X==> "+ host + ":" + port + " reason: " +e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        workContent(dispatcher);
        socketWrapper.closeSocket();
        clientLog.info("关闭远程连接 ==/==> " + host + ":" + port);
    }

    /** 需要自己重写workContent工作内容
     需要自己处理异常 */
    public abstract void workContent(MessageDispatcher dispatcher);

    /** 需要设置消息转发器才能将socket接收到的消息传递出去
     正是有了 MessageDispatcher，该socket通讯部分才能变成模块化 */
    public void setDispatcher(MessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
}
