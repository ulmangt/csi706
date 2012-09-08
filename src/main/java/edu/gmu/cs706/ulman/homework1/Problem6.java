package edu.gmu.cs706.ulman.homework1;

import java.util.concurrent.atomic.AtomicBoolean;

public class Problem6
{
    public static void main( String[] args )
    {
        new IncrementThread0( ).start( );
        new IncrementThread1( ).start( );
    }
    
    // controls access to critical section
    private static AtomicBoolean b = new AtomicBoolean( false );
    
    // turn counter indicating which thread
    // should proceed into the critical section next
    private static volatile int turn = 0;
    
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
                // this violates progress requirement, but this is inevitable
                // because we are specifically required to have the
                // threads take turns
                while( turn == 1 )
                {
                    // busy wait
                }
                
                // return false if nothing is inside
                // the critical section and we can proceed
                while ( b.getAndSet( true ) )
                {
                    // busy wait
                }
                
                // increment s inside critical section
                s = s + 1;
                
                // set turn inside critical section
                turn = 1;

                System.out.printf( "Thread 0 takes turn (s = %d)%n", s );
                
                // allow others to enter the critical section
                b.set( false );

                // non-critical section
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
                // this violates bounded wait, but this is inevitable
                // because we are specifically required to have the
                // threads take turns
                while( turn == 0 )
                {
                    // busy wait
                }
                
                // return false if nothing is inside
                // the critical section and we can proceed
                while ( b.getAndSet( true ) )
                {
                    // busy wait
                }
                
                // increment s inside critical section
                s = s + 1;
                
                // set turn inside critical section
                turn = 0;

                System.out.printf( "Thread 1 takes turn (s = %d)%n", s );
                
                // allow others to enter the critical section
                b.set( false );

                // non-critical section
            }
        }
    }
}
