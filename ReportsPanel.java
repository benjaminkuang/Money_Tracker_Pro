import javax.swing.*;
import java.util.LinkedList;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class ReportsPanel
{
	Transactions transactions;
	Transactions currentTransactions;
	Balances balances;
	Balances currentBalances;

	JPanel reportsPanel = new JPanel();
	String[][] monthsData = new String[33][1];
	String[] monthsColumnNames = {"Months"};
	JTable monthsTable = new JTable(monthsData,monthsColumnNames);
	String[][] balancesData = new String[33][2];
	String[] balancesColumnNames = {"Accounts", "Balances"};
	JTable balancesTable = new JTable(balancesData,balancesColumnNames);
	String[][] transactionsData = new String[200][5];
	String[] transactionsColumnNames = {"Date", "Amount", "From", "To", "Notes"};
	JTable transactionsTable = new JTable(transactionsData,transactionsColumnNames);

	JScrollPane monthsTableScrollPane = new JScrollPane(monthsTable);
	JScrollPane balancesTableScrollPane = new JScrollPane(balancesTable);
	JScrollPane transactionsTableScrollPane = new JScrollPane(transactionsTable);

	String currentMonth = "";
	String currentAccount = "";

	public ReportsPanel(Transactions inputTransactions)
	{
		transactions = inputTransactions;
		currentTransactions = transactions;
		balances = inputTransactions.getBalanceObject();
		currentBalances = balances;

		reportsPanel.setLayout(new BoxLayout(reportsPanel, BoxLayout.LINE_AXIS));

		monthsTable.getSelectionModel().addListSelectionListener(new monthsTableListSelectionListener());
		balancesTable.getSelectionModel().addListSelectionListener(new balancesTableListSelectionListener());

		monthsTableScrollPane.setPreferredSize(new Dimension(134,500));
		balancesTableScrollPane.setPreferredSize(new Dimension(150,500));
		transactionsTableScrollPane.setPreferredSize(new Dimension(500,500));

		reportsPanel.add(monthsTableScrollPane);
		reportsPanel.add(balancesTableScrollPane);
		reportsPanel.add(transactionsTableScrollPane);

		repaintMonthsTable();
		repaintBalancesTable();
		repaintTransactionsTable();
	}

	public JPanel getPanel()
	{
		return reportsPanel;
	}

	public void repaintMonthsTable()
	{
		String previousMonth = "";
		String currentMonth = "";
		int monthsIncrement = 0;
		//Blank months table
		for(int i=0; i<monthsData.length; i++)
		{
			monthsData[i][0] = "";
		}
		//If the number of transactions is not zero
		if(transactions.getNumberTransactions() != 0)
		{
			//There are transactions
			for(int i=0; i<transactions.getNumberTransactions(); i++)
			{
				//Set the currentMonth string
				currentMonth = transactions.getTransaction(i).getMonth();
				//Compare if the months change
				if(!previousMonth.equals(currentMonth))
				{
					//Previous month is now current month
					previousMonth = currentMonth;
					//Add it to the months data array
					monthsData[monthsIncrement][0] = currentMonth + " " + transactions.getTransaction(i).getYear();
					//Increment the incrementer
					monthsIncrement++;
				}
			}
		}
		monthsTable.repaint();
	}

	public void repaintBalancesTable()
	{
		String[][] result = currentBalances.getBalances();
		//Blank the balances table
		for(int i=0; i<balancesData.length; i++)
		{
			balancesData[i][0] = "";
			balancesData[i][1] = "";
		}
		//For each index in result
		for(int i=0; i<result.length; i++)
		{
			balancesData[i][0] = result[i][0];
			balancesData[i][1] = result[i][1];
		}
		balancesTable.repaint();
	}

	public void repaintTransactionsTable()
	{
		String[][] result = currentTransactions.getTransactionsArray();
		//Blank the transactions table
		for(int i=0; i<transactionsData.length; i++)
		{
			transactionsData[i][0] = "";
			transactionsData[i][1] = "";
			transactionsData[i][2] = "";
			transactionsData[i][3] = "";
			transactionsData[i][4] = "";
		}

		for(int i=0; i<result.length; i++)
		{
			transactionsData[i][0] = result[i][0];
			transactionsData[i][1] = result[i][1];
			transactionsData[i][2] = result[i][2];
			transactionsData[i][3] = result[i][3];
			transactionsData[i][4] = result[i][4];
		}
		transactionsTable.repaint();
	}

	private class monthsTableListSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			String[] result = {""};
			String month = "";

			//If the row is not blank
			if(!monthsData[monthsTable.getSelectedRow()][0].equals(""))
			{
				result = monthsData[monthsTable.getSelectedRow()][0].split(" ");
				currentMonth = result[0];
			}
			else
			{
				currentMonth = "";
			}

			switch(currentMonth) {
				case "JANUARY":
					month = "01";
					break;
				case "FEBRUARY":
					month = "02";
					break;
				case "MARCH":
					month = "03";
					break;
				case "APRIL":
					month = "04";
					break;
				case "MAY":
					month = "05";
					break;
				case "JUNE":
					month = "06";
					break;
				case "JULY":
					month = "07";
					break;
				case "AUGUST":
					month = "08";
					break;
				case "SEPTEMBER":
					month = "09";
					break;
				case "OCTOBER":
					month = "10";
					break;
				case "NOVEMBER":
					month = "11";
					break;
				case "DECEMBER":
					month = "12";
					break;
				case "":
					currentTransactions = transactions;
					currentBalances = transactions.getBalanceObject();
					break;
			}

			//If there is a currentMonth
			if(!currentMonth.equals(""))
			{
				LocalDate firstOfMonth = LocalDate.parse(result[1]+"-"+month+"-"+"01");
				LocalDate lastOfMonth = firstOfMonth.with(TemporalAdjusters.lastDayOfMonth());
				//Always the first day
				//ISO Date is Year Month Day YYYY-MM-DD
				LinkedList<Transaction> transactionsList = transactions.getTransactionsDateRange(firstOfMonth, lastOfMonth);
				currentTransactions = new Transactions();
				for(int i=0; i<transactionsList.size(); i++)
				{
					currentTransactions.addTransactions(transactionsList.get(i));
				}
				currentBalances = currentTransactions.getBalanceObject();
			}

			balancesTable.clearSelection();
			repaintBalancesTable();
			repaintTransactionsTable();
		}
	}

	private class balancesTableListSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{

			try
			{
				currentAccount = balancesData[balancesTable.getSelectedRow()][0];
			}
			catch(ArrayIndexOutOfBoundsException exception)
			{
				currentAccount = "";
			}

			String[][] result = currentTransactions.getTransactionsArray();
			int transactionsArrayIterator = 0;
			//Blank the transactions table
			for(int i=0; i<transactionsData.length; i++)
			{
				transactionsData[i][0] = "";
				transactionsData[i][1] = "";
				transactionsData[i][2] = "";
				transactionsData[i][3] = "";
				transactionsData[i][4] = "";
			}
			//For each index in the result array
			for(int i=0; i<result.length; i++)
			{
				//If either the from or to account name equals the selected account
				if(result[i][2].equals(currentAccount) || result[i][3].equals(currentAccount))
				{
					//Place it into the data array
					transactionsData[transactionsArrayIterator][0] = result[i][0];
					transactionsData[transactionsArrayIterator][1] = result[i][1];
					transactionsData[transactionsArrayIterator][2] = result[i][2];
					transactionsData[transactionsArrayIterator][3] = result[i][3];
					transactionsData[transactionsArrayIterator][4] = result[i][4];
					//Increment the iterator
					transactionsArrayIterator++;
				}
				//If currentAccount is blank just add everything
				else if(currentAccount.equals(""))
				{
					transactionsData[i][0] = result[i][0];
					transactionsData[i][1] = result[i][1];
					transactionsData[i][2] = result[i][2];
					transactionsData[i][3] = result[i][3];
					transactionsData[i][4] = result[i][4];
				}
			}
			transactionsTable.repaint();
		}
	}
}