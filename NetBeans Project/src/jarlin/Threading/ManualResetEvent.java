package jarlin.Threading;

/**
 *
 * @author Nikos Siatras - https://github.com/nsiatras
 */
public class ManualResetEvent
{
     private final Object fLock = new Object();
    private volatile boolean fIsOpen;

    public ManualResetEvent(boolean initialState)
    {
        fIsOpen = initialState;
    }

    public void Reset()
    {
        fIsOpen = false;
    }

    public void WaitOne()
    {
        synchronized (fLock)
        {
            while (!fIsOpen)
            {
                try
                {
                    fLock.wait();
                }
                catch (InterruptedException ex)
                {
                }
            }
        }
    }

    public boolean WaitOne(long milliseconds) throws InterruptedException
    {
        synchronized (fLock)
        {
            if (fIsOpen)
            {
                return true;
            }

            fLock.wait(milliseconds);
            return fIsOpen;
        }
    }

    public void WaitOneWithoutException(long milliseconds)
    {
        try
        {
            WaitOne(milliseconds);
        }
        catch (InterruptedException ex)
        {

        }
    }

    public void Set()
    {
        synchronized (fLock)
        {
            fIsOpen = true;
            fLock.notifyAll();
        }
    }

    public boolean getState()
    {
        return fIsOpen;
    }
}
