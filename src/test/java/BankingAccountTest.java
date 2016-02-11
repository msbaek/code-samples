import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class BankingAccountTest {
	private static Account myAccount;

	public static class SavingsAccountContext {
		@Before
		public void setUp() throws Exception {
			myAccount = new SavingsAccount();
		}

		@Test
		public void accountWithBalanceOf100_earns3InInterest() {
			myAccount.setBalance(100.0);

			assertThat(myAccount.getInterestEarned(), is(closeTo(3.0, 0.001)));
		}

		@Test
		public void accountWithBalanceOf200_earns6InInterest() {
			myAccount.setBalance(200.0);

			assertThat(myAccount.getInterestEarned(), is(closeTo(6.0, 0.001)));
		}
	}

	public static class MoneyMarketAccountContext {
		@Before
		public void setUp() throws Exception {
			myAccount = new MoneyMarketAccount();
		}

		@Test
		public void accountWithBalanceOf100_earns4InInterest() {
			myAccount.setBalance(100.0);

			assertThat(myAccount.getInterestEarned(), is(closeTo(4.0, 0.001)));
		}

		@Test
		public void accountWithBalanceOf200_earns8InInterest() {
			myAccount.setBalance(200.0);

			assertThat(myAccount.getInterestEarned(), is(closeTo(8.0, 0.001)));
		}
	}
}
