

package me.loc2.loc2me;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

/**
 * Loc2me application
 */
public class Loc2meApplication extends Application {

    private static Loc2meApplication instance;

    /**
     * Create main application
     */
    public Loc2meApplication() {
    }

    /**
     * Create main application
     *
     * @param context
     */
    public Loc2meApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Perform injection
        Injector.init(getRootModule(), this);

    }

    private Object getRootModule() {
        return new RootModule();
    }


    /**
     * Create main application
     *
     * @param instrumentation
     */
    public Loc2meApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static Loc2meApplication getInstance() {
        return instance;
    }
}
