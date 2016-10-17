package buffer;

import se.lth.cs.realtime.semaphore.*;

/**
 * The buffer.
 */
class Buffer {
	Semaphore mutex;        // For mutual exclusion blocking.
	Semaphore free;         // For buffer full blocking.
	Semaphore avail;        // For blocking when no data is available.
    final int size = 8;     // The number of buffered strings.
	String[] buffData;      // The actual buffer.
    int nextToPut;          // Writers index init. to zero by Java).
    int nextToGet;          // Readers index.

	Buffer() {
        buffData = new String[size];
		mutex = new MutexSem();
		free = new CountingSem(size);
		avail = new CountingSem();
	}

	void putLine(String input) {
		free.take();                            // Wait for buffer empty.
		mutex.take();                           // Wait for exclusive access.
		buffData[nextToPut] = new String(input);// Store copy of object.
        if (++nextToPut >= size) nextToPut = 0; // Next index.
		mutex.give();                           // Allow others to access.
		avail.give();                           // Allow others to get line.
	}

	String getLine() {
		avail.take();			// wait for data available.
		mutex.take();
		String ans = buffData[nextToGet];	// get reference to data.
		buffData[nextToGet] = null;		// extra care, not really needed here
        if (++nextToGet >= size) nextToGet = 0; // Next index.
		mutex.give();
		free.give();
		return ans;
	}
}
