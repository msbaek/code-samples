package util;

import org.junit.Test;

import java.text.MessageFormat;
import java.util.Date;

public class MessageFormatTest {
    @Test
    public void name() throws Exception {
        int planet = 7;

        System.out.println(
                MessageFormat.format("Hello {0}, {1}", "You", "He"));

        System.out.println(MessageFormat.format(
                "At {1,time} on {1,date}, there was {2} on planet {0,number,integer}.",
                planet, new Date()));


        int fileCount = 1273;
        String diskName = "MyDisk";
        Object[] testArgs = {new Long(fileCount), diskName};
        System.out.println(new MessageFormat(
                "The disk \"{1}\" contains {0} file(s).").format(testArgs));
    }
}
