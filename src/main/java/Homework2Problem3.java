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

            this.mutex = new binarySemaphore( 1, "mutex" );
            this.queue = new binarySemaphore( 0, "queue" );
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
                // wake up one waiting thread
                // (pass baton -- do not leave critical section)
                this.queue.V( );
            }
            else
            {
                // leave critical section
                this.mutex.V( );
                
                // wait to be woken up
                this.queue.P( );
                
                // (we have been passed the baton -- enter critical section)
                
                // we're out of the queue, so decrement count
                this.count--;
                
                // wake up the next thread (passing baton to it)
                this.queue.V( );
                
                if ( this.count == 0 )
                {
                    // leave critical section
                    this.mutex.V( );
                }
            }
        }
    }
}
