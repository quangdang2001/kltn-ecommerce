package com.example.kltn.services.common;

import com.example.kltn.models.Manufacturer;

import java.util.List;

public interface ManufactureSrv {
    Manufacturer findById(String manufacturerId);
    List<Manufacturer> findAll();
    Manufacturer save(Manufacturer manufacturer);
    Manufacturer updateManufacturer(Manufacturer manufacturer);
    boolean deleteManufacturer(String id);
}
