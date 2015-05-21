package me.loc2.loc2me.dao;

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
    }

    public static abstract class DeletedOffer implements BaseColumns {
        public static final String TABLE_NAME = "deleted_offers";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ID_TYPE = "integer";
        public static final String COLUMN_DELETED_OFFER_ID = "deleted_offer_id";
        public static final String COLUMN_DELETED_OFFER_ID_TYPE = "integer";
    }

    private OfferContract() {

    }
}
