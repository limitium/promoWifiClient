package me.loc2.loc2me.core.models;

public class UsedOffer {

    private Integer offerId;
    private String mac;

    public UsedOffer() {

    }

    public UsedOffer(Integer offerId, String mac) {
        this.offerId = offerId;
        this.mac = mac;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
