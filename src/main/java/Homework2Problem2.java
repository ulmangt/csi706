// CS706
// Homework 2
// 9/27/2012
// Wu Lan
// Geoffrey Ulman
// 
//------------------------------------------------------------------------
//
// ==== Problem 2 ====
//

public class Homework2Problem2
{
    public static void main( String[] args )
    {
        new IncrementThread0( ).start( );
        new IncrementThread1( ).start( );
    }

    // controls access to critical section
    private static countingSemaphore mutex = new countingSemaphore( 1 );

    // semaphore for thread 0 to block on while awaiting its turn
    // ** starts at 1 to indicate that it is thread 0's turn initially **
    private static countingSemaphore turn_queue_0 = new countingSemaphore( 1 );

    // semaphore for thread 1 to block on while awaiting its turn
    private static countingSemaphore turn_queue_1 = new countingSemaphore( 0 );

    // counter incremented by multiple threads
    private static int s = 0;

    public static class IncrementThread0 extends Thread
    {
        @Override
        public void run( )
        {
            while ( true )
            {
                // wait outside of the critical section for our turn
                // this violates progress, but this is inevitable
                // because we are specifically required to have the
                // threads take turns
                turn_queue_0.P( );

                // start critical section
                mutex.P( );

                // increment s inside critical section
                s = s + 1;

                // notify thread 1 that it is their turn
                turn_queue_1.V( );

                System.out.printf( "Thread 0 takes turn (s = %d)%n", s );

                // end critical section
                mutex.V( );
            }
        }
    }

    public static class IncrementThread1 extends Thread
    {
        @Override
        public void run( )
        {
            while ( true )
            {
                // wait outside of the critical section for our turn
                // this violates progress, but this is inevitable
                // because we are specifically required to have the
                // threads take turns
                turn_queue_1.P( );

                // start critical section
                mutex.P( );

                // increment s inside critical section
                s = s + 1;

                // notify thread 0 that it is their turn
                turn_queue_0.V( );

                System.out.printf( "Thread 1 takes turn (s = %d)%n", s );

                // end critical section
                mutex.V( );
            }
        }
    }
}
