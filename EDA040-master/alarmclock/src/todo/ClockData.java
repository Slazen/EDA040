package todo;

import done.ClockInput;
import done.ClockOutput;
import se.lth.cs.realtime.semaphore.MutexSem;

/**
 * Created by anton on 9/18/16.
 */
public class ClockData {

    private int t = 0;          //clock time
    private int alarmTime   = 0;
    private int buttonState;
    private ClockOutput output;
    private ClockInput input;
    private MutexSem sem;
    private int doAlarmCount = 0;

    public ClockData(ClockOutput output, ClockInput input) {
        sem         = new MutexSem();
        this.output = output;
        this.input  = input;
    }

    public void incTime(){
        sem.take();
        t += 1; // Method is called once every second

        /* The code below handles time formatting for the showTime method
           the format is hhmmss (hours, minutes, seconds). */

        if (t % 100 == 60) {            // seconds == 60
            t += 100;                   // minutes + 1
            t -= 60;                    // seconds = 00

            if (t % 10000 == 6000) {    // minutes == 60
                t += 10000;             // hours + 1
                t -= 6000;             // minutes = 00

                if (t == 240000) t = 0; // new day
            }
        }

        output.showTime(t);             // display time

        if (input.getAlarmFlag() && alarmTime == t) {
            doAlarmCount = 20;          // beep for 20 seconds
        }
        if (doAlarmCount > 0) {
            output.doAlarm();           // sound the alarm
            doAlarmCount -= 1;          // decrease beep count
            if (doAlarmCount == 0) {
			}
        }
        sem.give();
    }

    public void buttonAction(){
        sem.take();

        if (input.getChoice() != buttonState) { // user pressed button
        	doAlarmCount = 0;
            if (buttonState == ClockInput.SET_ALARM){
                alarmTime = input.getValue();
            } else if (buttonState == ClockInput.SET_TIME) {
                t = input.getValue();
            }
        }
        buttonState = input.getChoice();
        sem.give();
    }
}
