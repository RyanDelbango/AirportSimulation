// Ryan Delbango

import java.util.LinkedList; 
import java.util.Queue; 
import java.text.DecimalFormat;

public class airportSimulate
{
	public static void airportSimulate(int landTime, int departTime, double arrivalProb, double departureProb, int maxAirTime, int totalTime)
	{
		Queue<Integer> arrivalTimes = new LinkedList<Integer>();
		Queue<Integer> takeOffTimes = new LinkedList<Integer>();
		int next;
		BooleanSource arrival = new BooleanSource(arrivalProb);
		BooleanSource departure = new BooleanSource(departureProb);
		Runway newRunway = new Runway(landTime, departTime);
		DecimalFormat two = new DecimalFormat("0.00"); 
		Averager landing = new Averager();
		Averager takeOff = new Averager();
		int currentSecond = 0;
		int totalLanded = 0;
		int totalDepartures = 0;
		int totalCrashed = 0;
		int flightTime = 0;
		

		System.out.print('\n');
		System.out.println("Amount of minutes to land: " + landTime);
		System.out.println("Amount of minutes to take off: " + departTime);
		System.out.println("Probability of arrival during a minute: " + arrivalProb);
		System.out.println("Average amount of time between planes to land: " + ((arrivalProb) * 100));
		System.out.println("Probability of departure during a minute: " + departureProb);
		System.out.println("Average amount of time between planes to take off: " + ((departureProb) * 100));
		System.out.println("Maximum amount of time in the air before crashing: " + maxAirTime);
		System.out.println("Total simulation minutes: " + totalTime);

		if (landTime <= 0 || departTime <= 0 || arrivalProb < 0 || departureProb < 0 || arrivalProb > 1 || departureProb > 1 || totalTime < 0)
		{
			throw new IllegalArgumentException("Values out of range.");
		}

		for (currentSecond = 0; currentSecond < totalTime; currentSecond++)
		{

			if (!arrivalTimes.isEmpty())
			{
				flightTime++;
			}

			if (flightTime >  maxAirTime)
			{
				arrivalTimes.remove();
				totalCrashed++;
				
				if (!arrivalTimes.isEmpty())
				{
					flightTime = (currentSecond - arrivalTimes.element());
				}
				else
				{
					flightTime = 0;
				}
			}
			
			if (arrival.query())
			{
				arrivalTimes.add(currentSecond);
			}
			
			if (departure.query())
			{
				takeOffTimes.add(currentSecond);
			}
			
			if ((!newRunway.isBusy()) && (!arrivalTimes.isEmpty()))
			{
				next = arrivalTimes.remove();
				landing.addNumber(currentSecond - next);
				newRunway.startLanding();
				totalLanded++;
				if (!arrivalTimes.isEmpty())
				{
					flightTime = (currentSecond - arrivalTimes.element());
				}
				else
				{
					flightTime = 0;
				}
			}

			if ((!newRunway.isBusy()) && (arrivalTimes.isEmpty()) && (!takeOffTimes.isEmpty()))
			{
				next = takeOffTimes.remove();
				takeOff.addNumber(currentSecond - next);
				newRunway.startDeparting();
				totalDepartures++;
			}

			if (newRunway.isBusy())
			{
				newRunway.reduceRemainingTime();
			}
		}
		
		System.out.print('\n');
		System.out.println("Number of planes taken off: " + totalDepartures);
		System.out.println("Number of planes landed: " + totalLanded);
		System.out.println("Number of planes crashed: " + totalCrashed);
		
		if (takeOff.howManyNumbers() > 0)
		{
			System.out.println("Average waiting time for taking off: " + two.format(takeOff.average()) + " minutes");
		}

		if (landing.howManyNumbers() > 0)
		{
			System.out.println("Average waiting time for landing: " + two.format(landing.average()) + " minutes");
		}
		
		System.out.print('\n');
	}
	
	public static void main(String[] args)
	{
		airportSimulate(4, 2, 0.1, 0.1, 5, 6000);
	}

}