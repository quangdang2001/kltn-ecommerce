package com.example.kltn.dto;

import lombok.*;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class ProductSearchReq {
    @Nullable
    private String keyword;
    @Nullable
    private String cate;
    @Nullable
    private String manu;
    private String orderBy = "des"; // des, asc
    private String sortBy = "bestPrice";

    private int page = 0;
    private int size = 10;
}
