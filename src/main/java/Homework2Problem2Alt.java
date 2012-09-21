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

public class Homework2Problem2Alt
{
    public static void main( String[] args ) throws InterruptedException
    {
        TDThread t0 = new IncrementThread0( "thread0" );
        TDThread t1 = new IncrementThread1( "thread1" );

        t0.start( );
        t1.start( );

        t0.join( );
        t1.join( );
    }

    // controls access to critical section
    private static countingSemaphore mutex = new countingSemaphore( 1, "mutex" );

    // semaphore for thread 1 to block on while awaiting its turn
    private static countingSemaphore queue = new countingSemaphore( 0, "queue" );

    // the thread whose turn it is to enter the critical section
    private static int turn = 0;

    // indicates how many threads are waiting on queue
    private static int waiting = 0;

    // counter incremented by multiple threads
    private static int s = 0;

    public static class IncrementThread0 extends TDThread
    {
        public IncrementThread0( String name )
        {
            super( name );
        }

        @Override
        public void run( )
        {
            while ( true )
            {
                // enter critical section
                mutex.P( );

                // check whether it is out turn
                if ( turn == 1 )
                {
                    // indicate we will be waiting on queue.P()
                    waiting++;

                    // exit critical section before waiting on queue.P()
                    mutex.V( );

                    // baton passing technique used below avoids VP type errors here
                    queue.P( );
                }

                // once here, we either called mutex.P() above or were 
                // passed the baton by the caller of queue.V()
                // so we are in the critical section
                
                // increment the counter
                s = s + 1;

                // flip the turn variable
                turn = 1;

                System.out.printf( "Thread 0 takes turn (s = %d)%n", s );

                // if the other thread is waiting, we need to notify them
                if ( waiting > 0 )
                {
                    waiting--;

                    queue.V( );
                }
                // otherwise, simply exit the critical section
                else
                {
                    mutex.V( );
                }
            }
        }
    }

    public static class IncrementThread1 extends TDThread
    {
        public IncrementThread1( String name )
        {
            super( name );
        }

        @Override
        public void run( )
        {
            while ( true )
            {
                mutex.P( );

                if ( turn == 0 )
                {
                    waiting++;

                    mutex.V( );

                    queue.P( );
                }

                s = s + 1;

                turn = 0;

                System.out.printf( "Thread 1 takes turn (s = %d)%n", s );

                if ( waiting > 0 )
                {
                    waiting--;

                    queue.V( );
                }
                else
                {
                    mutex.V( );
                }
            }
        }
    }
}
