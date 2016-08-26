package fpij;

public class MailBuilder {
	public MailBuilder from(final String address) { /* ... */
		;
		return this;
	}

	public MailBuilder to(final String address) { /* ... */
		;
		return this;
	}

	public MailBuilder subject(final String line) { /* ... */
		;
		return this;
	}

	public MailBuilder body(final String message) { /* ... */
		;
		return this;
	}

	public void send() {
		System.out.println("sending...");
	}
	// ...
}
