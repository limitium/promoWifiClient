package me.loc2.loc2me.core.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.common.base.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.OfferSerializer;
import me.loc2.loc2me.util.Ln;

public class OfferDAO extends SQLiteOpenHelper {

    private OfferSerializer offerSerializer;

    public OfferDAO(Context context) {
        super(context, OfferContract.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createReceivedOffer = "create table " + OfferContract.ReceivedOffer.TABLE_NAME +
                "(" + OfferContract.ReceivedOffer.COLUMN_ID + " " + OfferContract.ReceivedOffer.COLUMN_ID_TYPE + " primary key" +
                ", " + OfferContract.ReceivedOffer.COLUMN_JSON + " " + OfferContract.ReceivedOffer.COLUMN_JSON_TYPE + ");";
        String createDeletedOffer = "create table " + OfferContract.DeletedOffer.TABLE_NAME +
                "(" + OfferContract.DeletedOffer.COLUMN_ID + " " + OfferContract.DeletedOffer.COLUMN_ID_TYPE + " primary key" +
                ", " + OfferContract.DeletedOffer.COLUMN_DELETED_OFFER_ID + " " + OfferContract.DeletedOffer.COLUMN_DELETED_OFFER_ID_TYPE + ");";
        db.execSQL(createReceivedOffer);
        db.execSQL(createDeletedOffer);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + OfferContract.ReceivedOffer.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OfferContract.DeletedOffer.TABLE_NAME);
        onCreate(db);
    }

    public boolean saveReceived(Offer offer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(OfferContract.ReceivedOffer.COLUMN_ID, offer.getId());
        try {
            contentValues.put(OfferContract.ReceivedOffer.COLUMN_JSON, getOfferSerializer().serialize(offer));
            db.insert(OfferContract.ReceivedOffer.TABLE_NAME, null, contentValues);
        } catch (IOException e) {
            Ln.e("Failed to serialize offer: " + offer);
            return false;
        }
        return true;
    }

    public Optional<Offer> findOneReceived(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + OfferContract.ReceivedOffer.TABLE_NAME + " where id=" + id + "", null);
        try {
            if (res.getCount() > 0) {
                res.moveToFirst();
                String serialized = res.getString(res.getColumnIndex(OfferContract.ReceivedOffer.COLUMN_JSON));
                return Optional.fromNullable(getOfferSerializer().deserialize(serialized));
            }
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            res.close();
        }
        return Optional.absent();
    }

    public boolean updateReceived(Offer offer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OfferContract.ReceivedOffer.COLUMN_ID, offer.getId());
        try {
            contentValues.put(OfferContract.ReceivedOffer.COLUMN_JSON, getOfferSerializer().serialize(offer));
            db.update(OfferContract.ReceivedOffer.TABLE_NAME, contentValues, OfferContract.ReceivedOffer.COLUMN_ID + " = ? ",
                    new String[]{Long.toString(offer.getId().longValue())});
        } catch (IOException e) {
            Ln.e(e);
            return false;
        }
        return true;
    }

    public Integer deleteReceived(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(OfferContract.ReceivedOffer.TABLE_NAME,
                OfferContract.ReceivedOffer.COLUMN_ID + " = ? ",
                new String[]{Long.toString(id.longValue())});
    }

    public List<Offer> findAllReceived() {
        LinkedList<Offer> result = new LinkedList<>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + OfferContract.ReceivedOffer.TABLE_NAME, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            try {
                Offer offer = getOfferSerializer().deserialize(res.getString(res.getColumnIndex(OfferContract.ReceivedOffer.COLUMN_JSON)));
                result.add(offer);
            } catch (IOException e) {
                Ln.e("Failed to read some offers!");
            }
            res.moveToNext();
        }
        res.close();
        return result;
    }

    public boolean isDeleted(Integer id) {
        boolean result;
        SQLiteDatabase db = this.getReadableDatabase();

        String Query = "Select * from " + OfferContract.DeletedOffer.TABLE_NAME + " where " +
                OfferContract.DeletedOffer.COLUMN_DELETED_OFFER_ID + " = " + id.longValue();
        Cursor cursor = db.rawQuery(Query, null);
        result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public boolean saveDeleted(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OfferContract.DeletedOffer.COLUMN_ID, id.longValue());
        long result = db.insert(OfferContract.DeletedOffer.TABLE_NAME, null, contentValues);
        return -1 != result;
    }

    public void removeDeleted(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(OfferContract.DeletedOffer.TABLE_NAME,
                OfferContract.DeletedOffer.COLUMN_DELETED_OFFER_ID + " = ? ",
                new String[]{Long.toString(id.longValue())});
    }


    private OfferSerializer getOfferSerializer() {
        if (null == offerSerializer) {
            offerSerializer = new OfferSerializer();
        }
        return offerSerializer;

    }


}
