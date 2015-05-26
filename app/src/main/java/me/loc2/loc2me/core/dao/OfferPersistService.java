package me.loc2.loc2me.core.dao;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.google.common.base.Optional;

import java.util.List;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.util.Ln;

public class OfferPersistService {

    private OfferDAO offerDAO;

    public OfferPersistService(Context context) {
        offerDAO = new OfferDAO(context);
    }

    public boolean saveReceived(Offer offer) {
        return offerDAO.saveReceived(offer);
    }

    public Optional<Offer> findOneReceived(Integer id) {
        return offerDAO.findOneReceived(id);
    }

    public boolean updateReceived(Offer offer) {
        return offerDAO.updateReceived(offer);
    }

    public Integer deleteReceived(Integer id) {
        return offerDAO.deleteReceived(id);
    }

    public List<Offer> findAllReceived() {
        return offerDAO.findAllReceived();
    }

    public void removeDeleted(Integer id) {
        offerDAO.removeDeleted(id);
    }

    public boolean saveDeleted(Integer id) {
        return offerDAO.saveDeleted(id);
    }

    public boolean isDeleted(Integer id) {
        return offerDAO.isDeleted(id);
    }

    public void close() {
        if (offerDAO != null) {
            try {
                offerDAO.close();
            } catch (Exception e) {
                Ln.e("Couldn't close offer db!");
            }
        }
    }
}