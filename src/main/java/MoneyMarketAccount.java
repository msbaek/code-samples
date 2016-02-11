public class MoneyMarketAccount {
	private double balance;

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getInterestEarned() {
		return balance * .04;
	}
}
