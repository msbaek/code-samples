import org.junit.Test;

public class MailTest {
	@Test public void newMailBox_isEmpty() {
		assertThat(mailbox.messageCount(), is(0));
	}
}
