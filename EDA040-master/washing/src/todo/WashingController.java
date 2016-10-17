package todo;

import done.AbstractWashingMachine;
import done.ButtonListener;

public class WashingController implements ButtonListener {	
    private AbstractWashingMachine mach;
    private double speed;
    private WashingProgram wp;
    private TemperatureController tempController;
    private WaterController waterController;
    private SpinController spinController;



    public WashingController(AbstractWashingMachine mach, double speed) {
		this.mach = mach;
        this.speed = speed;

        tempController = new TemperatureController(mach, speed);
        waterController = new WaterController(mach, speed);
        spinController = new SpinController(mach, speed);

        tempController.start();
        waterController.start();
        spinController.start();
    }

    public void processButton(int button) {
        switch (button) {
            case 0:
                if (wp != null)
                    wp.interrupt();
                wp = new WashingProgram0(mach, speed, tempController, waterController, spinController);
                break;
            case 1:
                if (wp != null && wp.isAlive())
                    return;
                wp = new WashingProgram1(mach, speed, tempController, waterController, spinController);
                break;
            case 2:
                if (wp != null && wp.isAlive())
                    return;
                wp = new WashingProgram2(mach, speed, tempController, waterController, spinController);
                break;
            case 3:
                if (wp != null && wp.isAlive())
                    return;
                wp = new WashingProgram3(mach, speed, tempController, waterController, spinController);
                break;
            default:
                return;

        }
        wp.start();
    }
}
