package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


class TemperatureController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private double speed;
	private RTThread sender;
	private double targetTemperature;
	private int mode = TemperatureEvent.TEMP_IDLE;
    private double upwardsMargin;
    private double downwardsMargin;
    private static double MARGIN = 0.2;
    private static double PERIOD_TIME = 10.0;   // in seconds
    private static double HEATING_POWER = 2000.0; // in watts
    private static double WASHER_VOLUME = 20.0;   // in liters
    private static double AMBIENT_TEMPERATURE = 20.0; // in degree C
    private static double ACCEPTED_COOLING = 2.0; // in degree C
    private boolean sentACK = true;
    private long timestamp;

	TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (10000/speed)); //
		this.mach = mach;
		this.speed = speed;
	}

	public void perform() {
		TemperatureEvent action = (TemperatureEvent) this.mailbox.tryFetch();
		if (action != null) {
			mode = action.getMode();
            targetTemperature = action.getTemperature();
            sender = (RTThread) action.getSource();

            switch(mode) {
                //Idle
                case TemperatureEvent.TEMP_IDLE:
                    mach.setHeating(false);
                    break;

                //Set temperature goal
                case TemperatureEvent.TEMP_SET:
                    mach.setHeating(true);
                    timestamp = System.currentTimeMillis();
                    upwardsMargin = PERIOD_TIME * HEATING_POWER / (mach.getWaterLevel() * WASHER_VOLUME * 4200) - MARGIN;
                    downwardsMargin = PERIOD_TIME * (mach.getTemperature() - AMBIENT_TEMPERATURE) / 3500 + MARGIN;
                    sentACK = false;
                    break;
            }

		}

		if (mode == TemperatureEvent.TEMP_SET && periodCheck()) {
            timestamp = System.currentTimeMillis();
            if(mach.getTemperature() >= targetTemperature - upwardsMargin) {
                mach.setHeating(false);
                if (!sentACK) {
                    sender.putEvent(new AckEvent(this));
                    sentACK = true;
                }
            }
            else if (mach.getTemperature() <= (targetTemperature - ACCEPTED_COOLING) + downwardsMargin) {
                mach.setHeating(true);
            }
        }
	}

	/* Temperature relay should not switch on/off too often. */
	private boolean periodCheck() {
        long currentTime = System.currentTimeMillis();
        return currentTime >= (timestamp + (PERIOD_TIME * 1000) / speed);
    }
}
