package com.example.quran.controller;

import com.example.quran.dto.MateriDTO;
import com.example.quran.dto.SubMateriDTO;
import com.example.quran.model.Materi;
import com.example.quran.model.Submateri;
import com.example.quran.response.MateriResponse;
import com.example.quran.response.MessageResponse;
import com.example.quran.response.SubMateriResponse;
import com.example.quran.services.MateriService;
import com.example.quran.services.SubmateriService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MateriController {
    @Autowired
    private MateriService materiService;
    @Autowired
    private SubmateriService submateriService;

    @GetMapping("/materi")
    public ResponseEntity<MateriResponse> getAllMateri(){
        return ResponseEntity.ok(materiService.getAllMateri());
    }

    @GetMapping("/materi/{materiId}")
    public ResponseEntity<?> getMateriById(@PathVariable Long materiId){
        SubMateriResponse subMateriByMateriId = materiService.getSubMateriByMateriId(materiId);
        return ResponseEntity.ok(subMateriByMateriId);
    }

    @GetMapping("/submateri/{subMateriId}")
    public ResponseEntity<Submateri> getSubMateriById(@PathVariable Long subMateriId){
        return ResponseEntity.ok(submateriService.getSubmateriByMateriId(subMateriId));
    }

    @PostMapping("/category")
    public ResponseEntity<?> createdMateri(@RequestBody @Valid MateriDTO materiDTO){
        Materi createdMateri = materiService.createMateri(materiDTO);
        if(createdMateri != null) {
            return ResponseEntity.ok(new MessageResponse(false, "Successfully Created Materi!"));
        }else {
            return ResponseEntity.badRequest().body(new MessageResponse(true, "Must be filled!"));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Submateri> createSubMateri(@RequestBody @Valid SubMateriDTO subMateriDTO){
        Submateri createdSubmateri = submateriService.createSubMateri(subMateriDTO);
        return new ResponseEntity<>(createdSubmateri, HttpStatus.CREATED);
    }
}
