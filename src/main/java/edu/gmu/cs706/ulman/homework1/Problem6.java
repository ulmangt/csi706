// CS706
// Homework 1
// 9/9/2012
// Wu Lan
// Geoffrey Ulman
// 
//------------------------------------------------------------------------
//
// ==== Problem 6 ====
//

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
                while ( true )
                {
                    //enter critical section to check turn variable
                    while ( b.getAndSet( true ) )
                    {

                    }

                    if ( turn == 1 )
                    {
                        // exit critical section and queue back up to enter again
                        b.set( false );
                    }
                    else
                    {
                        // it's our turn, exit loop (staying in critical section)
                        break;
                    }
                }

                // increment s inside critical section
                s = s + 1;

                // set turn inside critical section
                turn = 1;

                System.out.printf( "Thread 0 takes turn (s = %d)%n", s );

                // exit critical section
                b.set( false );
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
                while ( true )
                {
                    //enter critical section to check turn variable
                    while ( b.getAndSet( true ) )
                    {

                    }

                    if ( turn == 0 )
                    {
                        // exit critical section
                        b.set( false );
                    }
                    else
                    {
                        break;
                    }
                }

                // increment s inside critical section
                s = s + 1;

                // set turn inside critical section
                turn = 0;

                System.out.printf( "Thread 1 takes turn (s = %d)%n", s );

                // exit critical section
                b.set( false );
            }
        }
    }
}
