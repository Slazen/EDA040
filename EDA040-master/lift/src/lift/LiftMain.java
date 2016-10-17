package lift;


/**
 * Created by Anton Friberg and Joakim Magnusson on 02/10/16.
 * Main class for the lift simulation.
 */
public class LiftMain {
    public static void main(String[] args) {
        LiftView lv = new LiftView();           // Create GUI
        LiftMonitor lm = new LiftMonitor(lv);     // Create Monitor

        for (int i = 0; i < 20; i++) {
            new Thread(new PersonThread(lm)).start();         // Create Person Threads
        }
        new Thread(new LiftThread(lm, lv)).start();           // Create Lift Thread
    }
}
