package me.loc2.loc2me.core;

import android.os.Handler;
import android.os.Looper;

import me.loc2.loc2me.util.Ln;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * This message bus allows you to post a message from any thread and it will get handled and then
 * posted to the toolbar_menu thread for you.
 */
public class PostFromAnyThreadBus extends Bus
{
    public PostFromAnyThreadBus()
    {
        super(ThreadEnforcer.MAIN);
    }

    @Override
    public void post(final Object event)
    {
        if (Looper.myLooper() != Looper.getMainLooper())
        {
            // We're not in the toolbar_menu loop, so we need to get into it.
            (new Handler(Looper.getMainLooper())).post(new Runnable()
            {
                @Override
                public void run()
                {
                    // We're now in the toolbar_menu loop, we can post now
                    PostFromAnyThreadBus.super.post(event);
                }
            });
        }
        else
        {
            super.post(event);
        }
    }

    @Override
    public void unregister(final Object object)
    {
        //  Lots of edge cases with register/unregister that sometimes throw.
        try
        {
            super.unregister(object);
        }
        catch (IllegalArgumentException e)
        {
            // TODO: use Crashlytics unhandled exception logging
            Ln.e(e);
        }
    }
}
