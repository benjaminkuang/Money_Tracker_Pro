import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;

public class FileHandler {
	private static final String BUDGETS_SHA256 = "A64DE5E31AB29C7AE5285933299D43159E142131A7928C1BC719B769AE320155";
	private static final String GOALS_SHA256 = "E27B6312FC683D7A9F236443D36A94C6468342935A32B331EE1F89C7A44963A6";

    static void printFileContents(String filepath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filepath));
        scanner.useDelimiter(",");
        System.out.println("\n-- File read begin --");
        while (scanner.hasNext()) {
            System.out.print(scanner.next() + " "); // The space is to separate tokens for human-readability.
        }
        System.out.println("\n-- File read finished --");

        scanner.close();
    }


    static void loadTransactions(Transactions transactions, Budgets budgets, Goals goals, File inputFile) throws FileNotFoundException {
		//Creates a Scanner with the File
		Scanner inputScanner = new Scanner(inputFile);
		String currentString[]; //This holds an array of tokenized Strings
		String currentLine = "";
		String currentNotes;
		String tempString = "";
		Budget tempBudget;

		inputScanner.nextLine();

		//As long as the input has lines
		while(inputScanner.hasNextLine())
		{
			//Get the next line
			currentLine = inputScanner.nextLine();

			if(currentLine.equals(BUDGETS_SHA256))
			{
				//This section is done
				break;
			}

			//Tokenize the String by "," into an array
			currentString = currentLine.split(",");

			try
			{
				currentNotes = currentString[4];
			}
			catch(ArrayIndexOutOfBoundsException exception)
			{
				currentNotes = "";
			}

			//transactions.addTransactions(LocalDate.of(year, month, dayOfMonth), currentString[3], currentString[2], Double.parseDouble(currentString[1]));
			transactions.addTransactions(currentString[0], currentString[3], currentString[2], Double.parseDouble(currentString[1]), currentNotes);
		}
		//If there are still lines it is the Budgets section
		while(inputScanner.hasNextLine())
		{
			//Get the next line
			currentLine = inputScanner.nextLine();
			//The start date and end date
			//Tokenize the String by "," into an array
			currentString = currentLine.split(",");
			tempBudget = new Budget(currentString[0], currentString[1]);
			budgets.addBudget(tempBudget);
			//As long as the input has lines
			while(inputScanner.hasNextLine())
			{
				//Get the next line
				currentLine = inputScanner.nextLine();
				if(currentLine.equals(GOALS_SHA256))
				{
					//Budgets section is done
					break;
				}
				else if(currentLine.equals(BUDGETS_SHA256))
				{
					//Next Budget
					break;
				}
				//Tokenize the String by "," into an array
				currentString = currentLine.split(",");
				tempBudget.setBalance(currentString[0], Double.valueOf(currentString[1]));
			}
			if(currentLine.equals(GOALS_SHA256))
			{
				//Need to break again
				//Budgets section is done
				break;
			}
		}
		//Do goals section here
		//If there are still lines it is the Goals section
		while(inputScanner.hasNextLine())
		{
			//Get the next line
			currentLine = inputScanner.nextLine();
			//No need for ifs becuase goals is the end of the CSV file
			//Tokenize the String by "," into an array
			currentString = currentLine.split(",");
			//Add the lines to Goals
			goals.addGoal(new Goal(currentString[0], Double.valueOf(currentString[1])));
			//Until there are no more lines to the end
		}
		//Now we need to backfill goals from transactions
		//For every transaction
		for(int i=0; i<transactions.getNumberTransactions(); i++)
		{
			//If either the From or To field has the string "Goal: " it is a goals transaction
			//If the from has Goal, it is a withdraw
			if(transactions.getTransaction(i).getFrom().contains("Goal: "))
			{
				//Temp string is now the goal name
				tempString = transactions.getTransaction(i).getFrom().replace("Goal: ", "");
				//For each goal
				for(int j=0; j<goals.getNumberGoals(); j++)
				{
					//Compare the name with the current transaction name, if it matchs
					if(tempString.equals(goals.getGoal(j).getName()))
					{
						//Add the transaction
						goals.getGoal(j).addTransaction(transactions.getTransaction(i));
						//Set the withdraw amount
						goals.getGoal(j).setWithdraw(transactions.getTransaction(i).getAmount());
					}
				}
			}
			//If the to has Goal, it is a contribution
			else if(transactions.getTransaction(i).getTo().contains("Goal: "))
			{
				//Temp string is now the goal name
				tempString = transactions.getTransaction(i).getTo().replace("Goal: ", "");
				//For each goal
				for(int j=0; j<goals.getNumberGoals(); j++)
				{
					//Compare the name with the current transaction name, if it matchs
					if(tempString.equals(goals.getGoal(j).getName()))
					{
						//Add the transaction
						goals.getGoal(j).addTransaction(transactions.getTransaction(i));
					}
				}
			}
		}
		//Closing the input scanner
		inputScanner.close();
    }

    static void loadTransactions(Transactions transactions, String filepath) throws FileNotFoundException {
    	/*
    	Scanner scanner = new Scanner(new File(filepath));
        scanner.useDelimiter(",");
        System.out.println("\n-- File read begin --");
        while (scanner.hasNext()) {
        	transactions.addTransactions(date, from, to, amount);
            System.out.print(scanner.next() + " "); // The space is to separate tokens for human-readability.
        }
        System.out.println("\n-- File read finished --");

        scanner.close();
        */


		//Creates a Scanner with the File
		Scanner inputScanner = new Scanner(new File(filepath));
		String currentString[]; //This holds an array of tokenized Strings
		String currentNotes;

		inputScanner.nextLine();

		//As long as the input has lines
		while(inputScanner.hasNextLine()) //Get the next line
		{
			//Tokenize the String by "," into an array
			currentString = inputScanner.nextLine().split(",");

			try
			{
				currentNotes = currentString[4];
			}
			catch(ArrayIndexOutOfBoundsException exception)
			{
				currentNotes = "";
			}

			transactions.addTransactions(currentString[0], currentString[3], currentString[2], Double.parseDouble(currentString[1]), currentNotes);
		}
		//Closing the input scanner
		inputScanner.close();
    }

    static void writeFile(String filepath, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(filepath);
        fileWriter.write(content);
        fileWriter.flush(); // Writes any final buffered data; ends buffer stream
        fileWriter.close(); // Closes the file writer and finalizes written data
    }
}
