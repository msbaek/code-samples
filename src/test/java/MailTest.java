import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MailTest {
	@Test public void newMailBox_isEmpty() {
		MailBox mailbox = null;
		assertThat(mailbox.messageCount(), is(0));
	}
}
