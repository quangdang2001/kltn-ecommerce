package com.example.kltn.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Statistic {
    Object countUntilNow;
    Object totalUntilNow;
    Object countDay;
    Object totalDay;
    Object countMonth;
    Object totalMonth;
    Object countYear;
    Object totalYear;
}
