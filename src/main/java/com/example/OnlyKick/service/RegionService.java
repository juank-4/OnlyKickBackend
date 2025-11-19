package com.example.OnlyKick.service;

import com.example.OnlyKick.model.Comuna;
import com.example.OnlyKick.model.Region;
import com.example.OnlyKick.repository.ComunaRepository;
import com.example.OnlyKick.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@SuppressWarnings("null")
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    @Lazy
    private ComunaService comunaService;

    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    public Region findById(Integer id) {
        return regionRepository.findById(id).orElse(null);
    }

    public Region save(Region region) {
        return regionRepository.save(region);
    }

    //Eliminacion por cascada para borrar una region, borrando sus comunas asociadas
    public void deleteById(Integer id) {
        //Busca todas las comunas de esta region
        List<Comuna> comunas = comunaRepository.findByRegionIdRegion(id);
        
        //Llama al mEtodo de borrado manual de ComunaService por cada una
        if (comunas != null && !comunas.isEmpty()) {
            for (Comuna comuna : comunas) {
                comunaService.deleteById(comuna.getIdComuna());
            }
        }
        //Borrar la Region
        regionRepository.deleteById(id);
    }
}