package lift;

/**
 * Created by Anton Friberg and Joakim Magnusson on 02/10/16.
 * The data structures for the simulated lift with synchronized control.
 */
class LiftMonitor {
    private LiftView lv;
    private int here;
    private int next;
    private int[] waitEntry;
    private int[] waitExit;
    private int load;
    private int maxLoad = 4;
    private boolean headingUp;

    LiftMonitor(LiftView lv) {
        this.lv = lv;
        waitEntry = new int[7];
        waitExit  = new int[7];
        headingUp = true;
    }

    synchronized int[] move() {
        here = next;     // move lift to next floor
        notifyAll();

        while (!readyToLeave()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        next = headingUp ? next+1 : next-1;                 // if moving up add 1 else remove 1
        if (next == 6 || next == 0) headingUp = !headingUp; // flip direction on floor limits
        return new int[]{here, next};
    }

    synchronized void ride(int start, int destination) {
        waitEntry[start]++;         // add person to wait at start floor
        lv.drawLevel(start, waitEntry[start]);
        notifyAll();

        while (!(liftStill() && here == start && !overCapacity())) { // wait for non-full lift
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        waitEntry[start]--;
        lv.drawLevel(start, waitEntry[start]);
        waitExit[destination]++;
        load++;
        lv.drawLift(here, load);
        notifyAll();

        while(!(liftStill() && here == destination)) { // wait for correct floor to leave elevator
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        waitExit[destination]--;
        load--;
        lv.drawLift(here, load);
        notifyAll();
    }

    private boolean liftStill()     { return (here == next);}
    private boolean allEntered()    { return (waitEntry[here] == 0);}
    private boolean allExited()     { return (waitExit[here]  == 0);}
    private boolean overCapacity()  { return (load >= maxLoad);}
    private boolean readyToLeave()  { return allExited() && (allEntered() || overCapacity());}
}
