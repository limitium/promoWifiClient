package me.loc2.loc2me.core.dao;

import android.provider.BaseColumns;

public final class OfferContract {

    public static final String DATABASE_NAME = "Offers.db";

    /* Inner class that defines the table contents */
    public static abstract class ReceivedOffer implements BaseColumns {
        public static final String TABLE_NAME = "received_offers";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ID_TYPE = "integer";
        public static final String COLUMN_JSON = "json";
        public static final String COLUMN_JSON_TYPE = "text";
        public static final String COLUMN_WIFI = "wifi";
        public static final String COLUMN_WIFI_TYPE = "text";
    }

    public static abstract class DeletedOffer implements BaseColumns {
        public static final String TABLE_NAME = "deleted_offers";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ID_TYPE = "integer";
    }

    private OfferContract() {

    }
}
