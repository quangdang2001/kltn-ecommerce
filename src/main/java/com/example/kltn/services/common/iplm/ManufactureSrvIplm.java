package com.example.kltn.services.common.iplm;

import com.example.kltn.models.Manufacturer;
import com.example.kltn.repos.ManufactureRepo;
import com.example.kltn.services.common.ManufactureSrv;
import com.example.kltn.utils.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ManufactureSrvIplm implements ManufactureSrv {
    private final ManufactureRepo manufacturerRepo;
    @Override
    public Manufacturer findById(String id) {
        var payment = manufacturerRepo.findById(id);
        return payment.orElse(null);
    }

    @Override
    public List<Manufacturer> findAll() {
        return manufacturerRepo.findAll();
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        manufacturer.setSlug(SlugGenerator.slugify(manufacturer.getName()));
        return manufacturerRepo.save(manufacturer);
    }

    @Override
    public Manufacturer updateManufacturer(Manufacturer manufacturer) {
        Manufacturer manufacturerUpdate = findById(manufacturer.getId());
        if (manufacturerUpdate!=null) {
            manufacturerUpdate.setName(manufacturer.getName());
            return manufacturerUpdate;
        }
        else return null;
    }

    @Override
    public boolean deleteManufacturer(String manufacturerId) {
        boolean check = manufacturerRepo.existsById(manufacturerId);
        if (check){
            manufacturerRepo.deleteById(manufacturerId);
            return true;
        }else {
            return false;
        }
    }
}
