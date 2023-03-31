package com.example.kltn.controllers.customer;

import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.Manufacturer;
import com.example.kltn.services.common.ManufactureSrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ManufactureController {
    private final ManufactureSrv manufacturerService;
    @GetMapping("/manufacturer")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",manufacturerService.findAll()));
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public ResponseEntity<?> findById(@PathVariable String manufacturerId){
        Manufacturer manufacturer = manufacturerService.findById(manufacturerId);
        if (manufacturer !=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",manufacturer));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Manufacturer ID not exits",null));
    }

    @PostMapping("/admin/manufacturer")
    public ResponseEntity<?> saveManufacturer(@RequestBody Manufacturer manufacturer){
        Manufacturer manufacturerSave =  manufacturerService.save(manufacturer);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",manufacturerSave));
    }

    @PutMapping("/admin/manufacturer")
    public ResponseEntity<?> updateManufacturer(@RequestBody Manufacturer manufacturer){
        Manufacturer manufacturerUpdate = manufacturerService.updateManufacturer(manufacturer);
        if (manufacturerUpdate!= null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",manufacturerUpdate));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Manufacturer ID not exits",null));
    }

    @DeleteMapping("/admin/manufacturer/{id}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable String id){
        boolean check = manufacturerService.deleteManufacturer(id);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false,"Manufacturer ID not exits",null));
    }
}
