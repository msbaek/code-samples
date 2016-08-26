package fpij;

import java.util.function.Consumer;

public class FluentMailer {
	private FluentMailer() {
	}

	public FluentMailer from(final String address) { /* ... */
		;
		return this;
	}

	public FluentMailer to(final String address) { /* ... */
		;
		return this;
	}

	public FluentMailer subject(final String line) { /* ... */
		;
		return this;
	}

	public FluentMailer body(final String message) { /* ... */
		;
		return this;
	}

	public static void send(final Consumer<FluentMailer> block) {
		final FluentMailer mailer = new FluentMailer();
		block.accept(mailer);
		System.out.println("sending...");
	}
}
