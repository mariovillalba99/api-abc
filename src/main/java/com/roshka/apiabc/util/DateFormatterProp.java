package com.roshka.apiabc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatterProp {
    private static final String ISO_8601_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
    private static final String INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String INPUT_FORMAT_AUX = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static String getDateISO8601(Date fecha) {
        SimpleDateFormat sDFormat = new SimpleDateFormat(ISO_8601_FORMAT_DATE);
        return sDFormat.format(fecha);
    }

    /**
     * Función que recibe como parametro fecha en formato String, verifica si tiene formato valido
     * segun los INPUT_FORMAT utilizados en ABC definidos como constantes y retorna la fecha formateada
     * en ISO-8601.
     *
     * Para este caso, se observó que en las noticias de ABC los DateTime se devuelven en distintos formatos (INPUT_FORMAT,
     * INPUT_FORMAT_AUX) dependiendo del query utilizado para la busqueda, por ello al momento del parsing se utilizan ambos
     * inputs para evitar excepciones por parsing incorrecto de la cadena.
     *
     *
     * @param  fecha fecha extraida de la noticia de ABC como String
     * @return      Un objeto de tipo Date que contiene la fecha de la noticia formateada.
     * **/
    public static Date stringToDate(String fecha) {
        Date validDateFormat = checkValidDate(fecha, INPUT_FORMAT);
        if(validDateFormat != null){
            return validDateFormat;
        }else {
            validDateFormat = checkValidDate(fecha, INPUT_FORMAT_AUX);
            return validDateFormat ;
        }
    }

    /**
     * Función que recibe como parametros la fecha y el inputFormat de la fecha a ser testeado y devuelve la fecha formateada
     * de tipo Date o null en caso de que ocurra algun error de parsing con el inputFormat.
     *
     *
     *
     * @param  date fecha extraida de la noticia de ABC como String
     * @param  inputFormat fecha extraida de la noticia de ABC como String
     *
     * @return      Un objeto de tipo Date que contiene la fecha de la noticia formateada o null si el formato de parsing
     *              no es el correcto y se lanzo una excepcion de Parsing.
     * **/
    public static Date checkValidDate(String date, String inputFormat) {
        SimpleDateFormat sDFormat = new SimpleDateFormat(inputFormat);
        try {
           return sDFormat.parse(date);

        } catch (ParseException e) {
            return null;
        }
    }
}
