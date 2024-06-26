package com.example.quran.controller;

import com.example.quran.dto.ChangePassRequest;
import com.example.quran.dto.ChangePasswordRequest;
import com.example.quran.model.Users;
import com.example.quran.repository.UsersRepository;
import com.example.quran.response.*;
import com.example.quran.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UsersRepository usersRepository;

//    @GetMapping("/user")
//    public Optional<Users> getUserById(Long id){
//        return userService.getUserById(id);
//    }

//    @GetMapping("/user")
//    public ResponseEntity<?> getUserByToken(@RequestHeader("Authorization") String token) {
//        Users user = userService.getUserByToken(token);
//        if (user != null) {
//            return ResponseEntity.ok(user);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
//        }
//    }

//    @GetMapping("/user")
////    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN')")
//    public ResponseEntity<Users> getUserById(@RequestHeader(value = "userId") Long id) {
//        Optional<Users> userData = userService.getUserById(id);
//        if (userData.isPresent()) {
//            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/guru")
    public ResponseEntity<?> getTeacherUsers() {
        if (userService.performMaintenance()) {
            // Jika aplikasi sedang dalam mode maintenance, kembalikan respons Service Unavailable (kode status 503)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new MessageResponse(true, "Aplikasi sedang dalam maintenance. Silakan coba lagi nanti."));
        } else {
            // Jika tidak dalam mode maintenance, kembalikan data guru
            return ResponseEntity.ok(userService.getTeacherRole());
        }
    }

    @GetMapping("/guru/{userId}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long userId) {
        DetailRoleResponse teacherDetail = userService.getTeacherRoleDetail(userId);
        if (teacherDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(teacherDetail);
    }

    @GetMapping("/user/{userId}")
//    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable(value = "userId") Long id) {
        Users userData = userService.getUserById(id);
        if (userData != null) {
            return new ResponseEntity<>(userData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/getEmailUser")
    public Users getUserEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping("/{id}/uploadPhoto")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam MultipartFile file) throws Exception {
        userService.uploadPhoto(id, file);
        return ResponseEntity.ok(new MessageResponse(false, "Berhasil"));
    }

    @PutMapping("/{id}/changePhoto")
    public ResponseEntity<?> changePhoto(@PathVariable Long id, @RequestParam MultipartFile file) throws Exception {
        userService.changePhoto(id, file);
        return ResponseEntity.ok(new MessageResponse(false, "Photo Changed Successfully"));
    }

//    @PutMapping("/{userId}/password")
//    public ResponseEntity<?> changePassword(@PathVariable Long userId,
//                                                 @RequestBody @Validated ChangePasswordRequest request) {
//        try {
//            userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
//            return ResponseEntity.ok(new MessageResponse(false, "Password Changes Succesfully"));
//        } catch (InvalidPasswordException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<MessageResponse> changePassword(@PathVariable Long userId,
                                                          @RequestBody ChangePasswordRequest request) {
        try {
            return userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(true, e.getMessage()));
        }
    }
}
