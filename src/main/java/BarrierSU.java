
public class BarrierSU
{
    public static void main( String args[] )
    {
        SUBarrier b = new SUBarrier( 3 );
        WorkerThreadSU w1 = new WorkerThreadSU( b, 1 );
        WorkerThreadSU w2 = new WorkerThreadSU( b, 2 );
        WorkerThreadSU w3 = new WorkerThreadSU( b, 3 );
        w1.start( );
        w2.start( );
        w3.start( );
        try
        {
            w1.join( );
            w2.join( );
            w3.join( );
        }
        catch ( InterruptedException e )
        {
        }
        
        System.out.println("-------");
    }
}

class WorkerThreadSU extends TDThread
{
    private int ID;
    private SUBarrier b;

    public WorkerThreadSU( SUBarrier b, int ID )
    {
        super( "WorkerThread" + ID );
        this.ID = ID;
        this.b = b;
    }

    public void run( )
    {
        for ( int i = 0; i < 2; i++ )
        {
            System.out.println("Worker"+ID+" did work");
            b.waitB( ID );
        }
    }
}

class SUBarrier extends monitorSU
{
    private int n; // number of threads

    private int count; // number of waiting threads
    
    private conditionVariable proceedCondition;
    
    public SUBarrier( int n )
    {
        super( "SUBarrierMonitor" );
        this.n = n;
        this.count = n;
        this.proceedCondition = new conditionVariable( );
    }

    public void waitB( int ID )
    {
        enterMonitor( "waitB" );
        exerciseEvent( "Thread " + ID + " beginWaitB" ); // ignore these calls, for now.

        // decrement the count
        count--;
        
        if ( count > 0 )
        {            
            // while the count is non-negative, wait on the proceed condition
            proceedCondition.waitC( );

            count++;
            
            // once we wake up, signal the next waiting thread
            proceedCondition.signalC( );
        }
        else
        {
            // signal all waiting threads that they may proceed
            proceedCondition.signalC( );
        }

        exerciseEvent( "Thread " + ID + " endWaitB" ); // ignore these calls, for now.
        exitMonitor( );
    }
}