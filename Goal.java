public class Goal
{
	String name;
	double target;
	double left;
	Transactions transactions;
	int numTransactions = 0;

	public Goal(String inputName, double inputTarget)
	{
		name = inputName;
		target = inputTarget;
		left = inputTarget;
		transactions = new Transactions();
	}

	public void addTransaction(Transaction inputTransaction)
	{
		left = left - inputTransaction.getAmount();
		transactions.addTransactions(inputTransaction);
		numTransactions++;
	}

	public Transactions getTransactions()
	{
		return transactions;
	}

	public String getName()
	{
		return name;
	}

	public double getTarget()
	{
		return target;
	}

	public double getLeft()
	{
		return left;
	}

	public void setWithdraw(double withdraw)
	{
		left = left + withdraw*2; //Need to double to counteract the addTransaction action
	}

	public int getNumberTransactions()
	{
		return numTransactions;
	}
}