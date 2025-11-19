package com.example.OnlyKick.service;

import com.example.OnlyKick.model.Comuna;
import com.example.OnlyKick.model.Direcciones;
import com.example.OnlyKick.repository.ComunaRepository;
import com.example.OnlyKick.repository.DireccionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@SuppressWarnings("null")
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private DireccionesRepository direccionesRepository;

    @Autowired
    @Lazy //@Lazy es para romper la dependencia
    private DireccionesService direccionesService;

    public List<Comuna> findAll() {
        return comunaRepository.findAll();
    }

    public Comuna findById(Integer id) {
        return comunaRepository.findById(id).orElse(null);
    }

    public List<Comuna> findByRegionId(Integer idRegion) {
        return comunaRepository.findByRegionIdRegion(idRegion);
    }

    public Comuna save(Comuna comuna) {
        return comunaRepository.save(comuna);
    }

    //Eliminacion por cascada para borrar una comuna, borrando sus direcciones asociadas
    public void deleteById(Integer id) {
        //Buscar todas las direcciones de esta comuna
        List<Direcciones> direcciones = direccionesRepository.findByComunaIdComuna(id);
        //Llamar al borrado manual de DireccionesService por cada una
        if (direcciones != null && !direcciones.isEmpty()) {
            for (Direcciones direccion : direcciones) {
                direccionesService.deleteById(direccion.getIdDireccion());
            }
        }
        //Borra la Comuna
        comunaRepository.deleteById(id);
    }
}