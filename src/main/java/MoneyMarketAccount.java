public class MoneyMarketAccount extends Account {

	@Override
	public double getInterestEarned() {
		return getBalance() * .04;
	}
}
