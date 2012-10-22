public class UrgentMonitorSC
{
    private binarySemaphore mutex = new binarySemaphore( 1 );
    
    // a queue for signaled threads to wait on
    private binarySemaphore reentry = new binarySemaphore( 0 );
    
    // a count of the total signaled threads over all condition variables
    private int numReentryThreads = 0;
    
    protected final class conditionVariable
    {
        private countingSemaphore threadQueue = new countingSemaphore( 0 );
        private int numWaitingThreads = 0;

        public void signalC( )
        {
            if ( numWaitingThreads > 0 )
            {
                numWaitingThreads--;
                // increment numReentryThreads here while we hold mutex.P()
                numReentryThreads++;
                threadQueue.V( );
            }
        }

        public void signalCall( )
        {
            while ( numWaitingThreads > 0 )
            {
                numWaitingThreads--;
                // increment numReentryThreads here while we hold mutex.P()
                numReentryThreads++;
                threadQueue.V( );
            }
        }

        public void waitC( )
        {
            numWaitingThreads++;
            threadQueue.VP( mutex );
            
            // instead of mutex.P() get in the reentry.P() queue
            reentry.P( );
            
            // we are back in the monitor, so decrement numReentryThreads
            numReentryThreads--;
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
        // if there are any signaled threads, let one of them go first
        // thus, the thread calling signal will not call mutex.V() and any
        // new threads entering the monitor will block on mutex.P() in enterMonitor()
        if ( numReentryThreads > 0 )
        {
            reentry.V( );
        }
        else
        {
            mutex.V( );
        }
    }
}
