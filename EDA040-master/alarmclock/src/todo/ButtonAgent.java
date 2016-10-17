package todo;
import se.lth.cs.realtime.semaphore.*;

/**
 * Created by anton on 9/18/16.
 */
public class ButtonAgent implements Runnable{
    private Semaphore sem;
    private ClockData data;

    public ButtonAgent(Semaphore sem, ClockData data){
        this.sem  = sem;
        this.data = data;
    }

    public void run() {
        while(true) {
            sem.take();
            data.buttonAction();
        }
    }
}
