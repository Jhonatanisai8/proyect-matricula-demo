package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.exceptions.WarehouseException;
import com.isai.demowebregistrationsystem.services.AlmacenServicio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class AlmacenArchivoImpl
        implements AlmacenServicio {

    @Value("${storage.location}")
    private String ubicacionAlmacenamiento;

    @Override
    public void iniciarArchivosDeAlmacenamiento() {
        try {
            Files.createDirectories(Paths.get(ubicacionAlmacenamiento));
        } catch (IOException e) {
            new WarehouseException("Error al crear el archivo de almacenamiento");
        }
    }

    @Override
    public String almacenarArchivo(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            throw new WarehouseException("No se puede almacenar el archivo vacio");
        }
        try {
            String nombreArchivoOriginal = archivo.getOriginalFilename();
            String nombreUnicoArchivo = UUID.randomUUID().toString() + "-" + nombreArchivoOriginal;
            Path ruta = Paths.get(ubicacionAlmacenamiento);
            if (!Files.exists(ruta)) {
                Files.createDirectories(ruta);
            }
            Path destinoArchivo = ruta.resolve(Paths.get(nombreUnicoArchivo))
                    .normalize().toAbsolutePath();
            try (InputStream inputStream = archivo.getInputStream()) {
                Files.copy(inputStream, destinoArchivo, StandardCopyOption.REPLACE_EXISTING);
            }
            return nombreUnicoArchivo;
        } catch (IOException e) {
            throw new WarehouseException("Error al crear el archivo");
        }
    }

    @Override
    public Path cargarArchivo(String nombreArchivo) {
        return Paths.get(ubicacionAlmacenamiento)
                .resolve(nombreArchivo);
    }

    @Override
    public Resource cargarArchivoComoRecurso(String nombreDeArchivo) {
        Path rutaArchivo = cargarArchivo(nombreDeArchivo);
        try {
            Resource recurso = new UrlResource(rutaArchivo.toUri());
            if (recurso.exists() || recurso.isReadable()) {
                return recurso;
            } else {
                throw new WarehouseException("El recurso no existe");
            }
        } catch (MalformedURLException e) {
            throw new WarehouseException("Error al crear el archivo");
        }

    }

    @Override
    public void eliminarArchivo(String nombreDeArchivo) {
        try {

            Path rutaArchivo = cargarArchivo(nombreDeArchivo);
            FileSystemUtils.deleteRecursively(rutaArchivo.toFile());
        } catch (Exception e) {
            throw new WarehouseException("Error al eliminar el archivo");
        }
    }
}
