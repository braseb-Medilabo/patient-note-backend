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

import jakarta.validation.Valid;

@RestController
public class NoteController {
    private NoteRepository noteRepository;
    
    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }
    
    @GetMapping("/patient/note/{patientId}")
    public ResponseEntity<List<Note>> getListOfNotePatient(@PathVariable Integer patientId){
        
        return ResponseEntity.status(HttpStatus.OK)
                            .body(noteRepository.findByPatId(patientId));
    }
    
    @PostMapping("/patient/note")
    public ResponseEntity<Note> addNotePatient(@Valid @RequestBody Note note){
        return new ResponseEntity<Note>(noteRepository.insert(note), HttpStatus.CREATED);
    }
    
    @DeleteMapping("/patient/note/{patientId}")
    public ResponseEntity<?> removeNotesPatient(@PathVariable Integer patientId){
        long nbrNotes = noteRepository.countByPatId(patientId);
        try {
            noteRepository.deleteByPatId(patientId);
            return ResponseEntity.ok(Map.of("Number of deleted notes", nbrNotes));
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
