
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

public class Homework2Problem2Test
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

    private static int turn = 0;
    
    private static int waiting = 0;
    
    // counter incremented by multiple threads
    private static sharedInteger s = new sharedInteger( 0, "s" );

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
                mutex.P( );
                
                if ( turn == 1 )
                {
                    waiting++;
                    
                    mutex.V( );
                    
                    queue.P( );
                }
                
                s.Write( s.Read( ) + 1 );
                
                turn = 1;
                
                System.out.printf( "Thread 0 takes turn (s = %d)%n", s.Read( ) );
                
                if ( waiting > 0 )
                {
                    waiting--;
                    
                    if ( s.Read( ) > 2 )
                    {
                        queue.V( );
                        break;
                    }
                    
                    queue.V( );
                }
                else
                {
                    if ( s.Read( ) > 2 )
                    {
                        mutex.V( );
                        break;
                    }
                    
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
                
                s.Write( s.Read( ) + 1 );
                
                turn = 0;
                
                System.out.printf( "Thread 1 takes turn (s = %d)%n", s.Read( ) );
                
                if ( waiting > 0 )
                {
                    waiting--;
                    
                    if ( s.Read( ) > 2 )
                    {
                        queue.V( );
                        break;
                    }
                    
                    queue.V( );
                }
                else
                {
                    if ( s.Read( ) > 2 )
                    {
                        mutex.V( );
                        break;
                    }
                    
                    mutex.V( );
                }
            }
        }
    }
}
