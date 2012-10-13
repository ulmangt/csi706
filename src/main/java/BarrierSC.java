public class BarrierSC
{
    public static void main( String args[] )
    {
        SCBarrier b = new SCBarrier( 3 );
        WorkerThreadSC w1 = new WorkerThreadSC( b, 1 );
        WorkerThreadSC w2 = new WorkerThreadSC( b, 2 );
        WorkerThreadSC w3 = new WorkerThreadSC( b, 3 );
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
    }
}

class WorkerThreadSC extends TDThread
{
    private int ID;
    private SCBarrier b;

    public WorkerThreadSC( SCBarrier b, int ID )
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

class SCBarrier extends monitorSC
{
    private int n; // number of threads

    private int count; // number of waiting threads
    
    private conditionVariable proceedCondition;
    
    public SCBarrier( int n )
    {
        super( "SCBarrierMonitor" );
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
        }
        else
        {
            // set the count back to n to reset the barrier
            count = n;
            
            // signal all waiting threads that they may proceed
            proceedCondition.signalCall( );
        }

        exerciseEvent( "Thread " + ID + " endWaitB" ); // ignore these calls, for now.
        exitMonitor( );
    }
}