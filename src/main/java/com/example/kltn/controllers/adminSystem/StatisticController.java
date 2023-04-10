package com.example.kltn.controllers.adminSystem;

import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.Shop;
import com.example.kltn.services.statistic.StatisticSrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adminSys")
@RequiredArgsConstructor
@Slf4j
public class StatisticController {
    private final StatisticSrv statisticSrv;

    @GetMapping("/statistic")
    ResponseEntity<?> addShop(@RequestParam(defaultValue = "") String shopId,
                              @RequestParam String typeSatistic,
                              @RequestParam String typeId,
                              @RequestParam int day,
                              @RequestParam int month,
                              @RequestParam int year){
        var statistic = statisticSrv.countItemSold(day,month,year,shopId, typeSatistic,
                typeId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO(true, "Success", statistic)
        );
    }
}
