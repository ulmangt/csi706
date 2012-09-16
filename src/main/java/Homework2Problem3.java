public class Homework2Problem3
{
    public static void main( String args[] ) throws InterruptedException
    {
        Barrier b = new Barrier( 3 );
        
        WorkerThread w1 = new WorkerThread( b, 1 );
        WorkerThread w2 = new WorkerThread( b, 2 );
        WorkerThread w3 = new WorkerThread( b, 3 );

        w1.start( );
        w2.start( );
        w3.start( );

        w1.join( );
        w2.join( );
        w3.join( );
    }

    public static class WorkerThread extends TDThread
    {
        private Barrier b;
        private int id;

        public WorkerThread( Barrier b, int id )
        {
            this.b = b;
            this.id = id;
        }

        public void run( )
        {
            for ( int i = 0; i < 2; i++ )
            {
                System.out.printf( "Worker %d did work. Iteration %d.\n", id, i );
                b.waitB( );
            }
        }
    }

    public static class Barrier
    {
        private int n;
        private int count;

        private binarySemaphore mutex;
        private binarySemaphore queue;

        public Barrier( int n )
        {
            this.count = 0;
            this.n = n;

            this.mutex = new binarySemaphore( 1 );
            this.queue = new binarySemaphore( 0 );
        }

        public void waitB( )
        {
            // enter critical section
            this.mutex.P( );
            
            // count number of waitB( ) calls
            this.count++;
            
            // if this is the last waitB( ) call
            // wake up all threads and reset the barrier
            if ( this.count == n )
            {
                this.count = 0;
                
                // wake up all queued threads
                for ( int i = 0 ; i < n-1 ; i++ )
                {
                    this.queue.V( );
                }
                
                // leave critical section
                this.mutex.V( );
            }
            else
            {
                // leave critical section
                this.mutex.V( );
                
                // wait to be woken up
                this.queue.P( );
            }
        }
    }
}
