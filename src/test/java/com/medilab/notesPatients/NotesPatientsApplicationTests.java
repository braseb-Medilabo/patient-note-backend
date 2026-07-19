package com.medilab.notesPatients;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilab.notesPatients.controllers.NoteController;
import com.medilab.notesPatients.model.Note;
import com.medilab.notesPatients.model.repositorys.NoteRepository;

@WebMvcTest(controllers = NoteController.class)
class NotesPatientsApplicationTests {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @MockitoBean
    NoteRepository noteRepository;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnNoteListForPatient() throws Exception {
        Note note = new Note(null, 1, "Dupont", "Premi\u00e8re consultation");

        List<Note> notes = Arrays.asList(note);
        when(noteRepository.findByPatId(anyInt())).thenReturn(notes);

        mockMvc.perform(get("/patient/note/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patId").value(1))
                .andExpect(jsonPath("$[0].note").value("Premi\u00e8re consultation"));
    }

    @Test
    void shouldReturnEmptyListWhenPatientHasNoNotes() throws Exception {
        when(noteRepository.findByPatId(anyInt())).thenReturn(Arrays.asList());

        mockMvc.perform(get("/patient/note/99"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldAddNoteForPatient() throws Exception {
        Note note = new Note(null, 1, "Dupont", "Nouvelle note");

        when(noteRepository.insert(any(Note.class))).thenReturn(note);

        mockMvc.perform(post("/patient/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                        .andDo(print())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.patId").value(1))
                        .andExpect(jsonPath("$.note").value("Nouvelle note"));
    }

    @Test
    void shouldReturn400WhenAddingNoteWithBlankNote() throws Exception {
        Note invalidNote = new Note(null, 1, "Dupont", ""); // note vide -> viole @NotBlank

        mockMvc.perform(post("/patient/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNote)))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAddingNoteWithoutPatId() throws Exception {
        Note invalidNote = new Note(null, null, "Dupont", "Une note valide"); // patId null -> viole @NotNull

        mockMvc.perform(post("/patient/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNote)))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteNotesForPatientAndReturnCount() throws Exception {
        when(noteRepository.countByPatId(anyInt())).thenReturn(3L);
        doNothing().when(noteRepository).deleteByPatId(anyInt());

        mockMvc.perform(delete("/patient/note/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['deletedNotes']").value(3));

        verify(noteRepository, times(1)).deleteByPatId(1);
    }

    @Test
    void shouldReturn404WhenDeleteFails() throws Exception {
        when(noteRepository.countByPatId(anyInt())).thenReturn(0L);
        doThrow(new RuntimeException("erreur suppression"))
                .when(noteRepository).deleteByPatId(anyInt());

        mockMvc.perform(delete("/patient/note/99"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}