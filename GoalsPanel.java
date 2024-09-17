import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class GoalsPanel
{
	Goals goals;
	Transactions transactions;
	Balances balances;

	JPanel goalsPanel = new JPanel();
	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	String[] goalsListName = {"Goal", "Target", "Left"};
	String[] columnNames = {"Date", "Amount", "From", "To", "Notes"};
	String[][] goalTransactionsArray = new String[100][5];
	String[][] goalsListArray = new String[27][3];
	JTable goalsListTable = new JTable(goalsListArray,goalsListName);
	JLabel goalsTableLabel = new JLabel("Budgets List");
	JTable goalTransactionsTable = new JTable(goalTransactionsArray, columnNames);
	JPanel leftButtonsPanel = new JPanel() ;
	JButton upButton = new JButton("Up");
	JButton downButton = new JButton("Down");
	JButton addButton = new JButton("Add");
	JButton deleteButton = new JButton("Delete");
	JButton addBudgetItemButton = new JButton("Add");
	JButton deleteBudgetItemButton = new JButton("Delete");
	JScrollPane goalsListTableScrollPane = new JScrollPane(goalsListTable);
    JScrollPane goalTransactionsTableScrollPane = new JScrollPane(goalTransactionsTable);

    JLabel goalNameLabel = new JLabel(""); //GOAL NAME HERE
    JLabel goalAmountLabel = new JLabel("GOAL AMOUNT HERE");
    JLabel goalLeftToGoLabel = new JLabel("Amount left to go:      asd");
    JLabel goalAmountLeftLabel = new JLabel("GOAL AMOUNT LEFT TO GO");
    JPanel goalLabelsPanel = new JPanel();
    JPanel rightButtonsPanel = new JPanel();
    JButton contributeButton = new JButton("Contribute");
    JButton withdrawButton = new JButton("Withdraw");

	public GoalsPanel(Goals inputGoals, Transactions inputTransactions)
	{
		goals = inputGoals;
		transactions = inputTransactions;
		balances = transactions.getBalanceObject();

		addButton.addActionListener(new addButtonListener());
		deleteButton.addActionListener(new deleteButtonListener());
		contributeButton.addActionListener(new contributeButtonListener());
		withdrawButton.addActionListener(new withdrawButtonListener());

		goalsListTable.getSelectionModel().addListSelectionListener(new goalsListTableSelectionListener());

        goalTransactionsTableScrollPane.setPreferredSize(new Dimension(420,520));
        goalsListTableScrollPane.setPreferredSize(new Dimension(350,405));

		goalsPanel.setLayout(new BoxLayout(goalsPanel, BoxLayout.LINE_AXIS));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

		goalLabelsPanel.add(goalNameLabel);
		goalLabelsPanel.add(goalLeftToGoLabel);

		leftPanel.add(goalsListTableScrollPane);

		leftButtonsPanel.setLayout(new GridLayout(2,2));
		leftButtonsPanel.add(addButton);
		leftButtonsPanel.add(deleteButton);

		rightButtonsPanel.setLayout(new GridLayout(1,2));
		rightButtonsPanel.add(contributeButton);
		rightButtonsPanel.add(withdrawButton);

		leftPanel.add(leftButtonsPanel);
		rightPanel.add(goalTransactionsTableScrollPane);
		rightPanel.add(rightButtonsPanel);
		goalsPanel.add(leftPanel);
		goalsPanel.add(rightPanel);

		repaintGoalsListTable();
		repaintGoalTransactionsTable();
	}

	public JPanel getPanel()
	{
		return goalsPanel;
	}

	private void repaintGoalsListTable()
	{
		//Blank out the table
		for(int i=0; i<goalsListArray.length; i++)
		{
			goalsListArray[i][0] = "";
			goalsListArray[i][1] = "";
			goalsListArray[i][2] = "";
		}
		for(int i=0; i<goals.getNumberGoals(); i++)
		{
			goalsListArray[i][0] = goals.getGoal(i).getName();
			goalsListArray[i][1] = String.valueOf(goals.getGoal(i).getTarget());
			goalsListArray[i][2] = String.valueOf(goals.getGoal(i).getLeft());
		}
		goalsListTable.repaint();
	}

	private void repaintGoalTransactionsTable()
	{
		//Blank out the table
		for(int i=0; i<goalTransactionsArray.length; i++)
		{
			goalTransactionsArray[i][0] = "";
			goalTransactionsArray[i][1] = "";
			goalTransactionsArray[i][2] = "";
			goalTransactionsArray[i][3] = "";
			goalTransactionsArray[i][4] = "";
		}
		//If an actual goal is selected and an actual row is selected(not a negative number row)
		if(goalsListTable.getSelectedRow() < goals.getNumberGoals() && !(goalsListTable.getSelectedRow()<=-1))
		{
			//Temp transactions holder
			Transactions temp = goals.getGoal(goalsListTable.getSelectedRow()).getTransactions();
			for(int i=0; i<temp.getNumberTransactions(); i++)
			{
				goalTransactionsArray[i][0] = temp.getTransaction(i).getDate().toString();
				goalTransactionsArray[i][1] = String.valueOf(temp.getTransaction(i).getAmount());
				goalTransactionsArray[i][2] = temp.getTransaction(i).getFrom();
				goalTransactionsArray[i][3] = temp.getTransaction(i).getTo();
				goalTransactionsArray[i][4] = temp.getTransaction(i).getNote();
			}
		}
		goalTransactionsTable.repaint();
	}

	private class addButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JTextField goalName = new JTextField(7);
			JTextField goalTarget = new JTextField(7);
			JPanel inputPanel = new JPanel();
			inputPanel.add(new JLabel("Goal Name: "));
			inputPanel.add(goalName);
			inputPanel.add(new JLabel("Goal Target: "));
			inputPanel.add(goalTarget);

			int result = JOptionPane.showConfirmDialog(null, inputPanel, "Please enter Goal information.", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION)
			{
				try
				{
					goals.addGoal(new Goal(goalName.getText(), Double.valueOf(goalTarget.getText())));
					repaintGoalsListTable();
				}
				catch(NumberFormatException exception)
				{
					//Do nothing the add fails
				}
			}
		}
	}

	private class deleteButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(goalsListTable.getSelectedRow() < goals.getNumberGoals())
			{
				goals.deleteGoal(goalsListTable.getSelectedRow());
				repaintGoalsListTable();
				repaintGoalTransactionsTable();
			}
		}
	}

	private class contributeButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//If no legitimate goal is selected, ends the function
			if(!(goalsListTable.getSelectedRow() < goals.getNumberGoals() && !(goalsListTable.getSelectedRow()<=-1)))
			{
				return;
			}

			JTextField dateTextField = new JTextField(5);
			JTextField amountTextField = new JTextField(5);
			String[] accountsList = balances.getAccountsListStringArray();
			JComboBox<String> fromComboBox = new JComboBox<>(accountsList);
	        fromComboBox.setEditable(true);
			JTextField noteTextField = new JTextField(5);
			JPanel inputPanel = new JPanel();
			GridLayout inputLayout = new GridLayout(2,4);
			inputPanel.setLayout(inputLayout);
			inputPanel.add(new JLabel("Date MM/DD/YYYY: "));
			inputPanel.add(dateTextField);
			inputPanel.add(new JLabel("Amount: "));
			inputPanel.add(amountTextField);
			inputPanel.add(new JLabel("From: "));
			inputPanel.add(fromComboBox);
			inputPanel.add(new JLabel("Note: "));
			inputPanel.add(noteTextField);

			int result = JOptionPane.showConfirmDialog(null, inputPanel, "Please enter Contribution information.", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION)
			{
				//If an actual goal is selected and an actual row is selected(not a negative number row)
				if(goalsListTable.getSelectedRow() < goals.getNumberGoals() && !(goalsListTable.getSelectedRow()<=-1))
				{
					try
					{
						//Add the contribution to the overall transactions
						transactions.addTransactions(dateTextField.getText(), (String)fromComboBox.getSelectedItem(), "Goal: "+goalsListArray[goalsListTable.getSelectedRow()][0], Double.valueOf(amountTextField.getText()), noteTextField.getText());
						//Get the most recent transaction from transactions and add that to the goal transactions list
						goals.getGoal(goalsListTable.getSelectedRow()).addTransaction(transactions.getRecentTransaction());
					}
					catch(NumberFormatException exception)
					{
						//Do nothing add fails
					}
				}
			}
			repaintGoalsListTable();
			repaintGoalTransactionsTable();
		}
	}

	private class withdrawButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//If no legitimate goal is selected, ends the function
			if(!(goalsListTable.getSelectedRow() < goals.getNumberGoals() && !(goalsListTable.getSelectedRow()<=-1)))
			{
				return;
			}

			JTextField dateTextField = new JTextField(5);
			JTextField amountTextField = new JTextField(5);
			//JTextField fromTextField = new JTextField(5);
			String[] accountsList = balances.getAccountsListStringArray();
			JComboBox<String> toComboBox = new JComboBox<>(accountsList);
	        toComboBox.setEditable(true);
			JTextField noteTextField = new JTextField(5);
			JPanel inputPanel = new JPanel();
			GridLayout inputLayout = new GridLayout(2,4);
			inputPanel.setLayout(inputLayout);
			inputPanel.add(new JLabel("Date MM/DD/YYYY: "));
			inputPanel.add(dateTextField);
			inputPanel.add(new JLabel("Amount: "));
			inputPanel.add(amountTextField);
			inputPanel.add(new JLabel("To: "));
			inputPanel.add(toComboBox);
			inputPanel.add(new JLabel("Note: "));
			inputPanel.add(noteTextField);



			int result = JOptionPane.showConfirmDialog(null, inputPanel, "Please enter Withdrawal information.", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION)
			{
				//If an actual goal is selected and an actual row is selected(not a negative number row)
				if(goalsListTable.getSelectedRow() < goals.getNumberGoals() && !(goalsListTable.getSelectedRow()<=-1))
				{
					try
					{
						//Add the contribution to the overall transactions
						transactions.addTransactions(dateTextField.getText(), "Goal: "+goalsListArray[goalsListTable.getSelectedRow()][0], (String)toComboBox.getSelectedItem(), Double.valueOf(amountTextField.getText()), noteTextField.getText());
						//Get the most recent transaction from transactions and add that to the goal transactions list
						goals.getGoal(goalsListTable.getSelectedRow()).addTransaction(transactions.getRecentTransaction());
						goals.getGoal(goalsListTable.getSelectedRow()).setWithdraw(Double.valueOf(amountTextField.getText()));
					}
					catch(NumberFormatException exception)
					{
						//Do nothing add fails
					}
				}
			}
			repaintGoalsListTable();
			repaintGoalTransactionsTable();
		}
	}

	private class goalsListTableSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			goalNameLabel.setText(goalsListArray[goalsListTable.getSelectedRow()][0]);
			repaintGoalTransactionsTable();
		}
	}
}