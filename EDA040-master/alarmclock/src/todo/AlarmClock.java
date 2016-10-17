package todo;
import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;

/**
 * Main class of alarm-clock application.
 * Constructor providing access to IO.
 * Method start corresponding to main,
 * with closing down done in terminate.
 */
public class AlarmClock {

	private ClockInput	input;
	private ClockOutput	output;
	private Semaphore	signal; 
	private Thread tAgent;
	private Thread btnAgent;
	private ClockData data;

	/**
	 * Create main application and bind attributes to device drivers.
	 * @param i The input from simulator/emulator/hardware.
	 * @param o Dito for output.
	 */
	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		signal = input.getSemaphoreInstance();
		data = new ClockData(output, input);

	}

	/**
	 * Tell threads to terminate and wait until they are dead.
	 */
	public void terminate() {
		//tAgent.interrupt(); NOT WORKING
		//btnAgent.interrupt();
		//try {
		//	tAgent.join();
		//	btnAgent.join();
		//} catch (InterruptedException e) {}

		output.console("AlarmClock exit.");
	}
	
	/**
	 * Create thread objects, and start threads
	 */
	public void start() {
		// Delete/replace the following test/demo code;
		// make something happen by exercising the IO:

		// Create thread objects here...
		Thread tAgent = new Thread(new TimeAgent(data));
		Thread btnAgent = new Thread(new ButtonAgent(signal, data));
		
		// Create threads of execution by calling start...
		tAgent.start();
		btnAgent.start();
	}

	
}
