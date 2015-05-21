package me.loc2.loc2me.dao;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.common.base.Optional;
import com.squareup.otto.Bus;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.util.Ln;

public class OfferPersistService extends Service {

    @Inject
    protected Bus eventBus;

    private OfferDAO offerDAO;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        offerDAO = new OfferDAO(this);
        Injector.inject(this);
        // Register the bus so we can send notifications.
        eventBus.register(this);
    }

    public boolean saveReceived(Offer offer) {
        return offerDAO.saveReceived(offer);
    }

    public Optional<Offer> findOneReceived(BigInteger id) {
        return offerDAO.findOneReceived(id);
    }

    public boolean updateReceived(Offer offer) {
        return offerDAO.updateReceived(offer);
    }

    public Integer deleteReceived(BigInteger id) {
        return offerDAO.deleteReceived(id);
    }

    public List<Offer> findAllReceived() {
        return offerDAO.findAllReceived();
    }

    public void removeDeleted(BigInteger id) {
        offerDAO.removeDeleted(id);
    }

    public boolean saveDeleted(BigInteger id) {
        return offerDAO.saveDeleted(id);
    }

    public boolean isDeleted(BigInteger id) {
        return offerDAO.isDeleted(id);
    }

    @Override
    public void onDestroy() {
        if (offerDAO != null) {
            try {
                offerDAO.close();
            } catch (Exception e) {
                Ln.e("Couldn't close offer db!");
            }
        }
    }
}
