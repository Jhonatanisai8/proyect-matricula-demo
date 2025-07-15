package com.isai.demowebregistrationsystem.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface AlmacenServicio {

    void iniciarArchivosDeAlmacenamiento();

    String almacenarArchivo(MultipartFile archivo);

    Path cargarArchivo(String nombreArchivo);

    Resource cargarArchivoComoRecurso(String nombreDeArchivo);

    void eliminarArchivo(String nombreDeArchivo);

}
