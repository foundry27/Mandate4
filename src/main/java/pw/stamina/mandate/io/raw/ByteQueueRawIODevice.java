package pw.stamina.mandate.io.raw;

/**
 * @author Mark Johnson
 */
public class ByteQueueRawIODevice implements RawIODevice {

    private final int deviceID;

    private byte[] buffer;

    private int head;

    private int tail;

    public ByteQueueRawIODevice(final int deviceID, final int initialCapacity) {
        this.deviceID = deviceID;
        this.buffer = new byte[initialCapacity];
    }

    public ByteQueueRawIODevice(final int deviceID) {
        this(deviceID, 32);
    }

    @Override
    public int read() throws RawIOException {
        if (tail == 0) {
            return -1;
        }
        final int val = buffer[head++] & 0xFF;
        if (head == tail) {
            head = tail = 0;
        }
        return val;
    }

    @Override
    public void write(final int b) throws RawIOException {
        if (tail + 1 == buffer.length) {
            final byte[] newBuffer = new byte[buffer.length * 2];
            System.arraycopy(buffer, head, newBuffer, 0, tail - head);
            buffer = newBuffer;
            tail -= head;
            head = 0;
        }
        buffer[tail++] = (byte) (b & 0xFF);
    }

    @Override
    public int getID() {
        return deviceID;
    }
}
