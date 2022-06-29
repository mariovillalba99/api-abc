package com.roshka.apiabc.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticiaAbcAux {
    private List<NoticiaAbc> data;

    public List<NoticiaAbc> getData() {
        return data;
    }

    public void setData(List<NoticiaAbc> data) {
        this.data = data;
    }
}
