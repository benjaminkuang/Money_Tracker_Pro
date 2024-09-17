import java.time.LocalDate;

public class Transaction
	{
		private LocalDate date;
		private String from, to, note;
		private double amount;

		public Transaction(LocalDate inputDate, String inputFrom, String inputTo, double inputAmount, String inputNote)
		{
			date = inputDate;
			from = inputFrom;
			to=inputTo;
			amount = inputAmount;
			note = inputNote;
		}

		public LocalDate getDate()
		{
			return date;
		}

		public String getAmericanDateString()
		{
			String result;
			String[] temp;
			temp = date.toString().split("-");
			result = temp[1] + "-" + temp[2] + "-" + temp[0];
			return result;
		}

		public String getMonth()
		{
			return date.getMonth().toString();
		}

		public String getYear()
		{
			return String.valueOf(date.getYear());
		}

		public String getFrom()
		{
			return from;
		}

		public String getTo()
		{
			return to;
		}

		public double getAmount()
		{
			return amount;
		}

		public String getNote()
		{
			return note;
		}
	}