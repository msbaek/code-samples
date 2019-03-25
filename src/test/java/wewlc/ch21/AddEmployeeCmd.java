package wewlc.ch21;

import java.io.IOException;
import java.io.OutputStream;

public class AddEmployeeCmd extends Command {
    String name;
    String address;
    String city;
    String state;
    String yearlySalary;
    private static final byte[] header = {(byte) 0xde, (byte) 0xad};
    private static final byte[] commandChar = {0x02};
    private static final byte[] footer = {(byte) 0xbe, (byte) 0xef};
    private static final int SIZE_LENGTH = 1;
    private static final int CMD_BYTE_LENGTH = 1;

    private int getSize() {
        return getHeader().length +
                SIZE_LENGTH +
                CMD_BYTE_LENGTH +
                getFooter().length +
                name.getBytes().length + 1 +
                address.getBytes().length + 1 +
                city.getBytes().length + 1 +
                state.getBytes().length + 1 +
                yearlySalary.getBytes().length + 1;
    }

    public AddEmployeeCmd(String name, String address,
                          String city, String state,
                          int yearlySalary) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.yearlySalary = Integer.toString(yearlySalary);
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
        writeField(outputStream, this.name);
        writeField(outputStream, address);
        writeField(outputStream, city);
        writeField(outputStream, state);
        writeField(outputStream, yearlySalary);
    }
}
