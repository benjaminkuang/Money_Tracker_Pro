import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class MoneyTrackerApp extends JFrame implements ActionListener {

	private static final long serialVersionUID = -4154585966099329787L;

	//Constants
	//Made a text file with the word BUDGETS or GOALS in it then SHA256'ed it
	//Used as a separator in the CSV file for the respective sections
	private static final String BUDGETS_SHA256 = "A64DE5E31AB29C7AE5285933299D43159E142131A7928C1BC719B769AE320155";
	private static final String GOALS_SHA256 = "E27B6312FC683D7A9F236443D36A94C6468342935A32B331EE1F89C7A44963A6";

    static Transactions transactions = new Transactions();
    static Balances balances;
    static Budgets budgets = new Budgets();
    static Goals goals = new Goals();

    CardLayout cardLayout;
    JPanel cardPanel;
    JButton summary_button = new JButton("Summary");
    JButton balances_button = new JButton("Balances");
    JButton create_budget = new JButton("Budget"); //"Create Budget"
    JButton transaction_button = new JButton("Enter Transactions");
    JButton goals_button = new JButton("Goals");
    JButton import_export_button = new JButton("Import/Export");
    JButton recent_button = new JButton("Recent Transactions");
    JButton reports_button = new JButton("Reports");

    //Ben added this
    //Panels need to be here so they can be recreated
    //They should not be inside the scope of init()
    JPanel summaryPanel = createSummaryPanel();
    JPanel balancesPanel = createBalancesPanel();
    JPanel createBudgetPanel = createBudgetPanel();
    JPanel transactionPanel = createEnterTransactionsPanel();
    JPanel goalsPanel = createGoalsPanel();
    JPanel importExportPanel = createImportExportPanel();
    JPanel recentTransactionsPanel = createRecentTransactionsPanel();
    JPanel reportsPanel = createReportsPanel();

    //JLabel accountNameLabel = new JLabel("");
    //JLabel accountAmountLabel = new JLabel("");

    public void init() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create panels for each card
        summaryPanel = createSummaryPanel();
        balancesPanel = createBalancesPanel();
        createBudgetPanel = createBudgetPanel();
        transactionPanel = createEnterTransactionsPanel();
        goalsPanel = createGoalsPanel();
        importExportPanel = createImportExportPanel();
        recentTransactionsPanel = createRecentTransactionsPanel();
        reportsPanel = createReportsPanel();
        /*
        JPanel summaryPanel = createSummaryPanel();
        JPanel createBudgetPanel = createCreateBudgetPanel();
        JPanel transactionPanel = createTransactionPanel();
        JPanel goalsPanel = createGoalsPanel();
        JPanel importExportPanel = createImportExportPanel();
        JPanel recentTransactionsPanel = createRecentTransactionsPanel();
        JPanel reportsPanel = createReportsPanel();
        */

        // Add the panels to the card panel
        cardPanel.add(summaryPanel, "Summary");
        cardPanel.add(balancesPanel, "Balances");
        cardPanel.add(createBudgetPanel, "Budget");  //"Create Budget"
        cardPanel.add(transactionPanel, "Enter Transactions");
        cardPanel.add(goalsPanel, "Goals");
        cardPanel.add(importExportPanel, "Import/Export");
        cardPanel.add(recentTransactionsPanel, "Recent Transactions");
        cardPanel.add(reportsPanel, "Reports");

        // Create a button panel
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1));
        buttonPanel.add(summary_button);
        buttonPanel.add(balances_button);
        buttonPanel.add(create_budget);
        buttonPanel.add(transaction_button);
        buttonPanel.add(goals_button);
        buttonPanel.add(import_export_button);
        buttonPanel.add(recent_button);
        buttonPanel.add(reports_button);

        // Add action listeners to the buttons
        summary_button.addActionListener(this);
        balances_button.addActionListener(this);
        create_budget.addActionListener(this);
        transaction_button.addActionListener(this);
        goals_button.addActionListener(this);
        import_export_button.addActionListener(this);
        recent_button.addActionListener(this);
        reports_button.addActionListener(this);

        // Add the button panel and card panel to the frame
        add(buttonPanel, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        // Set the frame properties
        setTitle("Money Tracker Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        //This will recreate the panels as they are switched to
        switch(command) {
            case "Summary":
                summaryPanel = createSummaryPanel();
                cardPanel.add(summaryPanel, command);
                break;
            case "Balances":
            	balancesPanel = createBalancesPanel();
            	cardPanel.add(balancesPanel, command);
            	break;
            case "Budget": //"Create Budget"
                createBudgetPanel = createBudgetPanel();
                cardPanel.add(createBudgetPanel, command);
                break;
            case "Enter Transactions":
                transactionPanel = createEnterTransactionsPanel();
                cardPanel.add(transactionPanel, command);
                break;
            case "Goals":
                goalsPanel = createGoalsPanel();
                cardPanel.add(goalsPanel, command);
                break;
            case "Import/Export":
                importExportPanel = createImportExportPanel();
                cardPanel.add(importExportPanel, command);
                break;
            case "Recent Transactions":
                recentTransactionsPanel = createRecentTransactionsPanel();
                cardPanel.add(recentTransactionsPanel, command);
                break;
            case "Reports":
                reportsPanel = createReportsPanel();
                cardPanel.add(reportsPanel, command);
                break;
        }

        cardLayout.show(cardPanel, command);
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Add the table and combo box to a separate panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Account", "Value"};
        /*
        Object[][] data = {
                {"Account 1", 10000},
                {"Account 2", 20}
        };
        */
        String[][] balancesData = balances.getBalances();
        String[][] data = new String[32][2];
        //For each element in balancesData
        for(int i=0; i<balancesData.length; i++)
        {
        	//Add it to data
        	data[i][0] = balancesData[i][0];
        	data[i][1] = balancesData[i][1];
        }
        JTable table = new JTable(data, columnNames);
        table.setName("Account Balances");
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(150,50));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        //Add a ListSelectionListener to the accounts table
        /*
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e)
    		{

    		}
        });
        */

//            add label for table
        JLabel tableName = new JLabel("Account Balances");
        tablePanel.add(tableName, BorderLayout.NORTH);

        // Add the table panel and chart panel to the main panel
        panel.add(tablePanel, BorderLayout.WEST);

        // recent transactions table
        JPanel chartPanel = new JPanel();
        String[] cNames = {"Category", "Amount", "Account", "More info"};
        Object[][] expenses = {
                {"Shopping", "$40", "checking", " "},
                {"Entertainment", "70", "savings", " "}
        };
        JTable Expenses = new JTable(expenses, cNames);
        JScrollPane expenseScroll = new JScrollPane(Expenses);
        expenseScroll.setPreferredSize(new Dimension(450, 100));
        JLabel expenseLabel = new JLabel("Recent Transactions");
        chartPanel.add(expenseLabel, BorderLayout.NORTH);
        chartPanel.add(expenseScroll, BorderLayout.NORTH);
        // Create data for the chart
        /*
        List<String> xData = Arrays.asList("A", "B", "C", "D", "E");
        List<Double> yData = Arrays.asList(10.0, 20.0, 30.0, 40.0, 50.0);
        */

        /*
        // Create the chart and add it to a panel
        CategoryChart chart = new CategoryChartBuilder()
                .width(500).height(400)
                .title("Goals")
                .xAxisTitle("X")
                .yAxisTitle("Y")
                .build();
        chart.addSeries("Series", xData, yData);
        XChartPanel chartP = new XChartPanel(chart);
        // Add the chart panel to the main panel
        chartPanel.add(chartP, BorderLayout.CENTER);
        panel.add(chartPanel);
        */

        panel.add(new SummaryAdditionalPanel(transactions).getPanel());

        return panel;
    }

    /*
    private JPanel createBudgetPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create the input fields for budget creation
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Start Date (MM/DD/YYYY):"));
        JTextField startDateField = new JTextField();
        inputPanel.add(startDateField);
        inputPanel.add(new JLabel("End Date (MM/DD/YYYY):"));
        JTextField endDateField = new JTextField();
        inputPanel.add(endDateField);
        inputPanel.add(new JLabel("Budget Amount:"));
        JTextField budgetAmountField = new JTextField();
        inputPanel.add(budgetAmountField);
        inputPanel.add(new JLabel("Category of expenses"));
        JTextField categoryField = new JTextField();
        inputPanel.add(categoryField);
        panel.add(inputPanel, BorderLayout.CENTER);

        // Create the create budget button
        JButton createBudgetButton = new JButton("Create Budget");
        createBudgetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the start date, end date, and budget amount
                String startDateText = startDateField.getText();
                String endDateText = endDateField.getText();
                String budgetAmountText = budgetAmountField.getText();
                String categoryText = categoryField.getText();

                // Validate the input
                if (startDateText.isEmpty() || endDateText.isEmpty() || budgetAmountText.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please enter all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double budgetAmount = Double.parseDouble(budgetAmountText);
                    Budget budget = new Budget(startDateText, endDateText, budgetAmount, categoryText);
                    JOptionPane.showMessageDialog(panel, "Budget created: " + budget.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid budget amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(createBudgetButton, BorderLayout.SOUTH);

        // Add a title to the panel
        JLabel titleLabel = new JLabel("Create Budget Panel", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }
    */

    private JPanel createBudgetPanel() {
    	return new BudgetsPanel(budgets, transactions, balances).getPanel();
    }

    private JPanel createEnterTransactionsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Transaction Panel"));

        // Let's make a panel for the new table
        JPanel createEnterTransactionsPanel = new JPanel();

        // Let's make textFields
        JTextField dateTextField = new JTextField(10);
        JTextField amountTextField = new JTextField(25);
        JTextField notesTextField = new JTextField(25);

        String[] accountsList = balances.getAccountsListStringArray();
        JComboBox<String> toComboBox = new JComboBox<>(accountsList);
        toComboBox.setEditable(true);
        JComboBox<String> fromComboBox = new JComboBox<>(accountsList);
        fromComboBox.setEditable(true);

        // Let's make labels for those textFields
        JLabel dateLabel = new JLabel("Please enter the date MM/DD/YYYY: ");
        JLabel amountLabel = new JLabel("Now the amount: ");
        JLabel toLabel = new JLabel("Where's the money going? ");
        JLabel fromLabel = new JLabel("Where's the money coming from? ");
        JLabel notesLabel = new JLabel("Any additional comments: ");


        String[] columnNames = {"Date", "Amount", "To", "From", "Notes"};
        Object[][] data = new Object[100][100];
        ArrayList<Object[]> dataList = new ArrayList<Object[]>();
        JTable table = new JTable(data, columnNames);
        // Let's make a button and it's listener
        JButton createTransactionButton = new JButton("Add Transaction");

        createTransactionButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Object[] dataArray = new Object[5];
                // Check input for date
                dataArray[0] = dateTextField.getText();
                String date = (String) dataArray[0];
                //If date has 2/2/4 or 2-2-4 Digits, then do stuff
                if (date.matches("^\\d{2}/\\d{2}/\\d{4}$") || date.matches("^\\d{2}-\\d{2}-\\d{4}$")){

                	try
                	{
                		dataArray[1] = Double.parseDouble(amountTextField.getText());
                        dataArray[2] = (String)toComboBox.getSelectedItem();
                        dataArray[3] = (String)fromComboBox.getSelectedItem();
                        dataArray[4] = notesTextField.getText();
                        dataList.add(dataArray);
                        for(int i=0; i<dataList.size(); i++)
                        {
                            data[i][0] = dataList.get(i)[0].toString();
                            data[i][1] = dataList.get(i)[1].toString();
                            data[i][2] = dataList.get(i)[2].toString();
                            data[i][3] = dataList.get(i)[3].toString();
                            data[i][4] = dataList.get(i)[4].toString();
                        }
                        table.repaint();
                        transactions.addTransactions(
                                dateTextField.getText(),
                                (String)fromComboBox.getSelectedItem(),
                                (String)toComboBox.getSelectedItem(),
                                Double.parseDouble(amountTextField.getText()),
                                notesTextField.getText()
                        );
                	}
                	catch(DateTimeException exception)
                	{
                		//This is if the date is invalid like Feb 30
                		//Need to remove the last entry
                		data[dataList.size()-1][0] = "";
                		data[dataList.size()-1][1] = "";
                		data[dataList.size()-1][2] = "";
                		data[dataList.size()-1][3] = "";
                		data[dataList.size()-1][4] = "";
            			dataList.remove(dataList.size()-1);
                		table.repaint();
                	}
                	catch(NumberFormatException exception)
                	{
                		//If the double entry is not a number
                	}
                }

            }

        });

        // is this duplicate code?? =============================================================
        //No this code is to build the panel
        // Let's add the panel components
        createEnterTransactionsPanel.add(dateLabel);
        createEnterTransactionsPanel.add(dateTextField);

        createEnterTransactionsPanel.add(amountLabel);
        createEnterTransactionsPanel.add(amountTextField);

        createEnterTransactionsPanel.add(toLabel);
        createEnterTransactionsPanel.add(toComboBox);

        createEnterTransactionsPanel.add(fromLabel);
        createEnterTransactionsPanel.add(fromComboBox);

        createEnterTransactionsPanel.add(notesLabel);
        createEnterTransactionsPanel.add(notesTextField);

        createEnterTransactionsPanel.add(createTransactionButton);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 100));
        createEnterTransactionsPanel.add(scrollPane);
        createEnterTransactionsPanel.setLayout(new BoxLayout(createEnterTransactionsPanel, BoxLayout.Y_AXIS));
        panel.add(createEnterTransactionsPanel);

        return panel;
    }

    private JPanel createGoalsPanel() {
        GoalsPanel goalsPanel = new GoalsPanel(goals, transactions);
    	JPanel result = goalsPanel.getPanel();
    	return result;
    }

    private JPanel createImportExportPanel() {
        JPanel panel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        middlePanel.add(new JLabel("Import/Export Panel"));


        // LETS MAKE THE BUTTONS
        JButton importButton = new JButton("Import Data");
        JButton exportButton = new JButton("Export Data");


        // LETS MAKE THE LISTENERS
        importButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // STILL NEEDS JFILECHOOSER AND METHOD TO CHECK FOR CSV


                //JFileChooser
                //Holds the GUI element for the file chooser
                JDialog dialog = new JDialog();
                try //Possible exceptions
                {
                    //File reading here
                    JFileChooser chooser = new JFileChooser("."); //Makes a JFileChooser, possible exception
                    int status = chooser.showOpenDialog(dialog); //Opens the dialog for file selection
                    if(status == JFileChooser.APPROVE_OPTION) //If there is an input file
                    {
                    	MoneyTrackerApp.transactions = new Transactions();
                    	MoneyTrackerApp.balances = transactions.getBalanceObject();
                    	MoneyTrackerApp.budgets = new Budgets();
                    	MoneyTrackerApp.goals = new Goals();
                        File choosenFile = chooser.getSelectedFile(); //Give the file to a File type object
                        FileHandler.loadTransactions(transactions, budgets, goals, choosenFile);
                    }
                    else //There is no input file
                    {
                    	//Start this as blank
                        //System.out.println("No input file.");
                        //System.exit(0); //End the program
                    }
                }
                catch(FileNotFoundException exception) //Catch any error exceptions
                {
                    JOptionPane.showMessageDialog(null, "An error has occured.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        exportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // STILL NEEDS JFILE SAVING


            	String result = "Date,Amount,To,From\n";
            	String transactionsString[][] = transactions.getTransactionsArray();
            	String temp[];
            	String tempAmount;
            	for(int i=0; i<transactionsString.length; i++)
            	{
            		temp = transactionsString[i][0].split("-");
            		tempAmount = String.format("%.2f", Double.valueOf(transactionsString[i][1]));
            		result = result + temp[1].replace("0", "")+"/"+temp[2].replace("0", "")+"/"+temp[0] + "," +
            				          tempAmount + "," +
            				          transactionsString[i][2] + "," +
            				          transactionsString[i][3] + "," +
            				          transactionsString[i][4];
            		result += "\n";
            	}
            	result = result + exportBudgets();
            	result = result + exportGoals();
                //JFileChooser
                //Holds the GUI element for the file chooser
                JDialog dialog = new JDialog();
                try //Possible exceptions
                {
                    //File reading here
                    JFileChooser chooser = new JFileChooser("."); //Makes a JFileChooser, possible exception
                    int status = chooser.showOpenDialog(dialog); //Opens the dialog for file selection
                    if(status == JFileChooser.APPROVE_OPTION) //If there is an input file
                    {
                        File choosenFile = chooser.getSelectedFile(); //Give the file to a File type object
                        FileHandler.writeFile(choosenFile.getAbsolutePath(), result);
                    }
                    else //There is no input file
                    {
                    	//Start this as blank
                        //System.out.println("No input file.");
                        //System.exit(0); //End the program
                    }
                }
                catch(FileNotFoundException exception) //Catch any error exceptions
                {
                    JOptionPane.showMessageDialog(null, "An error has occured.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch(IOException exception) //Catch any error exceptions
                {
                    JOptionPane.showMessageDialog(null, "An error has occured.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        // LETS ADD THE BUTTONS TO THE PANEL
        bottomPanel.add(importButton);
        bottomPanel.add(exportButton);

        panel.add(topPanel);
        panel.add(middlePanel);
        panel.add(bottomPanel);

        return panel;
    }

    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Recent Transactions Panel"));

        // Let's make a panel for the new table
        JPanel tablePanel = new JPanel(new BorderLayout());

        // Let's give the columns of the table some names and data to
        // display that the table will actually display that data.
        String[] columnNames = {"Date", "Amount", "To", "From", "Notes"};
        String[][] data = transactions.getTransactionsArray();

        // Now let's make the actual table with the BorderLayout consistent
        // with the other panels of the GUI.
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createReportsPanel() {
    	return new ReportsPanel(transactions).getPanel();
    }

    private JPanel createBalancesPanel() {

    	JPanel outerPanel = new JPanel();
    	outerPanel = new JPanel(new BorderLayout());
    	JPanel innerPanel1 = new JPanel(new BorderLayout());
    	JPanel innerPanel4 = new JPanel();
    	JPanel innerPanel5 = new JPanel(); //5 is Overall right

    	String[] columnNames = {"Account", "Value"};
    	String[][] data = balances.getBalances();
    	String[] columnNames2 = {"Date", "Amount", "To", "From", "Notes"};;
    	String[][] resultDataArray = new String[200][5];

        JTable balancesTransactionsTable = new JTable(resultDataArray,columnNames2);

        JScrollPane scrollPane = new JScrollPane(balancesTransactionsTable);
        scrollPane.setPreferredSize(new Dimension(480, 530));

    	JTable balancesTable = new JTable(data, columnNames);
    	balancesTable.setName("Account Balances");
        JScrollPane scrollPanebalancesTable = new JScrollPane(balancesTable);
        scrollPanebalancesTable.setPreferredSize(new Dimension(150,50));

        balancesTransactionsTable.setName("Acount: Amount");





        // Add the table and combo box to a separate panel
        innerPanel1.add(scrollPanebalancesTable, BorderLayout.CENTER);

//            add label for table
        JLabel tableName = new JLabel("Account Balances");
        innerPanel1.add(tableName, BorderLayout.NORTH);


        //balancesTransactionsTable initial fill
        String[][] result = transactions.getTransactionsArray();
        for(int i=0; i<result.length; i++)
        {
        	resultDataArray[i][0] = result[i][0];
        	resultDataArray[i][1] = result[i][1];
        	resultDataArray[i][2] = result[i][2];
        	resultDataArray[i][3] = result[i][3];
        	resultDataArray[i][4] = result[i][4];
        }
        balancesTransactionsTable.repaint();

    	balancesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
    		public void valueChanged(ListSelectionEvent e)
    		{
    			int x = balancesTable.getSelectedRow();
    			String compare = balances.getNameOf(x);

    			String[][] transactionsArray = transactions.getTransactionsArray();
    			ArrayList<String[]> resultArray = new ArrayList<String[]>();
    			for(int i=0; i<transactionsArray.length; i++)
    			{
    				if(transactionsArray[i][2].equals(compare))
    				{
    					resultArray.add(transactionsArray[i]);
    				}
    				else if(transactionsArray[i][3].equals(compare))
    				{
    					resultArray.add(transactionsArray[i]);
    				}
    			}

    			for(int i=0; i<resultArray.size(); i++)
    			{
    				resultDataArray[i][0] = resultArray.get(i)[0];
    				resultDataArray[i][1] = resultArray.get(i)[1];
    				resultDataArray[i][2] = resultArray.get(i)[2];
    				resultDataArray[i][3] = resultArray.get(i)[3];
    				resultDataArray[i][4] = resultArray.get(i)[4];
    			}
    			for(int i=resultArray.size(); i<resultDataArray.length; i++)
    			{
    				resultDataArray[i][0] = "";
    				resultDataArray[i][1] = "";
    				resultDataArray[i][2] = "";
    				resultDataArray[i][3] = "";
    				resultDataArray[i][4] = "";
    			}
    			balancesTransactionsTable.setName(compare+": "+String.valueOf(balances.getAmount(compare)));
    			innerPanel1.repaint();
    			balancesTransactionsTable.repaint();
    		}
    	});

    	innerPanel4.add(scrollPane);

    	innerPanel5.add(innerPanel4, BorderLayout.CENTER);

    	outerPanel.add(innerPanel1, BorderLayout.WEST);
    	outerPanel.add(innerPanel5, BorderLayout.CENTER);
    	return outerPanel;
    }

	public String exportBudgets()
	{
		String result = "";
		String[][] balanceAccounts = balances.getBalances();
		//For every budget
		for(int i=0; i<budgets.getNumBudgets(); i++)
		{
			result = result + BUDGETS_SHA256 + "\n";
			result = result + getAmericanDateString(budgets.getBudget(i).getStartDate()) + "," + getAmericanDateString(budgets.getBudget(i).getEndDate()) + "\n";
			//For every balance account
			for(int j=0; j<balanceAccounts.length; j++)
			{

				try
				{
					result = result + balanceAccounts[j][0] + ",";
					result = result + budgets.getBudget(i).getBalance(balanceAccounts[j][0]) + "\n";
				}
				catch(NullPointerException exception)
				{
					//Skip it
					result = result + "0.0\n";
				}

			}
		}
		return result;
	}

	public String exportGoals()
	{
		String result = "";
		//If there are goals
		if(goals.getNumberGoals() > 0)
		{
			result += GOALS_SHA256 + "\n";
			//For each goal
			for(int i=0; i<goals.getNumberGoals(); i++)
			{
				//Add the goal name, comma for CSV, goal target, and a new line
				result += goals.getGoal(i).getName() + "," + String.valueOf(goals.getGoal(i).getTarget()) + "\n";
			}
		}
		return result;
	}

	public String getAmericanDateString(LocalDate date)
	{
		String result;
		String[] temp;
		temp = date.toString().split("-");
		result = temp[1] + "/" + temp[2] + "/" + temp[0];
		return result;
	}

    public static void main(String[] args){
        balances = transactions.getBalanceObject();

        //JFileChooser
        //Holds the GUI element for the file chooser
        JDialog dialog = new JDialog();
        try //Possible exceptions
        {
            //File reading here
            JFileChooser chooser = new JFileChooser("."); //Makes a JFileChooser, possible exception
            int status = chooser.showOpenDialog(dialog); //Opens the dialog for file selection
            if(status == JFileChooser.APPROVE_OPTION) //If there is an input file
            {
                File choosenFile = chooser.getSelectedFile(); //Give the file to a File type object
                FileHandler.loadTransactions(transactions, budgets, goals, choosenFile);
            }
            else //There is no input file
            {
            }
        }
        catch(FileNotFoundException exception) //Catch any error exceptions
        {
            JOptionPane.showMessageDialog(null, "An error has occured.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        transactions.getTransactionsDateRange(LocalDate.of(2023, 1, 15), LocalDate.of(2023, 1, 17));

        SwingUtilities.invokeLater(()->{
            MoneyTrackerApp app = new MoneyTrackerApp();
            app.init();
        });
    }
}