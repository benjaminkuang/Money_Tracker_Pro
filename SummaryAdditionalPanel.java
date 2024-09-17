import javax.swing.*;
import java.util.LinkedList;
import java.time.LocalDate;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class SummaryAdditionalPanel
{
	Transactions transactions;
	JPanel summaryAdditionalPanel = new JPanel();
	JPanel tablesPanel = new JPanel();
	JPanel radioButtonPanel = new JPanel();
	JLabel currentDateLabel;
	JLabel pastDateLabel = new JLabel();
	JLabel chooseDaysPastLabel;
    String[] balancesColumnNames = {"Account", "Value"};
    String[][] balancesData = new String[29][2];
	String[] transactionsColumnNames = {"Date", "Amount", "From", "To", "Notes"};
	String[][] transactionsData = new String[100][5];
	String selectedAccount = "";
	JTable balancesTable = new JTable(balancesData, balancesColumnNames);
	JTable transactionsTable = new JTable(transactionsData, transactionsColumnNames);

	JScrollPane transactionsTableScrollPane = new JScrollPane(transactionsTable);
	JScrollPane balancesTableScrollPane = new JScrollPane(balancesTable);

	JRadioButton thirtyDaysRadioButton;
	JRadioButton ninetyDaysRadioButton;
	boolean isThirtyDays = true;

	public SummaryAdditionalPanel(Transactions inputTransactions)
	{
		transactions = inputTransactions;

		summaryAdditionalPanel.setLayout(new BoxLayout(summaryAdditionalPanel, BoxLayout.PAGE_AXIS));
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.LINE_AXIS));
		balancesTableScrollPane.setPreferredSize(new Dimension(175,500));
		transactionsTableScrollPane.setPreferredSize(new Dimension(525,500));

		currentDateLabel = new JLabel("Current Date: " + LocalDate.now().toString());

		updateDateLabel();
		repaintBalancesTable();
		repaintTransactionsTable();

		balancesTable.getSelectionModel().addListSelectionListener(new balancesTableListSelectionListener());


		chooseDaysPastLabel = new JLabel("Choose days past: ");
		thirtyDaysRadioButton = new JRadioButton("30 Days");
		ninetyDaysRadioButton = new JRadioButton("90 Days");

		//Add ActionListener to Radio Buttons
		thirtyDaysRadioButton.addActionListener(new thirtyDaysRadioButtonActionListener());
		ninetyDaysRadioButton.addActionListener(new ninetyDaysRadioButtonActionListener());

		//Selected Starting Radio Buttons
		thirtyDaysRadioButton.setSelected(true);

		//Grouping Radio Buttons
		ButtonGroup daysPastButtonGroup = new ButtonGroup();
		daysPastButtonGroup.add(thirtyDaysRadioButton);
		daysPastButtonGroup.add(ninetyDaysRadioButton);

		radioButtonPanel.add(chooseDaysPastLabel);
		radioButtonPanel.add(thirtyDaysRadioButton);
		radioButtonPanel.add(ninetyDaysRadioButton);

		tablesPanel.add(balancesTableScrollPane);
		tablesPanel.add(Box.createHorizontalStrut(2)); //Spacer
		tablesPanel.add(transactionsTableScrollPane);

		summaryAdditionalPanel.add(currentDateLabel);
		summaryAdditionalPanel.add(pastDateLabel);
		summaryAdditionalPanel.add(tablesPanel);
		summaryAdditionalPanel.add(radioButtonPanel);
	}

	public JPanel getPanel()
	{
		return summaryAdditionalPanel;
	}

	public void repaintTransactionsTable()
	{
		LinkedList<Transaction> resultTemp;
		LinkedList<Transaction> result;
		if(isThirtyDays)
		{
			resultTemp = transactions.getTransactionsDateRange(LocalDate.now().minusDays(30), LocalDate.now());
		}
		else
		{
			resultTemp = transactions.getTransactionsDateRange(LocalDate.now().minusDays(90), LocalDate.now());
		}
		//Blank out the transactions data table
		for(int i=0; i<transactionsData.length; i++)
		{
			transactionsData[i][0] = "";
			transactionsData[i][1] = "";
			transactionsData[i][2] = "";
			transactionsData[i][3] = "";
			transactionsData[i][4] = "";
		}
		//If there is no selected account
		if(selectedAccount.equals(""))
		{
			result = resultTemp;
		}
		else //There is a selected account
		{
			//Initialize the result LinkedList
			result = new LinkedList<Transaction>();
			//Need to loop through the temp results
			for(int i=0; i<resultTemp.size(); i++)
			{
				//If either the from or to account name equals the selected account
				if(resultTemp.get(i).getFrom().equals(selectedAccount) || resultTemp.get(i).getTo().equals(selectedAccount))
				{
					//Add it to the result linked list
					result.add(resultTemp.get(i));
				}
			}
		}
		//For every transaction element
		for(int i=0; i<result.size(); i++)
		{
			//Add it to the transactions data array
			transactionsData[i][0] = result.get(i).getDate().toString();
			transactionsData[i][1] = String.valueOf(result.get(i).getAmount());
			transactionsData[i][2] = result.get(i).getFrom();
			transactionsData[i][3] = result.get(i).getTo();
			transactionsData[i][4] = result.get(i).getNote();
		}
		//Repaint the transactions table
		transactionsTable.repaint();
	}

	public void repaintBalancesTable()
	{
		LinkedList<Transaction> result;
		Transactions tempTransactions = new Transactions();
		String[][] balancesResult;
		if(isThirtyDays)
		{
			result = transactions.getTransactionsDateRange(LocalDate.now().minusDays(30), LocalDate.now());
		}
		else
		{
			result = transactions.getTransactionsDateRange(LocalDate.now().minusDays(90), LocalDate.now());
		}
		//Blank out the balances data table
		for(int i=0; i<balancesData.length; i++)
		{
			balancesData[i][0] = "";
			balancesData[i][1] = "";
		}
		//For every transaction element
		for(int i=0; i<result.size(); i++)
		{
			//Add it to the temporary transactions
			tempTransactions.addTransactions(result.get(i));
		}
		//Get the temp transactions balance balances array
		balancesResult = tempTransactions.getBalanceObject().getBalances();
		//For every index in the balanceResult array
		for(int i=0; i<balancesResult.length; i++)
		{
			//Add it to the balances data table
			balancesData[i][0] = balancesResult[i][0];
			balancesData[i][1] = balancesResult[i][1];
		}

		//Repaint the balances table
		balancesTable.repaint();
	}

	private void updateDateLabel()
	{
		if(isThirtyDays)
		{
			pastDateLabel.setText("30 Days Past Date: " + LocalDate.now().minusDays(30).toString());
		}
		else
		{
			pastDateLabel.setText("90 Days Past Date: " + LocalDate.now().minusDays(90).toString());
		}
	}

	private class thirtyDaysRadioButtonActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			isThirtyDays = true;
			updateDateLabel();
			repaintBalancesTable();
			repaintTransactionsTable();
		}
	}

	private class ninetyDaysRadioButtonActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			isThirtyDays = false;
			updateDateLabel();
			repaintBalancesTable();
			repaintTransactionsTable();
		}
	}

	private class balancesTableListSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			selectedAccount = balancesData[balancesTable.getSelectedRow()][0];
			repaintTransactionsTable();
		}
	}
}