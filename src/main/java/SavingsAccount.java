public class SavingsAccount {
	private double balance;

	public double getInterestEarned() {
		return balance * .03;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
