package lev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * A FileChannel setup that supports easy extraction/getting of information.
 *
 * @author Justin Swanson
 */
public class LFileChannel extends LChannel {

    FileInputStream iStream;
    FileChannel iChannel;
    long end;

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
    public LFileChannel(final String path) throws IOException {
	openFile(path);
    }

    /**
     *
     * @param f File to open a channel to.
     * @throws FileNotFoundException
     */
    public LFileChannel(final File f) throws IOException {
	openFile(f);
    }

    public LFileChannel(LStream rhs, long allocation) throws IOException {
	if (rhs.getClass() == getClass()) {
	    LFileChannel fc = (LFileChannel) rhs;
	    iStream = fc.iStream;
	    iChannel = fc.iChannel;
	    end = iChannel.position() + allocation;
	} else {
	    throw new IOException ("LFileChannel couldn't copy a non-file channel stream.");
	}
    }

    /**
     *
     * @param path Path to open a channel to.
     * @throws FileNotFoundException
     */
    final public void openFile(final String path) throws IOException {
	iStream = new FileInputStream(path);
	iChannel = iStream.getChannel();
	end = iChannel.size();
    }

    /**
     *
     * @param f File to open a channel to.
     * @throws FileNotFoundException
     */
    final public void openFile(final File f) throws IOException {
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

    @Override
    public Boolean isDone() throws IOException {
	return iChannel.position() == end;
    }

    @Override
    public int remaining() throws IOException {
	return (int) (end - iChannel.position());
    }

    @Override
    public void skip(int skip) throws IOException {
	iChannel.position(iChannel.position() + skip);
    }

    @Override
    public void jumpBack(int amount) throws IOException {
	skip(-amount);
    }

    @Override
    public byte[] extract(int amount) throws IOException {
	byte[] out = new byte[amount];
	iStream.read(out);
	return out;
    }

    @Override
    public byte[] extractUntil(int delimiter) throws IOException {
	int counter = 1;
	while (!isDone()) {
	    if (iStream.read() != delimiter) {
		counter++;
	    } else {
		jumpBack(counter);
		byte[] out = extract(counter - 1);
		skip(1);
		return out;
	    }
	}
	return new byte[0];
    }
}
