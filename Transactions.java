import java.util.ArrayList;
import java.util.LinkedList;
import java.time.LocalDate;

public class Transactions
{
	private ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();
	private Balances balances = new Balances();
	private int numTransactions = 0;
	private Transaction recentTransaction;

	public Transactions()
	{

	}

	public Balances getBalanceObject()
	{
		return balances;
	}

	public void addTransactions(String date, String from, String to, double amount, String note) //Date, From, To, Amount
	{
		//Make the adjustments to account balances
		balances.addAmount(from, -1.0*amount);
		balances.addAmount(to, amount);

		String currentString[]; //This holds an array of tokenized Strings

		if(date.contains("/"))
		{
			currentString = date.split("/");
		}
		else if(date.contains("-"))
		{
			currentString = date.split("-");
		}
		else
		{
			return;
		}
		LocalDate localDate = LocalDate.of(Integer.valueOf(currentString[2]), Integer.valueOf(currentString[0]), Integer.valueOf(currentString[1]));
		if(transactionsList.isEmpty())
		{
			recentTransaction = new Transaction(localDate, from, to, amount, note);
			transactionsList.add(recentTransaction);
		}
		else
		{
			recentTransaction = new Transaction(localDate, from, to, amount, note);
			transactionsList.add(addLocation(localDate), recentTransaction);
		}
		numTransactions++;
	}

	public void addTransactions(Transaction inputTransaction)
	{
		addTransactions(inputTransaction.getAmericanDateString(), inputTransaction.getFrom(), inputTransaction.getTo(), inputTransaction.getAmount(), inputTransaction.getNote());
	}

	public LinkedList<Transaction> getTransactionsDateRange(LocalDate fromDate, LocalDate toDate)
	{
		LinkedList<Transaction> result = new LinkedList<Transaction>();

		//Search the entire list of transactions
		for(int i=0; i<transactionsList.size(); i++)
		{
			//If the list date is equal to the asked for from or to date
			if(transactionsList.get(i).getDate().isEqual(fromDate) || transactionsList.get(i).getDate().isEqual(toDate))
			{
				//Add to the result
				result.add(transactionsList.get(i));
			}
			//Else if the date is between the from and to date
			else if(transactionsList.get(i).getDate().isAfter(fromDate) && transactionsList.get(i).getDate().isBefore(toDate))
			{
				//Add to the result
				result.add(transactionsList.get(i));
			}
		}

		return result;
	}

	public String[] getTransactionStringArray(int i)
	{
		String[] result = new String[5];
		result[0] = transactionsList.get(i).getDate().toString();
		result[1] = Double.toString(transactionsList.get(i).getAmount());
		result[2] = transactionsList.get(i).getTo();
		result[3] = transactionsList.get(i).getFrom();
		result[4] = transactionsList.get(i).getNote();
		return result;
	}

	public String[][] getTransactionsArray()
	{
		String[][] result = new String[transactionsList.size()][5];
		for(int i=0; i<transactionsList.size(); i++)
		{
			result[i][0] = transactionsList.get(i).getDate().toString();
			result[i][1] = Double.toString(transactionsList.get(i).getAmount());
			result[i][2] = transactionsList.get(i).getTo();
			result[i][3] = transactionsList.get(i).getFrom();
			result[i][4] = transactionsList.get(i).getNote();
		}
		return result;
	}

	public int getNumberTransactions()
	{
		return numTransactions;
	}

	public Transaction getTransaction(int i)
	{
		return transactionsList.get(i);
	}

	public Transaction getRecentTransaction()
	{
		return recentTransaction;
	}

	private int addLocation(LocalDate date)
	{
		if(date.isBefore(transactionsList.get(0).getDate()))
		{
			return 0;
		}
		else if(date.isAfter(transactionsList.get(transactionsList.size()-1).getDate()))
		{
			return transactionsList.size();
		}
		else
		{
			int positionStart = 0;
			int positionCurrent = transactionsList.size()/2;
			int positionEnd = transactionsList.size();
			while(positionCurrent > positionStart && positionCurrent < positionEnd)
			{
				if(date.isAfter(transactionsList.get(positionCurrent).getDate()))
				{
					positionStart = positionCurrent;
					positionCurrent = (positionEnd-positionStart)/2 + positionStart;
				}
				else
				{
					positionEnd = positionCurrent;
					positionCurrent = (positionEnd-positionStart)/2 + positionStart;
				}
			}
			if(date.isAfter(transactionsList.get(positionCurrent).getDate()))
			{
				return positionCurrent+1;
			}
			else
			{
				return positionCurrent;
			}
		}
	}
}