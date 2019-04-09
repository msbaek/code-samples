package wewlc.ch21;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommandTest {
    class TestingOutputStream extends OutputStream {
        private StringBuffer buf = new StringBuffer();

        public String getBuffer() {
            return buf.toString();
        }

        @Override
        public void write(int b) throws IOException {
            buf.append(b);
        }
    }

    @Test
    public void write() throws Exception {
        String userName = "userName";
        String passwd = "passwd";
        Command command = new LoginCommand(userName, passwd);
        OutputStream os = new TestingOutputStream();
        command.write(os);
        assertThat(((TestingOutputStream) os).getBuffer()).isEqualTo("-34-8322111711510111478971091010112971151151191000-66-17");

        String name = "name" ;
        String address = "address";
        String city = "city";
        String state = "state";
        int yearlySalary = 1000;
        Command command1 = new AddEmployeeCmd(name, address, city, state, yearlySalary);
        OutputStream os1 = new TestingOutputStream();
        command1.write(os1);
        assertThat(((TestingOutputStream) os1).getBuffer()).isEqualTo("-34-83352110971091010971001001141011151150991051161210115116971161010494848480-66-17");
    }
}