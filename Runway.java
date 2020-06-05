public class Runway
{
	private int secondsForLand;
	private int secondsForDepart;
	private int landTimeLeft;
	private int departTimeLeft;

	public Runway(int l, int d)
	{
		secondsForLand = l;
		secondsForDepart = d;
		landTimeLeft = 0;
		departTimeLeft = 0;
	}

	public boolean isBusy()
	{
		return (landTimeLeft > 0 || departTimeLeft > 0);
	}

	public void reduceRemainingTime()
	{
		if (landTimeLeft > 0)
		{
			landTimeLeft--;
		}
		else if (departTimeLeft > 0)
		{
			departTimeLeft--;
		}
	}
	
	public void startLanding()
	{
		if (landTimeLeft > 0)
		{
			throw new IllegalStateException("Runway is already busy.");
		}
		
		landTimeLeft = secondsForLand;
	}

	public void startDeparting()
	{
		if (departTimeLeft > 0)
		{
			throw new IllegalStateException("Runway is already busy.");
		}	

		departTimeLeft = secondsForDepart;
	}
}