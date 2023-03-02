package com.example.kltn.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupSpecification {
    private String groupName;
    private String groupKey;
    private List<Specification> groupItems;

    @Getter
    @Setter
    public static class Specification {
        private String name;
        private String key;
        private String value;
    }
}
