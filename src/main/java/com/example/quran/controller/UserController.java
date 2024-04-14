package com.example.quran.controller;

import com.example.quran.dto.ChangePasswordRequest;
import com.example.quran.model.Users;
import com.example.quran.repository.UsersRepository;
import com.example.quran.response.DetailRoleResponse;
import com.example.quran.response.RoleResponse;
import com.example.quran.response.MessageResponse;
import com.example.quran.response.UserDetailsResponse;
import com.example.quran.services.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/guru")
    public ResponseEntity<?> getTeacherUsers(){
        return ResponseEntity.ok(userService.getTeacherRole());
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
        return ResponseEntity.ok("Photo Upload Succesfully");
    }

    @PutMapping("/{id}/changePhoto")
    public ResponseEntity<?> changePhoto(@PathVariable Long id, @RequestParam MultipartFile file) throws Exception {
        userService.changePhoto(id, file);
        return ResponseEntity.ok("Photo Changed Successfully");
    }

    @PostMapping("/change-password")
//    public ResponseEntity<> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        String currentPassword = changePasswordRequest.getOldPassword();
//        String newPassword = changePasswordRequest.getNewPassword();
//
//        userService.changePassword(username, currentPassword, newPassword);
//
//        return ResponseEntity.ok("Password changed successfully");
//    }
//    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) throws Exception{
//        UserDTO pDTO = new UserDTO();
//        pDTO.setEmail(changePasswordRequest.getEmail());
//        pDTO.setPassword(changePasswordRequest.getOldPassword());
//
//        int check = userService.changeUserPassword(pDTO, changePasswordRequest.getNewPassword());
//
//        if(check == 1){
//            return ResponseEntity.status(HttpStatus.OK).body("Berhasil");
//
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("gagal");
//    }
    public ResponseEntity<?> changeUserPasswordByEmail(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) throws Exception {
        userService.changeUserPasswordByEmail(changePasswordRequest);
        return ResponseEntity.ok(new MessageResponse(false, "Password changed successfully"));
    }

//    @PatchMapping("/change-password")
//    public ResponseEntity<?> changePassword(
//            @RequestBody ChangePasswordRequest request,
//            Principal connectedUser
//    ) {
//        userService.changePassword(request, connectedUser);
//        return ResponseEntity.ok().build();
//    }

}
