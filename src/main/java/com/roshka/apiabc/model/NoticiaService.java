package com.roshka.apiabc.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roshka.apiabc.error.ApiError;
import com.roshka.apiabc.util.DateFormatterProp;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.safety.Whitelist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoticiaService {
    private final String URL_PATH = "https://www.abc.com.py";

    /**
     * Función que a partir del query y el flag f recibido como parametro obtiene las noticias
     * en formato HTML para seguidamente parsearlas en formato JSON y retornarlas al controlador.
     *
     * En caso de error (e.g: Lista Vacia, query Invalido) la funcion retorna un mensaje genérico
     * dependiendo de la naturaleza del error/excepción.
     *
     * @param  query  parametro de busqueda recibido del controlador por parte del usuario
     * @param  f parametro opcional que en el caso de que se realice una consulta con el valor true
     *           retorna en el JSON de la respuesta HTTP el atributo extra contenido_foto que contiene
     *           la foto en base64 y el content_type_foto
     * @return      Objeto del tipo ResponseEntity de tipo genérico
     * **/
    public ResponseEntity<?> obtenerNoticias(String query, Boolean f) throws IOException {

        //sanitizo la entrada de datos para prevenir caracteres invalidos dentro del QueryParam
        //query = Jsoup.clean(query, Safelist.basic());

        if(query.trim().isEmpty())
            return new ResponseEntity<>(ApiError.BAD_REQUEST, HttpStatus.BAD_REQUEST);

        //Se obtiene el HTML
        Document doc = Jsoup.connect(URL_PATH +"/buscar/"+ URLEncoder.encode(query, StandardCharsets.UTF_8)).get();

        //Se obtiene el elemento con id fusion-metadata, el cual contiene las noticias
        Element noticias = doc.getElementById("fusion-metadata");
        if(noticias == null)
            return new ResponseEntity<>(ApiError.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        String noticiasHtml = noticias.data();
        int inicioJson = noticiasHtml.indexOf("globalContent=");

        noticiasHtml = noticiasHtml.substring(inicioJson);

        //obtengo el indice final del inicio del json sin los elementos HTML
        inicioJson = noticiasHtml.indexOf("{");

        // obtengo el index del final del json
        int finJson = noticiasHtml.indexOf(";");

        // finalmente se obtiene el json en formato string de todas las news
        noticiasHtml = noticiasHtml.substring(inicioJson,finJson);

        NoticiaAbcAux noticiaAbcAux = formatNoticias(noticiasHtml);

        if (noticiaAbcAux.getData().isEmpty()){
            return new ResponseEntity<>(new ApiError("g267",
                    "No se encuentran noticias para el texto: " + query.trim()),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(validarLista(noticiaAbcAux, f), HttpStatus.OK);
    }

    /**
     * Función que recibe como parametro la lista de noticias en formato JSON extraidas
     * de la pagina de ABC para mapearlas con la clase auxiliar NoticiaAbcAux
     *
     *
     * @param  noticias parametro de tipo String que contiene las noticias en formato JSON extraidas de
     *                  la pagina web de ABC.
     *
     * @return      Objeto del tipo NoticiaAbcAux que sirve como auxiliar para mapear la lista de noticias
     *              del tipo NoticiaAbc contenidas en el parametro noticias.
     * **/
    public NoticiaAbcAux formatNoticias(String noticias) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        NoticiaAbcAux noticiaAbcAux = objectMapper.readValue(noticias,
                NoticiaAbcAux.class);

        return noticiaAbcAux;
    }

    /**
     * Función que recibe como parametro un objeto del tipo NoticiaAbcAux, con el fin de iterar sobre la lista
     * de NoticiasAbc contenidas dentro del mismo y mapearlas con la clase Noticia de la RESTFul API a utilizarse
     * para la respuesta del endpoint
     *
     *
     * @param  data parametro de tipo String que contiene las noticias en formato JSON extraidas de
     *              la pagina web de ABC.
     * @param  flag parametro de tipo boolean que permite identificar si se debe incluir la foto en base64
     *              con su Content-Type.
     * @return      Un objeto de tipo List que contiene todos los objetos de tipo Noticia instanciados a partir
     * de lo extraido de la pagina web de ABC.
     * **/
    public List<Noticia> validarLista(NoticiaAbcAux data, Boolean flag){
        List<Noticia> listaNoticias = new ArrayList<>();

        for(NoticiaAbc nAbc : data.getData()){

            Noticia noticia = new Noticia();
            List<String> urlNoticias = nAbc.getWebsiteUrls();

            if(urlNoticias.isEmpty())
                noticia.setEnlace("No hay ninguna URL disponible");
            else
                noticia.setEnlace(URL_PATH + urlNoticias.get(0));

            noticia.setTitulo(nAbc.getHeadlines().getBasic());
            noticia.setResumen(nAbc.getSubheadlines().getBasic());
            noticia.setFecha(validarFecha(nAbc.getPublishDate()));
            if (nAbc.getPromoItems() != null) {
                String fotoUrl = nAbc.getPromoItems().getBasic().getUrl();
                noticia.setEnlaceFoto(fotoUrl);
                if (flag) {
                    if (!fotoUrl.trim().isEmpty()) {
                        noticia.setContenidoFoto(getBase64Image(fotoUrl));
                        noticia.setContentTypeFoto("image/jpeg;base64");
                    }

                }
            }
            listaNoticias.add(noticia);

        }
        return listaNoticias;
    }


    /**
     * Función que recibe como parametro la url de la imagen a ser encodeada en base64 y retorna
     * el String correspondiente al encoding.
     *
     *
     * @param  fotoUrl dirección URL donde se encuentra alojada la foto de la noticia.
     * @return      Un objeto de tipo String resultante del encoding de la foto en base64.
     * **/
    public String getBase64Image(String fotoUrl){

        try {
            URL url = new URL(fotoUrl);
            BufferedInputStream bis;
            bis = new BufferedInputStream(url.openConnection().getInputStream());
            return Base64.encodeBase64String(bis.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Función que recibe como parametro la fecha en formato String extraida con el formato
     * utilizado por ABC, la parsea a un objeto de tipo Date para luego formatear la fecha
     * resultante en el formato ISO-8601 utilizando la clase auxiliar DateFormatterProp.
     *
     * @param  fecha dirección URL donde se encuentra alojada la foto de la noticia.
     * @return      Un objeto de tipo String resultante del formateo de la fecha en formato ISO-8601.
     * **/
    public String validarFecha(String fecha){

        Date fechaSinForm = DateFormatterProp.stringToDate(fecha);
        String fechaForm = DateFormatterProp.getDateISO8601(fechaSinForm);

        return fechaForm;
    }
}
