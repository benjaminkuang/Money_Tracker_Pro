import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.LinkedList;

public class BudgetsPanel
{
	Budgets budgets;
	Transactions transactions;
	Balances balances;
	Transactions currentTransactions;

	JPanel budgetsPanel = new JPanel();
	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	JLabel budgetsLabel = new JLabel("Budgets");
	String[] budgetsListName = {"Budgets List"};
	String[] columnNames = {"Accounts", "Planned", "Actual", "Difference"};
	String[][] budgetArray = new String[33][4];
	String[][] budgetsListArray = new String[27][1];
	JTable budgetsListTable = new JTable(budgetsListArray,budgetsListName);

	LinkedList<Transaction> tempTransactions;
	Balances currentBalances;
	Budget tempBudget;

	JTable budgetTable = new JTable(budgetArray, columnNames) {
		private static final long serialVersionUID = -8369356675486954525L;

		public boolean isCellEditable(int row, int column)
		{
			//Make read only fields except for column 1
			return column == 1;
		}
	};

	JPanel leftButtonsPanel = new JPanel() ;
	JButton addButton = new JButton("Add");
	JButton deleteButton = new JButton("Delete");
	JButton addBudgetItemButton = new JButton("Add");
	JButton deleteBudgetItemButton = new JButton("Delete");
	JPanel rightButtonsPanel;
	JScrollPane budgetsListTableScrollPane = new JScrollPane(budgetsListTable);
    JScrollPane budgetTableScrollPane = new JScrollPane(budgetTable);

	public BudgetsPanel(Budgets inputBudgets, Transactions inputTransactions, Balances inputBalance)
	{
		budgets = inputBudgets;
		transactions = inputTransactions;
		balances = inputBalance;

		budgetsListTable.getSelectionModel().addListSelectionListener(new budgetsListTableListSelectionListener());
		budgetTable.getSelectionModel().addListSelectionListener(new budgetTableListSelectionListener());

		String[][] data = balances.getBalances();
		for(int i=0; i<data.length; i++)
		{
			budgetArray[i][0] = data[i][0];
		}
		updateBudgetsList();

		addButton.addActionListener(new addButtonListener());
		deleteButton.addActionListener(new deleteButtonListener());

    	budgetTable.setName("Account Balances");
        budgetTableScrollPane.setPreferredSize(new Dimension(450,50));
        budgetsListTableScrollPane.setPreferredSize(new Dimension(175,405));

		budgetsPanel.setLayout(new BoxLayout(budgetsPanel, BoxLayout.LINE_AXIS));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

		leftPanel.add(budgetsListTableScrollPane);

		leftButtonsPanel.setLayout(new GridLayout(2,2));
		leftButtonsPanel.add(addButton);
		leftButtonsPanel.add(deleteButton);

		leftPanel.add(leftButtonsPanel);
		rightPanel.add(budgetsLabel);
		rightPanel.add(budgetTableScrollPane);
		budgetsPanel.add(leftPanel);
		budgetsPanel.add(rightPanel);
	}

	public JPanel getPanel()
	{
		return budgetsPanel;
	}

	private void updateBudgetsList()
	{
		String[] result = budgets.getBudgetsList();
		if(result.length==0)
		{
			for(int i=0; i<budgetsListArray.length; i++)
			{
				budgetsListArray[i][0] = null;
			}
		}
		else
		{
			for(int i=0; i<budgetsListArray.length; i++)
			{
				if(i<result.length)
				{
					budgetsListArray[i][0] = result[i];
				}
				else
				{
					budgetsListArray[i][0] = null;
				}
			}
		}
		budgetsListTable.repaint();
	}

	private void updateActualList(LocalDate startDate, LocalDate endDate)
	{
		tempTransactions = transactions.getTransactionsDateRange(startDate, endDate);
		currentTransactions = new Transactions();
		for(int i=0; i<tempTransactions.size(); i++)
		{
			currentTransactions.addTransactions(tempTransactions.get(i));
		}
		currentBalances = currentTransactions.getBalanceObject();
		String[][] temp = currentBalances.getBalances();

		//For each element in the budget array
		for(int i=0; i<budgetArray.length; i++)
		{
			//If not null
			if(budgetArray[i][0] != null)
			{
				//Replace with 0
				budgetArray[i][2] = "0";
			}
		}
		//For each element in the budget array
		for(int i=0; i<budgetArray.length; i++)
		{
			//Search the current budget balances array
			for(int j=0; j<temp.length; j++)
			{
				if(budgetArray[i][0] == null)
				{
					break;
				}
				//If the name is in the current budget balances array
				if(budgetArray[i][0].equals(temp[j][0]))
				{
					//Replace the balance
					budgetArray[i][2] = temp[j][1];
					//Name was found so stop the inner loop, move on to the next
					break;
				}
			}
		}

		double tempPlanned = 0.0;
		//For each element in the budget array
		for(int i=0; i<budgetArray.length; i++)
		{
			try
			{
				tempPlanned = Double.valueOf(budgetArray[i][1]);
			}
			catch(NumberFormatException exception)
			{
				budgetArray[i][1] = "0";
			}
			catch(NullPointerException exception)
			{

			}

			//If not null
			if(budgetArray[i][0] != null)
			{
				//If the Planned amount is null
				if(budgetArray[i][1] == null)
				{
					//Difference is
					budgetArray[i][3] = String.valueOf(0.0-Double.valueOf(budgetArray[i][2]));
					budgetArray[i][1] = "0";
					tempBudget.setBalance(budgetArray[i][0], 0.0);
				}
				else //There is a value in planned
				{
					budgetArray[i][3] = String.format("%.2f", tempPlanned-Double.valueOf(budgetArray[i][2]));
					tempBudget.setBalance(budgetArray[i][0], tempPlanned);
				}
			}
		}

		budgetTable.repaint();

	}

	private void blankActualList()
	{
		//For each element in the budget array
		for(int i=0; i<budgetArray.length; i++)
		{
			//If not null
			if(budgetArray[i][0] != null)
			{
				//Zero out the other three columns
				budgetArray[i][1] = null;
				budgetArray[i][2] = null;//"0";
				budgetArray[i][3] = null;//"0";
			}
		}
		budgetTable.repaint();
	}

	private void updatePlannedList()
	{
		//For each element in the budget array
		for(int i=0; i<budgetArray.length; i++)
		{
			try
			{
				//Get the planned amount
				budgetArray[i][1] = String.valueOf(tempBudget.getBalance(budgetArray[i][0]));
			}
			catch(NullPointerException exception)
			{
				//Do nothing
			}
		}
	}

	private class addButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JTextField startDate = new JTextField(7);
			JTextField endDate = new JTextField(7);
			JPanel inputPanel = new JPanel();
			inputPanel.add(new JLabel("Start: "));
			inputPanel.add(startDate);
			inputPanel.add(Box.createHorizontalStrut(15)); //Spacer
			inputPanel.add(new JLabel("End: "));
			inputPanel.add(endDate);

			int result = JOptionPane.showConfirmDialog(
					null, inputPanel,
					"Please enter Budget start and end dates (MM/DD/YYYY).",
					JOptionPane.OK_CANCEL_OPTION
			);
			// Modified by Ryan
			// closes out the option window if cancel or closed is pressed
			if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION)
			{
				return;
			}






//			Code added by Ryan
//			checks input for dates
			String slashDateFormat = "^\\d{2}/\\d{2}/\\d{4}$"; // creates the regex for date
			String dashDateFormat = "^\\d{2}-\\d{2}-\\d{4}$";
			String startString[]; //This holds an array of tokenized Strings
			String endString[];
			LocalDate fromDate;
			LocalDate toDate;
			if(startDate.getText().matches(slashDateFormat) && endDate.getText().matches(slashDateFormat))
			{
				startString = startDate.getText().split("/");
				endString = endDate.getText().split("/");
			}
			else if(startDate.getText().matches(dashDateFormat) && endDate.getText().matches(dashDateFormat))
			{
				startString = startDate.getText().split("-");
				endString = endDate.getText().split("-");
			}
			else // sends the user back to the start to try and enter the date info again.
			{
				JOptionPane.showMessageDialog(
						null,
						"enter the month day and year as: MM/DD/YYYY or MM-DD-YYYY"
				);
				actionPerformed(e);
				return;
			}
//			if(startDate.getText().contains("/"))
//			{
//				currentString = startDate.getText().split("/");
//			}
//			else if(startDate.getText().contains("-"))
//			{
//				currentString = startDate.getText().split("-");
//			}
//			else
//			{
//				actionPerformed(e);
//			}
			// Added by Ryan
			// checks if the date is within the specified range
			try {
				fromDate = LocalDate.of(
						Integer.valueOf(startString[2]),
						Integer.valueOf(startString[0]),
						Integer.valueOf(startString[1])
				);
				toDate = LocalDate.of(
						Integer.valueOf(endString[2]),
						Integer.valueOf(endString[0]),
						Integer.valueOf(endString[1])
				);
			}catch(DateTimeException err){
				JOptionPane.showMessageDialog(
						null,
						err.toString()
				);
				actionPerformed(e);
				return;
			}

//			if(endDate.getText().contains("/"))
//			{
//				currentString = endDate.getText().split("/");
//			}
//			else if(endDate.getText().contains("-"))
//			{
//				currentString = endDate.getText().split("-");
//			}
//			else
//			{
//				actionPerformed(e);
//			}


			budgets.addBudget(new Budget(fromDate, toDate));

			updateBudgetsList();
		}
	}

	private class deleteButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(budgetsListTable.getSelectedRow() <= budgets.getNumBudgets())
			{
				budgets.deleteBudget(budgetsListTable.getSelectedRow());
			}
			updateBudgetsList();
			blankActualList();
		}
	}

	private class budgetsListTableListSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			//If the currently selected row is more than the number of budgets
			if(budgetsListTable.getSelectedRow() >= budgets.getNumBudgets())
			{
				tempBudget = null;
				blankActualList();
			}
			else //The selected row is a correct row
			{
				//Get budget at selected row
				tempBudget = budgets.getBudget(budgetsListTable.getSelectedRow());
				blankActualList();
				updatePlannedList();
				updateActualList(tempBudget.getStartDate(), tempBudget.getEndDate());
			}
		}
	}

	private class budgetTableListSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if(tempBudget != null)
			{
				updateActualList(tempBudget.getStartDate(), tempBudget.getEndDate());
			}
		}
	}
}