package com.medilab.notesPatients.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.medilab.notesPatients.model.Note;
import com.medilab.notesPatients.model.repositorys.NoteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.validation.Valid;

@RestController
public class NoteController {
    private NoteRepository noteRepository;
    
    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }
    
    @GetMapping("/patient/note/{patientId}")
    @Operation(
            summary = "Get all notes for a patient",
            description = "Retrieves all medical notes associated with a patient."
        )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "List of notes associated with the patient",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(implementation = Note.class)
                )
            )
        )
    })
    public ResponseEntity<List<Note>> getListOfNotePatient(
            @Parameter(
                    description = "Unique identifier of the patient",
                    example = "1"
            )
            @PathVariable Integer patientId) {
        
        return ResponseEntity.status(HttpStatus.OK)
                            .body(noteRepository.findByPatId(patientId));
    }
    
    @PostMapping("/patient/note")
    @Operation(
            summary = "Add a note to a patient",
            description = "Creates a new medical note associated with a patient."
        )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Note successfully created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Note.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid note data",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        type = "object",
                        example = """
                            {
                              "message": "validation failed",
                              "errors": {keys's errors}
                            }
                            """
                    )
                )
        )
    })
    public ResponseEntity<Note> addNotePatient(@Valid @RequestBody Note note) {
        return new ResponseEntity<Note>(noteRepository.insert(note), HttpStatus.CREATED);
    }
    
    @DeleteMapping("/patient/note/{patientId}")
    @Operation(
            summary = "Delete all notes of a patient",
            description = "Deletes all medical notes associated with a patient."
        )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Notes successfully deleted",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    example = "{\"deletedNotes\": 3}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No notes found for the patient"
        )
    })
    public ResponseEntity<?> removeNotesPatient(@PathVariable Integer patientId){
        long nbrNotes = noteRepository.countByPatId(patientId);
        if (nbrNotes == 0) {
            return ResponseEntity.notFound().build();
        }
        noteRepository.deleteByPatId(patientId);
        return ResponseEntity.ok(Map.of("deletedNotes", nbrNotes));
       
    }
}
