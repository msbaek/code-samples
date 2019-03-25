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

    @Override
    protected int getSize() {
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

    @Override
    protected byte[] getFooter() {
        return footer;
    }

    @Override
    protected byte[] getCommandChar() {
        return commandChar;
    }

    @Override
    protected byte[] getHeader() {
        return header;
    }

    @Override
    protected void writeBody(OutputStream outputStream) throws IOException {
        writeField(outputStream, this.name);
        writeField(outputStream, address);
        writeField(outputStream, city);
        writeField(outputStream, state);
        writeField(outputStream, yearlySalary);
    }
}
