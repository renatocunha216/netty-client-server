package br.com.rbcti.common.messages;

/**
 *
 * Interface that all messages in this project must implement.
 *
 * @author Renato Cunha
 *
 */
public interface SimpleMessage {

    /**
     *
     * @return
     */
    public abstract long getUsn();

    /**
     *
     * @return
     */
    public abstract int getLength();

    /**
     *
     * @return
     */
    public abstract int getId();

    /**
     *
     * @return
     */
    public abstract short getVersion();

    /**
     *
     * @return
     */
    public abstract byte[] getData();

}