/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/xpcom/io/nsIConverterOutputStream.idl
 */

package org.mozilla.interfaces;

/**
 * This interface allows writing strings to a stream, doing automatic
 * character encoding conversion.
 */
public interface nsIConverterOutputStream extends nsIUnicharOutputStream {

  String NS_ICONVERTEROUTPUTSTREAM_IID =
    "{4b71113a-cb0d-479f-8ed5-01daeba2e8d4}";

  /**
     * Initialize this stream. Must be called before any other method on this
     * interface, or you will crash. The output stream passed to this method
     * must not be null, or you will crash.
     *
     * @param aOutStream
     *        The underlying output stream to which the converted strings will
     *        be written.
     * @param aCharset
     *        The character set to use for encoding the characters. A null
     *        charset will be interpreted as UTF-8.
     * @param aBufferSize
     *        How many bytes to buffer. A value of 0 means that no bytes will be
     *        buffered. Implementations not supporting buffering may ignore
     *        this parameter.
     * @param aReplacementCharacter
     *        The replacement character to use when an unsupported character is found.
     *        The character must be encodable in the selected character
     *        encoding; otherwise, attempts to write an unsupported character
     *        will throw NS_ERROR_LOSS_OF_SIGNIFICANT_DATA.
     *
     *        A value of 0x0000 will cause an exception to be thrown upon
     *        attempts to write unsupported characters.
     */
  void init(nsIOutputStream aOutStream, String aCharset, long aBufferSize, int aReplacementCharacter);

}