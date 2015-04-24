package me.loc2.loc2me;

import dagger.Module;

/**
 * Add all the other modules to this one.
 */
@Module(
        includes = {
                AndroidModule.class,
                Loc2meModule.class
        }
)
public class RootModule {
}
