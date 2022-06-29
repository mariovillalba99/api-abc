package com.roshka.apiabc.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.ResponseBody;

public class Noticia {
    private String fecha; //publish_date
    private String enlace; //_website_urls
    @JsonProperty("enlace_foto")
    private String enlaceFoto; //promo_items
    private String titulo; //headlines
    private String resumen; //subheadlines
    @JsonProperty("contenido_foto")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contenidoFoto;
    @JsonProperty("content_type_foto")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contentTypeFoto;

    public Noticia() {
    }

    public Noticia(String fecha, String enlace,
                   String enlaceFoto, String titulo,
                   String resumen, String contenidoFoto,
                   String contentTypeFoto) {
        this.fecha = fecha;
        this.enlace = enlace;
        this.enlaceFoto = enlaceFoto;
        this.titulo = titulo;
        this.resumen = resumen;
        this.contenidoFoto = contenidoFoto;
        this.contentTypeFoto = contentTypeFoto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getEnlaceFoto() {
        return enlaceFoto;
    }

    public void setEnlaceFoto(String enlaceFoto) {
        this.enlaceFoto = enlaceFoto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getContenidoFoto() {
        return contenidoFoto;
    }

    public void setContenidoFoto(String contenidoFoto) {
        this.contenidoFoto = contenidoFoto;
    }

    public String getContentTypeFoto() {
        return contentTypeFoto;
    }

    public void setContentTypeFoto(String contentTypeFoto) {
        this.contentTypeFoto = contentTypeFoto;
    }
}
