package org.example.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private final  UUID id = UUID.randomUUID();
    private String name;
    private Integer price;
    private String path;
    private UUID categoryId;
}
