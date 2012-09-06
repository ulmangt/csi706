package edu.gmu.cs706.ulman.homework1;

import java.util.concurrent.atomic.AtomicBoolean;

public class Problem5
{

    public static void main( String[] args )
    {
        Thread t1 = new Thread( new Runner( ) );
        Thread t2 = new Thread( new Runner( ) );

        t1.start( );
        t2.start( );
    }

    public static AtomicBoolean b = new AtomicBoolean( false );

    // mutual exclusion and progress are guaranteed,
    // but not bounded waiting (the thread exiting the
    // critical section and calling set(false) could
    // always be the next thread to call getAndSet(true)
    public static class Runner implements Runnable
    {
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

                // critical section

                // allow others to enter the critical section
                b.set( false );

                // non-critical section
            }
        }
    }
}
