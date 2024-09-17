import java.util.HashMap;
import java.util.LinkedList;

public class Balances
{
	private HashMap<String, Double> balances = new HashMap<String, Double>();
	private LinkedList<String> accountsList = new LinkedList<String>();

	public void addAccount(String accountName)
	{
		accountsList.add(accountName);
		balances.put(accountName, 0.0);
	}

	public void addAmount(String account, double amount)
	{
		if(balances.containsKey(account))
		{
			balances.put(account, balances.get(account)+amount);
		}
		else
		{
			accountsList.add(account);
			balances.put(account, amount);
		}
	}

	public String getNameOf(int i)
	{
		return accountsList.get(i);
	}

	public double getAmount(String account)
	{
		return balances.get(account);
	}

	public String[][] getBalances()
	{
		String[][] result = new String[accountsList.size()][2];
		for(int i=0; i<accountsList.size(); i++)
		{
			result[i][0] = accountsList.get(i);
			result[i][1] = String.format("%.2f", getAmount(accountsList.get(i))); //String.valueOf(getAmount(accountsList.get(i)));
		}
		return result;
	}

	public LinkedList<String> getAccountsList()
	{
		return accountsList;
	}

	public String[] getAccountsListStringArray()
	{
		String[] result = new String[accountsList.size()];
		for(int i=0; i<accountsList.size(); i++)
		{
			result[i] = accountsList.get(i);
		}
		return result;
	}
}