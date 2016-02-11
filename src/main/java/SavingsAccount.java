public class SavingsAccount extends Account {
	public double getInterestEarned() {
		return getBalance() * .03;
	}
}
