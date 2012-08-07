package lev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * A FileChannel setup that supports easy extraction/getting of information.
 *
 * @author Justin Swanson
 */
public class LFileChannel extends LChannel {

    FileInputStream iStream;
    FileChannel iChannel;

    /**
     *
     */
    public LFileChannel() {
    }

    /**
     *
     * @param path Path to open a channel to.
     * @throws FileNotFoundException
     */
    public LFileChannel(final String path) throws FileNotFoundException {
	openFile(path);
    }

    /**
     *
     * @param f File to open a channel to.
     * @throws FileNotFoundException
     */
    public LFileChannel(final File f) throws FileNotFoundException {
	openFile(f);
    }

    /**
     *
     * @param path Path to open a channel to.
     * @throws FileNotFoundException
     */
    final public void openFile(final String path) throws FileNotFoundException {
	iStream = new FileInputStream(path);
	iChannel = iStream.getChannel();
    }

    /**
     *
     * @param f File to open a channel to.
     * @throws FileNotFoundException
     */
    final public void openFile(final File f) throws FileNotFoundException {
	openFile(f.getPath());
    }

    /**
     * Reads in a byte and moves forward one position in the file.
     *
     * @return The next int in the file.
     * @throws IOException
     */
    @Override
    final public int read() throws IOException {
	return iStream.read();
    }

    /**
     * Reads in the desired bytes.
     *
     * @param skip Bytes to skip
     * @param read Bytes to read and convert
     * @return
     * @throws IOException
     */
    @Override
    final public byte[] readInBytes(final int skip, final int read) throws IOException {
	offset(skip);
	ByteBuffer allocate = ByteBuffer.allocate(read);
	iChannel.read(allocate);
	return allocate.array();
    }

    /**
     * Reads in the desired bytes and wraps them in a ByteBuffer.
     *
     * @param skip Bytes to skip
     * @param read Bytes to read and convert
     * @return ByteBuffer containing read bytes.
     * @throws IOException
     */
    final public ByteBuffer readInByteBuffer(int skip, int read) throws IOException {
	offset(skip);
	ByteBuffer buf = ByteBuffer.allocate(read);
	iChannel.read(buf);
	buf.flip();
	return buf;
    }

    /**
     *
     * @param pos Position to move to.
     * @throws IOException
     */
    @Override
    final public void pos(long pos) throws IOException {
	iChannel.position(pos);
    }

    /**
     *
     * @return Current position.
     * @throws IOException
     */
    @Override
    final public long pos() throws IOException {
	return iChannel.position();
    }

    /**
     * Closes streams.
     *
     * @throws IOException
     */
    @Override
    final public void close() throws IOException {
	if (iStream != null) {
	    iStream.close();
	    iChannel.close();
	}
    }

    /**
     *
     * @return Bytes left to read in the file.
     * @throws IOException
     */
    @Override
    final public int available() throws IOException {
	return iStream.available();
    }
}
