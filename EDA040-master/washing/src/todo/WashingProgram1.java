/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980812 Created
 * PP 990924 Revised
 */

package todo;

import done.AbstractWashingMachine;

/**
 * Program 3 of washing machine. Does the following:
 * <UL>
 *   <LI>Switches off heating
 *   <LI>Switches off spin
 *   <LI>Pumps out water
 *   <LI>Unlocks the hatch.
 * </UL>
 */
class WashingProgram1 extends WashingProgram {

	// ------------------------------------------------------------- CONSTRUCTOR
    private double speed;
    private static long WASH_TIME = 30*60*1000; // in seconds
    private static long RINSE_TIME = 2*60*1000; // in seconds
    private static long CENT_TIME = 5*60*1000;  // in seconds

	/**
	 * @param   mach             The washing machine to control
	 * @param   speed            Simulation speed
	 * @param   tempController   The TemperatureController to use
	 * @param   waterController  The WaterController to use
	 * @param   spinController   The SpinController to use
	 */
	WashingProgram1(AbstractWashingMachine mach,
                           double speed,
                           TemperatureController tempController,
                           WaterController waterController,
                           SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
        this.speed = speed;
	}

	// ---------------------------------------------------------- PUBLIC METHODS

	/**
	 * This method contains the actual code for the washing program. Executed
	 * when the start() method is called.
	 */
	protected void wash() throws InterruptedException {
		// Lock
		myMachine.setLock(true);

        // Fill
        myWaterController.putEvent(new WaterEvent(this,
                WaterEvent.WATER_FILL,
                0.5));
        mailbox.doFetch(); // Wait for Ack

        // Switch on spin
        mySpinController.putEvent(new SpinEvent(this,
                SpinEvent.SPIN_SLOW));

		// Heat water to 60C
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_SET,
				60));
        mailbox.doFetch(); // Wait for Ack

        Thread.sleep((long) (WASH_TIME / speed));

        // Turn off heat
        myTempController.putEvent(new TemperatureEvent(this,
                TemperatureEvent.TEMP_IDLE,
                0));

		// Rinse 5 times in cold water
        for (int i = 0; i < 5; i++) {
            //Drain
            myWaterController.putEvent(new WaterEvent(this,
                    WaterEvent.WATER_DRAIN,
                    0.0));
            mailbox.doFetch(); // Wait for Ack

            // Fill
            myWaterController.putEvent(new WaterEvent(this,
                    WaterEvent.WATER_FILL,
                    0.5));
            mailbox.doFetch(); // Wait for Ack
            Thread.sleep((long) (RINSE_TIME / speed));
        }

        // Drain
        myWaterController.putEvent(new WaterEvent(this,
                WaterEvent.WATER_DRAIN,
                0.0));
        mailbox.doFetch(); // Wait for Ack

        // Centrifuge
        mySpinController.putEvent(new SpinEvent(this,
                SpinEvent.SPIN_FAST));
        Thread.sleep((long) (CENT_TIME / speed));

        // Switch off spin
		mySpinController.putEvent(new SpinEvent(this,
                SpinEvent.SPIN_OFF));

		// Set water regulation to idle => drain pump stops
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				0.0));

		// Unlock
		myMachine.setLock(false);
	}
}
