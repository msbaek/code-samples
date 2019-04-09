package wewlc.ch21;

import java.io.IOException;
import java.io.OutputStream;

public class LoginCommand extends Command {
    private String userName;
    private String passwd;
    private static final byte[] commandChar = {0x01};

    public LoginCommand(String userName, String passwd) {
        this.userName = userName;
        this.passwd = passwd;
    }

    @Override
    protected byte[] getCommandChar() {
        return commandChar;
    }

    @Override
    protected int getBodySize() {
        return userName.getBytes().length + 1 +
                passwd.getBytes().length + 1;
    }

    @Override
    protected void writeBody(OutputStream outputStream) throws IOException {
        writeField(outputStream, userName);
        writeField(outputStream, passwd);
    }
}
