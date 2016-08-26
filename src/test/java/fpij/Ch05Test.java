package fpij;

import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static fpij.Ch05Test.FileWriterEAM.use;
import static fpij.Ch05Test.Locker.runLocked;
import static junit.framework.TestCase.fail;

public class Ch05Test {
	class FileWriterExample {
		private final FileWriter writer;

		public FileWriterExample(final String fileName) throws IOException {
			writer = new FileWriter(fileName);
		}

		public void writeStuff(final String message) throws IOException {
			writer.write(message);
		}

		public void close() throws IOException {
			writer.close();
		}
		// ...
	}

	class FileWriterARM implements AutoCloseable {
		private final FileWriter writer;

		public FileWriterARM(final String fileName) throws IOException {
			writer = new FileWriter(fileName);
		}

		public void writeStuff(final String message) throws IOException {
			writer.write(message);
		}

		public void close() throws IOException {
			System.out.println("close called automatically...");
			writer.close();
		}
		// ...
	}

	@Test
	public void ensuring_code__arm_try_with_resource() throws IOException {
		// FileWriterExample writerExample = new FileWriterExample("peekaboo.txt");
		// writerExample.writeStuff("peek-a-boo");
		// writerExample.close();

		// ensuring close
		FileWriterExample writerExample = new FileWriterExample("peekaboo.txt");
		try {
			writerExample.writeStuff("peek-a-boo");
		} finally {
			writerExample.close();
		}

		// Java 7 ARM(Automatic Resource Management)
		// try-with-resources
		try (FileWriterARM writerARM = new FileWriterARM("peekaboo.txt")) {
			writerARM.writeStuff("peek-a-boo");
			System.out.println("done with resource...");
		}
	}

	static class FileWriterEAM { // EAM: Execution Arround Method
		private final FileWriter writer;

		public FileWriterEAM(final String fileName) throws IOException {
			writer = new FileWriter(fileName);
		}

		public void writeStuff(final String message) throws IOException {
			writer.write(message);
		}

		private void close() throws IOException {
			System.out.println("close called automatically...");
			writer.close();
		}

		// ...
		public static void use(String fileName, UseInstance<FileWriterEAM, IOException> block) throws IOException {
			final FileWriterEAM writerEAM = new FileWriterEAM(fileName);
			try {
				block.accept(writerEAM);
			} finally {
				writerEAM.close();
			}
		}
	}

	@Test
	public void using_lambda_to_clean_up_resources() throws IOException {
		use("peekboo.txt", instance -> instance.writeStuff("peek-a-boo"));
	}

	class Locking {
		Lock lock = new ReentrantLock(); // or mock

		protected void setLock(final Lock mock) {
			lock = mock;
		}

		public void doOp1() {
			lock.lock();
			try {
				// ...critical code...
			} finally {
				lock.unlock();
			}
		}
		// ...
	}

	static class Locker {
		public static void runLocked(Lock lock, Runnable block) {
			lock.lock();
			try {
				block.run();
			} finally {
				lock.unlock();
			}
		}
	}

	@Test
	public void using_lambda_to_fine_control_locks() {
		Lock lock = new ReentrantLock();
		runLocked(lock, () -> {
		});
	}

	static class TestHelper {
		public static <X extends Throwable> Throwable assertThrows
				(final Class<X> exceptionClass, final Runnable block) {
			try {
				block.run();
			} catch (Throwable ex) {
				if (exceptionClass.isInstance(ex))
					return ex;
			}
			fail("Failed to throw expected exception ");
			return null;
		}
	}

	@Test
	public void creating_concise_exception_tests() {

	}
}
