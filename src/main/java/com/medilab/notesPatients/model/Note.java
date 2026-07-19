package com.medilab.notesPatients.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "note")
public class Note {
    
    @Id
    private ObjectId id;
    @NotNull
    private Integer patId;
    private String patient;
    @NotBlank(message = "note is required")
    private String note;
    
}
