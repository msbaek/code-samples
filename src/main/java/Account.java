/**
 * Created by msbaek on 2016. 2. 11..
 */
public abstract class Account {
	private double balance;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public abstract double getInterestEarned();
}
