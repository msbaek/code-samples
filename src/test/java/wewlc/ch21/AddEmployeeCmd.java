package wewlc.ch21;

import java.io.IOException;
import java.io.OutputStream;

public class AddEmployeeCmd extends Command {
    String name;
    String address;
    String city;
    String state;
    String yearlySalary;
    private static final byte[] commandChar = {0x02};

    public AddEmployeeCmd(String name, String address,
                          String city, String state,
                          int yearlySalary) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.yearlySalary = Integer.toString(yearlySalary);
    }

    protected byte[] getCommandChar() {
        return commandChar;
    }

    protected int getBodySize() {
        return name.getBytes().length + 1 +
                address.getBytes().length + 1 +
                city.getBytes().length + 1 +
                state.getBytes().length + 1 +
                yearlySalary.getBytes().length + 1;
    }

    protected void writeBody(OutputStream outputStream) throws IOException {
        writeField(outputStream, name);
        writeField(outputStream, address);
        writeField(outputStream, city);
        writeField(outputStream, state);
        writeField(outputStream, yearlySalary);
        t1();
    }

    public static void t1() {
    }
}
