package com.roshka.apiabc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PromoItems {
    public BasicModel basic;


    public BasicModel getBasic() {
        return basic;
    }

    public void setBasic(BasicModel basic) {
        this.basic = basic;
    }
}
