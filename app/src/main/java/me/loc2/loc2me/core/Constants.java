

package me.loc2.loc2me.core;

/**
 * Bootstrap constants
 */
public final class Constants {
    private Constants() {}

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */
    public static final class Http {
        private Http() {}

        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "http://loc2me.cloudapp.net";

    }


    public static final class Extra {
        private Extra() {}

        public static final String WISE_ITEM = "wise_item";

        public static final String USER = "user";

    }

    public static final class Prefs {
        private Prefs() {}

        public static final String NAME = "loc2me";
        public static final String FILTER_SALE_PERCENT = "FILTER_SALE_PERCENT";
        public static final String FILTER_ELECTRONICS= "FILTER_ELECTRONICS";
        public static final String FILTER_ENTERTAIMENT = "FILTER_ENTERTAIMENT";
        public static final String FILTER_FASHION = "FILTER_FASHION";
        public static final String FILTER_MOTOR = "FILTER_MOTOR";
        public static final String FILTER_COLLECTINABLES_ART= "FILTER_COLLECTINABLES_ART";
        public static final String FILTER_HOME_GARDEN = "FILTER_HOME_GARDEN";
        public static final String FILTER_SPORTS= "FILTER_SPORTS";
        public static final String FILTER_TOYS_HOBBIES= "FILTER_TOYS_HOBBIES";


    }

    public static final class Storage {
        private Storage() {}

        public static final String FILE_NAME = "me.loc2.loc2me.store";


    }

    public static final class Intent {
        private Intent() {}

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "me.loc2.loc2me.";

    }

    public static class Notification {
        private Notification() {
        }

        public static final int TIMER_NOTIFICATION_ID = 1000; // Why 1000? Why not? :)
        public static final int SCAN_NOTIFICATION_ID = 2000; // Why 1000? Why not? :)
    }

}


