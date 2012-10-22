
public class UrgentMonitorTest
{
    public static void main( String args[] )
    {
        SCUBarrier b = new SCUBarrier( 3 );
        WorkerThreadSCU w1 = new WorkerThreadSCU( b, 1 );
        WorkerThreadSCU w2 = new WorkerThreadSCU( b, 2 );
        WorkerThreadSCU w3 = new WorkerThreadSCU( b, 3 );
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

class WorkerThreadSCU extends TDThread
{
    private int ID;
    private SCUBarrier b;

    public WorkerThreadSCU( SCUBarrier b, int ID )
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

class SCUBarrier extends UrgentMonitorSC
{
    private int n; // number of threads

    private int count; // number of waiting threads
    
    private conditionVariable proceedCondition;
    
    public SCUBarrier( int n )
    {
        this.n = n;
        this.count = n;
        this.proceedCondition = new conditionVariable( );
    }

    public void waitB( int ID )
    {
        enterMonitor( );
        
        if ( count > 1 )
        {
            // decrement the count
            count--;
            
            // while the count is non-negative, wait on the proceed condition
            proceedCondition.waitC( );
            
            if ( ID == 1 ) try { Thread.sleep( 100 ); } catch (Exception e) { };
        }
        else
        {
            // set the count back to n to reset the barrier
            count = n;
            
            if ( ID == 2 ) try { Thread.sleep( 100 ); } catch (Exception e) { };
            
            // signal all waiting threads that they may proceed
            proceedCondition.signalCall( );
        }

        exitMonitor( );
        
        if ( ID == 3 ) try { Thread.sleep( 100 ); } catch (Exception e) { };
    }
}
