

package me.loc2.loc2me.core;

/**
 * Bootstrap constants
 */
public final class Constants {
    private Constants() {}

    public static final class Auth {
        private Auth() {}

        /**
         * Account type id
         */
        public static final String BOOTSTRAP_ACCOUNT_TYPE = "me.loc2.loc2me";

        /**
         * Account name
         */
        public static final String BOOTSTRAP_ACCOUNT_NAME = "Loc2me";

        /**
         * Provider id
         */
        public static final String BOOTSTRAP_PROVIDER_AUTHORITY = "me.loc2.loc2me.sync";

        /**
         * Auth token type
         */
        public static final String AUTHTOKEN_TYPE = BOOTSTRAP_ACCOUNT_TYPE;
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */
    public static final class Http {
        private Http() {}


        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://api.parse.com";


        /**
         * Authentication URL
         */
        public static final String URL_AUTH_FRAG = "/1/login";
        public static final String URL_AUTH = URL_BASE + URL_AUTH_FRAG;

        /**
         * List Users URL
         */
        public static final String URL_USERS_FRAG =  "/1/users";
        public static final String URL_USERS = URL_BASE + URL_USERS_FRAG;


        /**
         * List News URL
         */
        public static final String URL_NEWS_FRAG = "/1/classes/News";
        public static final String URL_NEWS = URL_BASE + URL_NEWS_FRAG;


        /**
         * List Checkin's URL
         */
        public static final String URL_CHECKINS_FRAG = "/1/classes/Locations";
        public static final String URL_CHECKINS = URL_BASE + URL_CHECKINS_FRAG;

        /**
         * PARAMS for auth
         */
        public static final String PARAM_USERNAME = "username";
        public static final String PARAM_PASSWORD = "password";


        public static final String PARSE_APP_ID = "zHb2bVia6kgilYRWWdmTiEJooYA17NnkBSUVsr4H";
        public static final String PARSE_REST_API_KEY = "N2kCY1T3t3Jfhf9zpJ5MCURn3b25UpACILhnf5u9";
        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String SESSION_TOKEN = "sessionToken";


    }


    public static final class Extra {
        private Extra() {}

        public static final String WISE_ITEM = "wise_item";

        public static final String USER = "user";

    }

    public static final class Prefs {
        private Prefs() {}

        public static final String NAME = Auth.BOOTSTRAP_ACCOUNT_TYPE;
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
        public static final int INFO_NOTIFICATION_ID = 3000; // Why 1000? Why not? :)
        public static final int STORAGE_NOTIFICATION_ID = 4000; // Why 1000? Why not? :)
    }

}


