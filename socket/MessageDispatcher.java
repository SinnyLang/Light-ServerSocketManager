package xyz.sl.util.socket;

/**
 *  该接口用于socket向前端发送数据的
 *  前端使用该模块发送消息之后，大部分情况下需要接收从远端主机
 *  返回的消息。对于此种情况，前端需要在调用该模块之前，前端需
 *  要实现 MessageDispatcher 接口，并将实例化的 Message-
 *  Dispatcher 对象一并传递给 WorkClient 或者 Worker 对象
 *
 *  例如：
 *      new WorkClientImpl(name, host, port, messageDispatcher).conn();
 *      将 dispatcher 对象传递给 WorkClient 并连接服务端
 *      当然也可以在连接服务器之前手动设置 dispatcher 对象
 *      workerClientImpl.setDispatcher(dispatcher);
 *      workerClientImpl.conn();
 *
 *  再如：
 *      serverMan.setDispatcher(dispatcher);
 *      serverMan.setWorkerImpl(WorkerImpl.class);
 *      serverMan.accept();
 *      serverManager 并不会构造一个含有 MessageDispatcher 的 Worker，
 *      因此需要在 serverManager 启动 server 之前手动设置 dispatcher
 *      在这个例子中 workerImpl 是一个继承了 Worker 的实例化对象
 *
 *          缺点就是，一个 serverManager 会为自己所有的 Worker 设置
 *          相同的 dispatcher 。也并不是没有方法解决这个问题，你可以让
 *          serverManager 暂停所有的 Worker ，然后设置一个新的
 *          dispatcher ，然后再启动所有的 Worker
 *
 *  当然如果不想对远端主机发送的消息传递到前端，则不需要上面讲到的setDispatcher
 *  步骤或者也可以传递一个什么都不做的 dispatch();  (前提是你不怕麻烦的话)
 *  例如：
 *      setDispatcher(new MessageDispatcher() {
 *             @Override
 *             public int dispatch(Object o) {
 *                 return 0;
 *             }
 *         });
 *
 *  前端处理完成消息之后，需要把消息处理的结果告诉 socket层，然后 socket层会根据
 *  dispatch返回的结果进行下一步操作
 */
public interface MessageDispatcher {

    int DO_NOTHING = 0;

    int dispatch(Object o);
}
