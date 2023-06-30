package br.com.rbcti.common.messages;

/**
 *
 *
 * @author Renato Cunha
 *
 */
public interface SimpleMessage {

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