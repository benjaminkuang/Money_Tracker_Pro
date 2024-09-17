import java.util.LinkedList;

public class Budgets
{
	private LinkedList<Budget> budgetsList = new LinkedList<Budget>();
	private int numBudgets = 0;

	public Budgets()
	{

	}

	public void addBudget(Budget budget)
	{
		budgetsList.add(budget);
		numBudgets++;
	}

	public void deleteBudget(int i)
	{
		budgetsList.remove(i);
		numBudgets--;
	}

	public Budget getBudget(int i)
	{
		return budgetsList.get(i);
	}

	public int getNumBudgets()
	{
		return numBudgets;
	}

	public String[] getBudgetsList()
	{
		String result[] = new String[budgetsList.size()];
		Budget currentBudget;
		for(int i=0; i<budgetsList.size(); i++)
		{
			currentBudget = budgetsList.get(i);
			result[i] = currentBudget.getStartDate().toString() + "     to     " + currentBudget.getEndDate().toString();
		}
		return result;
	}
}