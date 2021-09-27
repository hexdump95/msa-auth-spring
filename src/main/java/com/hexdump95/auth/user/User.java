package com.hexdump95.auth.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private UUID userId;
    @Indexed(unique = true)
    private String username;
    private String password;
    @Indexed(unique = true)
    private String email;
    private List<String> roles = new ArrayList<>();
    private LocalDate createdAt;
}
