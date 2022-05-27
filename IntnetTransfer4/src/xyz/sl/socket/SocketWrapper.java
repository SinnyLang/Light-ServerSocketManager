package xyz.sl.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *  提供网络交互中的基本的输入输出（基本流操作）
 *  嫌流操作太少不够使用的可以自己可以添加
 */
public class SocketWrapper {
    private Logger log = LoggerFactory.getLogger("socket");
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public SocketWrapper(Socket socket, Logger logger){
        this(socket);
        log = logger;
    }

    public SocketWrapper(Socket socket) {
        this.socket = socket;
        try {
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log.error("数据流获取失败" + e.toString());
            e.printStackTrace();
        }
    }

    public void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public byte[] readBytes(){
        byte[] tmp = new byte[1024]; // 默认 8192bit 最为合适，但是这里交互比较小的数据所以就用 1024
        byte[] rtn = null;
        try {
            int len = dis.read(tmp);
            rtn = new byte[len];
            System.arraycopy(tmp, 0, rtn, 0, len);
        } catch (IOException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
        return rtn;
    }

    public void writeBytes(byte[] b){
        try {
            dos.write(b);
        } catch (IOException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
    }
}
