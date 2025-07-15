package com.isai.demowebregistrationsystem.controllers.imgs;

import com.isai.demowebregistrationsystem.services.impl.AlmacenArchivoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/assets")
@RequiredArgsConstructor
public class ImagenPersonaController {

    private final AlmacenArchivoImpl almacenArchivo;

    @RequestMapping(path = "/{filename:.+}", method = RequestMethod.GET)
    public Resource obtenerImagen(@PathVariable String filename) {
        return almacenArchivo.cargarArchivoComoRecurso(filename);
    }
}
