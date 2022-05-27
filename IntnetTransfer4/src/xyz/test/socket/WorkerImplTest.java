package xyz.test.socket;

import xyz.sl.socket.SocketWrapper;
import xyz.sl.socket.Worker;

public class WorkerImplTest extends Worker {
    public WorkerImplTest(SocketWrapper socketWrapper, int id) {
        super(socketWrapper, id);
    }

    @Override
    public void workContent() {

//        String name = new String(socketWrapper.readBytes(), StandardCharsets.UTF_8);
//        this.setName(name);
//        System.out.println("worker: " + this.getName() + " " +this.id);
//        socketWrapper.writeBytes(("hello" + getName()).getBytes(StandardCharsets.UTF_8));
    }
}
