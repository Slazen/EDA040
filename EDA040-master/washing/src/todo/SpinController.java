package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


class SpinController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private double speed;
	private RTThread sender;
	private int mode = SpinEvent.SPIN_OFF;
    private int direction = AbstractWashingMachine.SPIN_LEFT;
    private long timestamp;
	private static int ROTATION_PERIOD = 60; // in seconds
    private static int counter = 0; // count the passed seconds

	SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed));
		this.mach = mach;
		this.speed = speed;
	}

	public void perform() {
        SpinEvent action = (SpinEvent) this.mailbox.tryFetch();
        if (action != null) {
            mode = action.getMode();
            sender = (RTThread) action.getSource();

            switch (mode) {
                //Off
                case SpinEvent.SPIN_OFF:
                    mach.setSpin(AbstractWashingMachine.SPIN_OFF);
                    break;

                case SpinEvent.SPIN_SLOW:
                    mach.setSpin(direction);
                    timestamp = System.currentTimeMillis();
                    break;


                case SpinEvent.SPIN_FAST:
                    mach.setSpin(AbstractWashingMachine.SPIN_FAST);
                    break;
            }
        }
        /* Change direction of slow spin every period time */
        if (mode == SpinEvent.SPIN_SLOW && periodCheck()) {
            direction = (direction % 2) + 1;
            mach.setSpin(direction);
        }


	}
    /* Check if a period passed */
    private boolean periodCheck() {
        counter = (counter >= 60) ? 0: counter+1;
        return  (counter == 0);
    }
}
