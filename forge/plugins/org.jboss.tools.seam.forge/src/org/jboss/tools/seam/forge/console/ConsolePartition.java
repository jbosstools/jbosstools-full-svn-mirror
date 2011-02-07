package org.jboss.tools.seam.forge.console;

import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;

/**
 * A region in an IOConsole's document.
 * 
 * @since 3.1
 */
public class ConsolePartition implements ITypedRegion {
	public static final String OUTPUT_PARTITION_TYPE = ConsolePlugin.getUniqueIdentifier() + ".io_console_output_partition_type"; //$NON-NLS-1$
	public static final String INPUT_PARTITION_TYPE = ConsolePlugin.getUniqueIdentifier() + ".io_console_input_partition_type"; //$NON-NLS-1$

	/**
	 * The data contained by this partition.
	 */
	private StringBuffer buffer;
    private String type;
    private int offset;
    /**
     * Output partitions are all read only.
     * Input partitions are read only once they have been appended to the console's input stream.
     */
    private boolean readOnly;
    
    /**
     * Only one of inputStream or outputStream will be null depending on the partitions type.
     */
    private ConsoleOutputStream outputStream;
    private ConsoleInputStream inputStream;
    private int length;
    
    /**
     * Creates a new partition to contain output to console.
     */
    public ConsolePartition(ConsoleOutputStream outputStream, int length) {
        this.outputStream = outputStream;
        this.length = length;
        this.type = OUTPUT_PARTITION_TYPE;
        this.readOnly = true;
    }
    
    /**
     * Creates a new partition to contain input from a console
     */
    public ConsolePartition(ConsoleInputStream inputStream, String text) {
        this.inputStream = inputStream;
        buffer = new StringBuffer(text);
        length = text.length();
        this.type = INPUT_PARTITION_TYPE;
        this.readOnly = false;
    }
    
    /**
     * Inserts a string into this partition.
     * @param s The string to insert
     * @param offset the offset in the partition
     */
    public void insert(String s, int insertOffset) {
        buffer.insert(insertOffset, s);
        length += s.length();
    }
      
    /**
     * Deletes data from this partition.
     * @param delOffset
     * @param delLength
     */
    public void delete(int delOffset, int delLength) {
        buffer.delete(delOffset, delOffset+delLength);
        length -= delLength;
    }
    
    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.text.ITypedRegion#getType()
     */
    public String getType() {
        return type;
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.text.IRegion#getLength()
     */
    public int getLength() {
        return length;
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.text.IRegion#getOffset()
     */
    public int getOffset() {
        return offset;
    }
    
    /**
     * Sets this partitions offset in the document.
     * 
     * @param offset This partitions offset in the document.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    /**
     * Sets this partition's length.
     * 
     * @param length
     */
    public void setLength(int length) {
    	this.length = length;
    }
    
    /**
     * Returns the data contained in this partition.
     * @return The data contained in this partition.
     */
    public String getString() {
        return buffer.toString();
    }
    
    /**
     * Returns a StyleRange object which may be used for setting the style
     * of this partition in a viewer.
     */
    public StyleRange getStyleRange(int rangeOffset, int rangeLength) {
        return new StyleRange(rangeOffset, rangeLength, getColor(), null, getFontStyle());
    }

    /**
     *  Returns the font of the input stream if the type of the partition 
     * is <code>INPUT_PARTITION_TYPE</code>, otherwise it returns the output 
     * stream font
     * 
     * @return the font of one of the backing streams
     */
    private int getFontStyle() {
        if (type.equals(INPUT_PARTITION_TYPE)) {
            return inputStream.getFontStyle();
        } 
        return outputStream.getFontStyle();
    }

    /**
     * Returns the colour of the input stream if the type of the partition 
     * is <code>INPUT_PARTITION_TYPE</code>, otherwise it returns the output 
     * stream colour
     * 
     * @return the colour of one of the backing streams
     */
    public Color getColor() {
        if (type.equals(INPUT_PARTITION_TYPE)) {
            return inputStream.getColor();
        } 
        return outputStream.getColor();
    }

    /**
     * Returns if this partition is read-only.
     * 
     * @see org.eclipse.ui.console.IConsoleDocumentPartitioner#isReadOnly(int)
     * @return if this partition is read-only
     */
    public boolean isReadOnly() {
        return readOnly;
    }
    
    /**
     * Sets the read-only state of this partition to <code>true</code>.
     * 
     * @see org.eclipse.ui.console.IConsoleDocumentPartitioner#isReadOnly(int)
     */
    public void setReadOnly() {
        readOnly = true;
    }

    /**
     * Clears the contents of the buffer
     */
    public void clearBuffer() {
        buffer.setLength(0);
    }
    
    /**
     * Returns the underlying output stream
     * 
     * @return the underlying output stream
     */
    ConsoleOutputStream getStream() {
        return outputStream;
    }
}
