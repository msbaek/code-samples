package wewlc.ch21;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Command {
    protected void writeField(OutputStream outputStream, String name) throws IOException {
        outputStream.write(name.getBytes());
        outputStream.write(0x00);
    }

    public void write(OutputStream outputStream) throws Exception {
        outputStream.write(getHeader());
        outputStream.write(getSize());
        outputStream.write(getCommandChar());
        writeBody(outputStream);
        outputStream.write(getFooter());
    }

    protected abstract int getSize();

    protected abstract byte[] getFooter();

    protected abstract byte[] getCommandChar();

    protected abstract byte[] getHeader();

    protected abstract void writeBody(OutputStream outputStream) throws IOException;
}
