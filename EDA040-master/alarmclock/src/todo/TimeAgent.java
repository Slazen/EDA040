package todo;

/**
 * Created by anton on 9/18/16.
 */
public class TimeAgent implements Runnable {
    private ClockData data;
    private long compTime;

    public TimeAgent(ClockData data){
        this.data = data;
    }

    public void run() {
        compTime = System.currentTimeMillis();

        while(true) {
            compTime += 1000;
            long diff = compTime - System.currentTimeMillis();
            if (diff > 0) {
                try {
                    Thread.sleep(diff);
                } catch (InterruptedException e) {
                }
            }
            data.incTime();
        }
    }
}