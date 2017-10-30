package com.nnamdianinye.cryptodechange;

public class TrueOrFalse {
    boolean btc, eth;

    TrueOrFalse() {

    }

    public TrueOrFalse(boolean btc, boolean eth) {
        this.btc = btc;
        this.eth = eth;
    }

    public boolean isBtc() {
        return btc;
    }

    public void setBtc(boolean btc) {
        this.btc = btc;
    }

    public boolean isEth() {
        return eth;
    }

    public void setEth(boolean eth) {
        this.eth = eth;
    }
}
