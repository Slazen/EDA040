package lift;

/**
 * Created by Anton Friberg and Joakim Magnusson on 02/10/16.
 * Thread simulating a moving lift.
 */
class LiftThread implements Runnable{

    private LiftMonitor lm;
    private LiftView lv;

    LiftThread(LiftMonitor lm, LiftView lv) {
        this.lm = lm;
        this.lv = lv;
    }

    @Override
    public void run() {
        while (true) {
            int[] floors = lm.move();
            int here = floors[0];
            int next = floors[1];
            lv.moveLift(here, next);
        }
    }
}
