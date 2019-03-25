package wewlc.ch21;

import java.io.IOException;
import java.io.OutputStream;

public class LoginCommand extends Command {
    private String userName;
    private String passwd;
    private static final byte[] header = {(byte) 0xde, (byte) 0xad};
    private static final byte[] commandChar = {0x01};
    private static final byte[] footer = {(byte) 0xbe, (byte) 0xef};
    private static final int SIZE_LENGTH = 1;
    private static final int CMD_BYTE_LENGTH = 1;

    public LoginCommand(String userName, String passwd) {
        this.userName = userName;
        this.passwd = passwd;
    }

    private int getSize() {
        return getHeader().length + SIZE_LENGTH + CMD_BYTE_LENGTH +
                getFooter().length + userName.getBytes().length + 1 +
                passwd.getBytes().length + 1;
    }

    public void write(OutputStream outputStream) throws Exception {
        outputStream.write(getHeader());
        outputStream.write(getSize());
        outputStream.write(getCommandChar());
        writeBody(outputStream);
        outputStream.write(getFooter());
    }

    private byte[] getFooter() {
        return footer;
    }

    private byte[] getCommandChar() {
        return commandChar;
    }

    private byte[] getHeader() {
        return header;
    }

    private void writeBody(OutputStream outputStream) throws IOException {
        writeField(outputStream, userName);
        writeField(outputStream, passwd);
    }
}
