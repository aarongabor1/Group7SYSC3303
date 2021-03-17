package Run;

import Scheduler.Scheduler;

/**
 * Class to run just the scheduler system.
 * 
 * @author Marc Angers
 * @version 1.0
 */
public class RunScheduler {
	public static void main(String args[]) {
	    Scheduler scheduler;
	    Thread schedulerThread;

		scheduler = new Scheduler();

		schedulerThread = new Thread(scheduler, "Scheduler");

		schedulerThread.start();
	}
}
