import java.util.LinkedList;

public class Goals
{
	private LinkedList<Goal> goalsList = new LinkedList<Goal>();
	private int numGoals = 0;

	public Goals()
	{

	}

	public void addGoal(Goal inputGoal)
	{
		goalsList.add(inputGoal);
		numGoals++;
	}

	public Goal getGoal(int i)
	{
		return goalsList.get(i);
	}

	public void deleteGoal(int i)
	{
		goalsList.remove(i);
		numGoals--;
	}

	public int getNumberGoals()
	{
		return numGoals;
	}
}