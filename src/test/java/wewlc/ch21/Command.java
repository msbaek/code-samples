package wewlc.ch21;

import java.io.IOException;
import java.io.OutputStream;

public class Command {
    protected void writeField(OutputStream outputStream, String name) throws IOException {
        outputStream.write(name.getBytes());
        outputStream.write(0x00);
    }
}
