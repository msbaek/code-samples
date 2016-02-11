/**
 * Created by msbaek on 2016. 2. 11..
 */
public abstract class Account {
	private double balance;

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getInterestEarned() {
		return balance * getInterestRate();
	}

	protected abstract double getInterestRate();
}
