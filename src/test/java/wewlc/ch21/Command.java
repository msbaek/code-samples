package wewlc.ch21;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Command {
    protected static final byte[] header = {(byte) 0xde, (byte) 0xad};
    protected static final byte[] footer = {(byte) 0xbe, (byte) 0xef};
    protected static final int SIZE_LENGTH = 1;
    protected static final int CMD_BYTE_LENGTH = 1;

    public void write(OutputStream outputStream) throws Exception {
        outputStream.write(getHeader());
        outputStream.write(getSize());
        outputStream.write(getCommandChar());
        writeBody(outputStream);
        outputStream.write(getFooter());
    }

    protected int getSize() {
        return header.length +
                SIZE_LENGTH +
                CMD_BYTE_LENGTH +
                footer.length +
                getBodySize();
    }

    protected void writeField(OutputStream outputStream, String field) throws IOException {
        outputStream.write(field.getBytes());
        outputStream.write(0x00);
    }

    protected abstract int getBodySize();

    protected byte[] getFooter() {
        return footer;
    }

    protected abstract byte[] getCommandChar();

    protected byte[] getHeader() {
        return header;
    }

    protected abstract void writeBody(OutputStream outputStream) throws IOException;
}
