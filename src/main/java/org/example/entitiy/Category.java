package org.example.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private final UUID id = UUID.randomUUID();
    private String name;
    private UUID parentId;
}
