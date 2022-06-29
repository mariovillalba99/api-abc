package com.roshka.apiabc.controller;

import com.roshka.apiabc.error.ApiError;
import com.roshka.apiabc.model.Noticia;
import com.roshka.apiabc.model.NoticiaService;
import org.jsoup.HttpStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping(path = "consulta")
public class NoticiaController {

    private final NoticiaService noticiaService;

    public NoticiaController(NoticiaService noticiaService) {
        this.noticiaService = noticiaService;
    }

    @GetMapping
    public ResponseEntity<?> getNoticias(@RequestParam("q") String query,
                                         @RequestParam(name="f", required = false)
                                         boolean f) throws IOException {

        return noticiaService.obtenerNoticias(query, f);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {

        return new ResponseEntity<>(ApiError.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<?> handleMalformedUrl(MalformedURLException ex) {
        return new ResponseEntity<>(ApiError.INTERNAL_SERVER_ERROR
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(HttpStatusException.class)
    public ResponseEntity<?> handleHttpStatus(HttpStatusException ex) {
        return new ResponseEntity<>(ApiError.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleHttpStatus(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(ApiError.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }
}
