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
    private static countingSemaphore turn_queue_0 = new countingSemaphore( 1 );
    
    // semaphore for thread 1 to block on while awaiting its turn
    private static countingSemaphore turn_queue_1 = new countingSemaphore( 0 );
    
    // turn counter indicating which thread
    // should proceed into the critical section next
//    private static int turn = 0;
    
    private static boolean waiting_0 = false;
    private static boolean waiting_1 = false;
    
    // counter incremented by multiple threads
    private static int s = 0;
    
    public static class IncrementThread0 extends Thread
    {
        @Override
        public void run( )
        {
            while ( true )
            {                
                // start entry section
//                mutex.P( );
                
                waiting_0 = true;
                
                // release mutex so that we don't block
                // on turn_queue.P( ) holding mutex lock
//                mutex.V( );
                
                // wait outside of the critical section for our turn
                // this violates progress, but this is inevitable
                // because we are specifically required to have the
                // threads take turns
                //
                // consider possible VP bug:
                // if a context switch occurs here
                turn_queue_0.P( );
                
                // re-acquire mutex to decrement waiting counter
//                mutex.P( );
                
                waiting_0 = false;
                
                // end entry section
//                mutex.V( );
                
                // start critical section
                mutex.P( );
                
                // increment s inside critical section
                s = s + 1;
                
                // set turn inside critical section
                turn = 1;
                
                // notify Thread 1 that it is their turn
                if ( waiting_1 )
                {
                    turn_queue_1.V( );
                }

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
                // start entry section
//              mutex.P( );
              
              waiting_1 = true;
              
              // release mutex so that we don't block
              // on turn_queue.P( ) holding mutex lock
//              mutex.V( );
              
              // wait outside of the critical section for our turn
              // this violates progress, but this is inevitable
              // because we are specifically required to have the
              // threads take turns
              //
              // consider possible VP bug:
              // if a context switch occurs here
              turn_queue_1.P( );
              
              // re-acquire mutex to decrement waiting counter
//              mutex.P( );
              
              waiting_1 = false;
              
              // end entry section
//              mutex.V( );
                
                // start critical section
                mutex.P( );
                
                // increment s inside critical section
                s = s + 1;
                
                // set turn inside critical section
                turn = 0;
                
                // notify Thread 0 that it is their turn
                if ( waiting_0 )
                {
                    turn_queue_0.V( );
                }

                System.out.printf( "Thread 1 takes turn (s = %d)%n", s );
                
                // end critical section
                mutex.V( );
            }
        }
    }
}
