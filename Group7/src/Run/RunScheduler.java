package Run;

import Scheduler.Scheduler;

public class RunScheduler {
	public static void main(String args[]) {
	    Scheduler scheduler;
	    Thread schedulerThread;

		scheduler = new Scheduler();

		schedulerThread = new Thread(scheduler, "Scheduler");

		schedulerThread.start();
	}
}
