package com.medilab.notesPatients.model.repositorys;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.medilab.notesPatients.model.Note;

@Repository
public interface NoteRepository extends MongoRepository<Note, ObjectId>{
    public List<Note> findByPatId(Integer patId);
    
    public void deleteByPatId(Integer patId);
    
    public long countByPatId(Integer patId);
}
