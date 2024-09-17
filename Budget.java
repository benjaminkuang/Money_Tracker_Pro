import javax.swing.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;

public class Budget
{
	private LocalDate startDate;
	private LocalDate endDate;
	private HashMap<String, Double> balances = new HashMap<String, Double>();

	public Budget(LocalDate fromDate, LocalDate toDate)
	{
		startDate = fromDate;
		endDate = toDate;
	}
	// Modified by Ryan
	// if there is an error with the file loader class this input validator should
	// catch it.
	public Budget(String fromDate, String toDate)
	{
		String slashDateFormat = "^\\d{2}/\\d{2}/\\d{4}$"; // creates the regex for date
		String dashDateFormat = "^\\d{2}-\\d{2}-\\d{4}$";
		String startString[]; //This holds an array of tokenized Strings
		String endString[];
		if(fromDate.matches(slashDateFormat) && toDate.matches(slashDateFormat))
		{
			startString = fromDate.split("/");
			endString = toDate.split("/");
		}
		else if(fromDate.matches(dashDateFormat) && toDate.matches(dashDateFormat))
		{
			startString = fromDate.split("-");
			endString = toDate.split("-");
		}
		else // displays error messog
		{
			JOptionPane.showMessageDialog(
					null,
					"The month day and year is not entered in as: MM/DD/YYYY or MM-DD-YYYY"
			);
			return;
		}
//		String currentString[]; //This holds an array of tokenized Strings
//
//		if(fromDate.contains("/"))
//		{
//			currentString = fromDate.split("/");
//		}
//		else if(fromDate.contains("-"))
//		{
//			currentString = fromDate.split("-");
//		}
//		else
//		{
//			return;
//		}
		try {
			startDate = LocalDate.of(Integer.valueOf(startString[2]), Integer.valueOf(startString[0]), Integer.valueOf(startString[1]));
			endDate = LocalDate.of(Integer.valueOf(endString[2]), Integer.valueOf(endString[0]), Integer.valueOf(endString[1]));
		}catch(DateTimeException err){
			JOptionPane.showMessageDialog(null, err.toString());
		}
//		if(toDate.contains("/"))
//		{
//			currentString = toDate.split("/");
//		}
//		else if(toDate.contains("-"))
//		{
//			currentString = toDate.split("-");
//		}
//		else
//		{
//			return;
//		}

	}

	public LocalDate getStartDate()
	{
		return startDate;
	}

	public LocalDate getEndDate()
	{
		return endDate;
	}

	public double getBalance(String account)
	{
		return balances.get(account);
	}

	public void setBalance(String account, double amount)
	{
		balances.put(account, amount);
	}
}