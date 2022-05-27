package xyz.sl.socket;

/**
 *  该接口用于socket向向前端发送数据的
 */
public interface MessageDispatcher {
    void dispatch(Object o);
}
