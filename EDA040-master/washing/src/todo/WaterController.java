package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


class WaterController extends PeriodicThread {
	private AbstractWashingMachine mach;
    private int mode = WaterEvent.WATER_IDLE;
	private double speed;
    private double level = 0.0;
    private RTThread sender;

	WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
        this.mach = mach;
        this.speed = speed;
	}

	public void perform() {
        // look for new WaterEvents in mailbox
        WaterEvent action = (WaterEvent) this.mailbox.tryFetch();

        if (action != null) {
            mode = action.getMode();
            level = action.getLevel();
            sender = (RTThread) action.getSource();

            switch(mode){

                //Idle
                case WaterEvent.WATER_IDLE:
                    mach.setFill(false);
                    mach.setDrain(false);
                    break;

                //Fill
                case WaterEvent.WATER_FILL:
                    mach.setDrain(false);
                    mach.setFill(true);
                    break;

                //Drain
                case WaterEvent.WATER_DRAIN:
                    mach.setFill(false);
                    mach.setDrain(true);
                    break;

            }
        }

        // do periodic check for need to change current action
        if (mode == WaterEvent.WATER_FILL && mach.getWaterLevel() >= level) {
            mach.setFill(false);
            mode = WaterEvent.WATER_IDLE;
            sender.putEvent(new AckEvent(this));
        }
        else if(mode == WaterEvent.WATER_DRAIN && mach.getWaterLevel() <= level) {
            mach.setDrain(false);
            mode = WaterEvent.WATER_IDLE;
            sender.putEvent(new AckEvent(this));
        }
	}
}
