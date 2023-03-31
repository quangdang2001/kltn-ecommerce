package com.example.kltn.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


@Document(collection = "product_manufacturer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {
    @Id
    private String id;
    @NotNull
    private String name;
    @Indexed(unique = true)
    private String slug;
    private String desc;
    private String image;
}
