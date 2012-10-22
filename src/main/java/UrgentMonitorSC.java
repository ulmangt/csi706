public class UrgentMonitorSC
{
    private binarySemaphore mutex = new binarySemaphore( 1 );
    private binarySemaphore reentry = new binarySemaphore( 0 );
    private int waiting = 0;
    
    protected final class conditionVariable
    {
        private countingSemaphore threadQueue = new countingSemaphore( 0 );
        private int numWaitingThreads = 0;

        public void signalC( )
        {
            if ( numWaitingThreads > 0 )
            {
                numWaitingThreads--;
                threadQueue.V( );
            }
        }

        public void signalCall( )
        {
            while ( numWaitingThreads > 0 )
            {
                --numWaitingThreads;
                threadQueue.V( );
            }
        }

        public void waitC( )
        {
            numWaitingThreads++;
            threadQueue.VP( mutex );
            waiting++;
            reentry.P( );
            waiting--;
        }

        public boolean empty( )
        {
            return ( numWaitingThreads == 0 );
        }

        public int length( )
        {
            return numWaitingThreads;
        }
    }

    protected void enterMonitor( )
    {
        mutex.P( );
    }

    protected void exitMonitor( )
    {
        if ( waiting > 0 )
        {
            reentry.V( );
        }
        else
        {
            mutex.V( );
        }
    }
}
