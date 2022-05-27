package xyz.sl.socket;

import static xyz.sl.socket.ServerSocketManager.serverLog;

public abstract class Worker extends Thread{

    protected SocketWrapper socketWrapper;
    protected int id;

    public Worker(SocketWrapper socketWrapper, int id) {
        this.socketWrapper = socketWrapper;
        this.id = id;
    }

    /** 需要自己重写workContent工作内容
        需要自己处理异常 */
    public abstract void workContent();

    // 处理协议的线程
    @Override
    public void run() {
        setName("connect "+id);
        serverLog.info("一个连接已建立，连接号 {}", id);
        this.workContent();
        //getHostName(); C/S 架构则是 getClientName; C/C 则是getServerName
        //verifyToken(); C/S 则验证ClientName和pass; C/C 验证serverName和token
        //this.setName("worker-"+id);
        //接下来是同步模式的对话
        //super.run();
        socketWrapper.closeSocket();
        serverLog.info("一个连接已断开，连接号 {}", id);
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }
}
