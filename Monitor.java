package comp346Ass3;

/**
 * Class Monitor To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */

/*
 * Done by Abdallah Said 40099027 and Mehrdod Ilhompur 40078633
 */
public class Monitor {
	enum State {
		THINKING, EATING, TALKING
	}

	private int num;

	private State state[]; // state of each philosopher

	private boolean someoneTalking; // flag on whether a philosopher is talking

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers) {
		// TODO: set appropriate number of chopsticks based on the # of philosophers

		num = piNumberOfPhilosophers; // set num to the number of philosophers
		state = new State[num]; // allocate num State memory space
		for (int i = 0; i < num; i++) {
			state[i] = State.THINKING; // initialize each philosopher to THINKING
		}
		someoneTalking = false; // initialize someone talking flag to false
	}

	/*
	 * ------------------------------- User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID) {
		try

		{
			int index = piTID - 1; // index corresponds to philosopher
			state[index] = State.THINKING; // setting the state in this case in Thinking
			while (true) {
				// only if philosopher left neighbour and right neighbour aren't eating (if both chopsticks are free)
				// and also making sure that he is not currently eating, after he can proceed

				if (state[(index + 1) % num] != State.EATING && state[(index + num - 1) % num] != State.EATING
						&& state[index] != State.EATING) {
					state[index] = State.EATING; // setting state to eating
					//notifyAll(); // allow waiting threads to try to eat (continue)
					return; // exits
				} else {
					wait(); // if condition is not met wait temporarily gives up access and must reacquire
							// after the condition has been met
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down and
	 * let others know they are available.
	 */
	public synchronized void putDown(final int piTID) {
		int index = piTID - 1;          //index corresponding to philosopher
		state[index] = State.THINKING;		//setting state to Thinking
		//notifyAll();		//allows waiting threads to continue and exits
		return;
	}

	/**
	 * Only one philopher at a time is allowed to philosophy (while she is not
	 * eating).
	 */
	public synchronized void requestTalk(final int piTID) {
		try {
			while (true) // infinite loop
			{
				// if there is no one talking and the given philosopher  is not eating
				if (!someoneTalking && state[piTID - 1] != State.EATING) {
					state[piTID - 1] = State.TALKING;  //setting state to Talking
					someoneTalking = true;		//set talking to true
					notifyAll();   
					break;    //exits
				} else {
					wait();  //else wait until someone finish talking
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When one philosopher is done talking stuff, others can feel free to start
	 * talking.
	 */
	public synchronized void endTalk(final int piTID) {
		state[piTID - 1] = State.THINKING;   //setting state to thinking
		someoneTalking = false;			//set to false
		notifyAll();		//allow other threads to try to request talk.
		return; //exits
	}
}

