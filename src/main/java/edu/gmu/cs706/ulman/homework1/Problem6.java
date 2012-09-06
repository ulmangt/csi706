package edu.gmu.cs706.ulman.homework1;

import java.util.concurrent.atomic.AtomicBoolean;

public class Problem6
{
    public static void main( String[] args )
    {
        int numThreads = 2;
        
        for ( int i = 0 ; i < numThreads ; i++ )
        {
            new IncrementThread( i, numThreads ).start( );
        }
    }
    
    // controls access to critical section
    private static AtomicBoolean b = new AtomicBoolean( false );
    
    // turn counter indicating which thread
    // should proceed into the critical section next
    private static int turn = 0;
    
    // counter incremented by multiple threads
    private static int s = 0;
    
    public static class IncrementThread extends Thread
    {
        private int id;
        private int numThreads;
        
        public IncrementThread( int id, int numThreads )
        {
            this.id = id;
            this.numThreads = numThreads;
        }
        
        @Override
        public void run( )
        {
            while ( true )
            {
                // return false if nothing is inside
                // the critical section and we can proceed
                while ( b.getAndSet( true ) )
                {
                    // busy wait
                }
                
                // only increment if it is our turn
                // otherwise simply exit critical section and
                // continue busy waiting
                if ( turn == id )
                {
                    s = s + 1;
                    turn = ( turn + 1 ) % numThreads;
                    
                    System.out.printf( "Thread %d takes turn (s = %d)%n", id, s );
                }

                // allow others to enter the critical section
                b.set( false );

                // non-critical section
            }
        }
    }
}
