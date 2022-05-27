package xyz.test.socket;

import xyz.sl.socket.WorkClient;

public class WorkClientImpl extends WorkClient {
    public WorkClientImpl(String name, String host, int port) {
        super(name, host, port);
    }

    @Override
    public void workContent() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(getName()+
//                " 连接成功host:"+socket.getInetAddress().toString()+
//                        " port:"+socket.getPort())
//        ;
//        socketWrapper.writeBytes(getName().getBytes(StandardCharsets.UTF_8));
//        String rcv = new String(socketWrapper.readBytes(), StandardCharsets.UTF_8);
//        System.out.println("服务器返回 "+ rcv);
    }
}
