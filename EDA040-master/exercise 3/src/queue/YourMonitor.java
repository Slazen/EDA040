package queue;

class YourMonitor {
	private int nCounters;
	private int lastCustomerArrived = 99;   // Start with zero after inc.
	private int lastCustomerServed = 99;    // - " -
    private int nbrOfFreeClerks;
    private int lca;                        // Last counter assigned.
    private boolean[] counterFree;

	YourMonitor(int n) { 
		nCounters = n;
		counterFree = new boolean[n];
	}

	/**
	 * Return the next queue number in the intervall 0...99. 
	 * There is never more than 100 customers waiting.
	 */
	synchronized int customerArrived() { 
		lastCustomerArrived = (lastCustomerArrived + 1) % 100;
		notifyAll();
        return lastCustomerArrived;
	}

	/**
	 * Register the clerk at counter id as free. Send a customer if any. 
	 */
	synchronized void clerkFree(int id) { 
		if (!counterFree[id]) { // Ignoring multiple button pushes.
            nbrOfFreeClerks++;
            counterFree[id] = true;
            notifyAll();
        }
	}

	/**
	 * Wait for there to be a free clerk and a waiting customer, then
	 * return the queue number of next customer to serve and the counter
	 * number of the engaged clerk.
	 */
	synchronized DispData getDisplayData() throws InterruptedException { 
		while (lastCustomerArrived == lastCustomerServed || nbrOfFreeClerks == 0) {
            wait();
        }
        while (!counterFree[lca]) lca = (lca + 1)%nCounters; // Fairness.
        DispData ans = new DispData();
        ans.counter = lca;
        counterFree[lca] = false;
        nbrOfFreeClerks--;
        ans.ticket = lastCustomerServed = (lastCustomerServed + 1) % 100;
        return ans;
	}
}
