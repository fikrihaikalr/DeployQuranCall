package com.example.quran.services;

import com.example.quran.data.RoleData;
import com.example.quran.dto.ChangePassRequest;
import com.example.quran.dto.ChangePasswordRequest;
import com.example.quran.model.ERole;
import com.example.quran.model.Role;
import com.example.quran.model.Users;
import com.example.quran.repository.RoleRepository;
import com.example.quran.repository.UsersRepository;
import com.example.quran.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ValidationService validationService;

    @Autowired
    private AuthenticationManager authenticationManager;


    public Users getUserById(Long id){
        log.info("Get Data User By Id Succses!");
        return usersRepository.findById(id).orElse(null);
//        Users users = usersRepository.findById(id).orElse(null);
//        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
//        userDetailsResponse.setId(users.getId().toString());
//        userDetailsResponse.setUsername(users.getUsername());
//        userDetailsResponse.setEmail(users.getEmail());
//        userDetailsResponse.setPassword(users.getPassword());
//        userDetailsResponse.setPhotoPath(users.getPhotoPath());
//        userDetailsResponse.setRoles(convertSetToList(users.getRoles()));

//        return userDetailsResponse;

    }

    public DetailRoleResponse getTeacherRoleDetail(Long userId){
        Users teacher = usersRepository.findById(userId).orElse(null);
        if (teacher == null) {
            return null;
        }
        DetailRoleResponse teacherDetail = new DetailRoleResponse();
        MessageResponse messageResponse = new MessageResponse(false, "Success");
        RoleData roleData = new RoleData();
        roleData.setId(teacher.getId().toString());
        roleData.setUsername(teacher.getUsername());
        roleData.setEmail(teacher.getEmail());
        roleData.setPhotoPath(teacher.getPhotoPath());
        roleData.setNumberPhone(teacher.getNumberPhone());
        roleData.setStatus(teacher.getStatus());
        roleData.setRoles(convertSetToList(teacher.getRoles()));
        teacherDetail.setMessageResponse(messageResponse);
        teacherDetail.setData(roleData);
        return teacherDetail;
    }

    public RoleResponse getTeacherRole() {
        ERole roleTeacher = ERole.ROLE_TEACHER;
        Optional<Role> roleOptional = roleRepository.findByName(roleTeacher);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            List<Users> usersWithRole = usersRepository.findByRolesName(role.getName());
            List<RoleData> teacherList = new ArrayList<>();

            if (usersWithRole.isEmpty()) {
                RoleResponse emptyResponse = new RoleResponse();
                emptyResponse.setMessageResponse(new MessageResponse(true, "No teachers found"));
                return emptyResponse;
            }

            RoleResponse detailRoleResponse = new RoleResponse();
            MessageResponse messageResponse = new MessageResponse(false, "Success");

            for (Users user : usersWithRole) {
                RoleData roleData = new RoleData();
                roleData.setId(user.getId().toString());
                roleData.setUsername(user.getUsername());
                roleData.setEmail(user.getEmail());
                roleData.setPhotoPath(user.getPhotoPath());
                roleData.setNumberPhone(user.getNumberPhone());
                roleData.setStatus(user.getStatus());
                roleData.setRoles(convertSetToList(user.getRoles()));
                teacherList.add(roleData);
            }

            detailRoleResponse.setMessageResponse(messageResponse);
            detailRoleResponse.setData(teacherList);
            return detailRoleResponse;
        } else {
            // Lakukan penanganan jika role tidak ditemukan
            return null;
        }
    }


    private List<String> convertSetToList(Set<Role> rolesSet) {
        List<String> rolesList = new ArrayList<>();
        for (Role role : rolesSet) {
            rolesList.add(role.getName().name());
        }
        return rolesList;
    }

//    public DetailRoleResponse getTeacherRole(Long userId){
//        Users teacher = usersRepository.findById(userId).orElse(null);
//        if (teacher == null) {
//            return null;
//        }
//        DetailRoleResponse teacherDetail = new DetailRoleResponse();
//        MessageResponse messageResponse = new MessageResponse(false, "Success");
//        RoleData roleData = new RoleData();
//        roleData.setId(teacher.getId().toString());
//        roleData.setUsername(teacher.getUsername());
//        roleData.setEmail(teacher.getEmail());
//        roleData.setPhotoPath(teacher.getPhotoPath());
//        roleData.setRoles(convertSetToList(teacher.getRoles()));
//        teacherDetail.setMessageResponse(messageResponse);
//        teacherDetail.setData(roleData);
//        return teacherDetail;
//    }



    public Users getUserByEmail(String email){
        return usersRepository.findByEmail1(email);
    }

    public void uploadPhoto(Long id, MultipartFile file) throws Exception {
        Users users = usersRepository.findById(id)
                .orElseThrow();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            String uploadDir = "user-photos/" + id;
            String filePath = uploadDir + "/" + fileName;
            Path storagePath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(storagePath);
            Files.copy(file.getInputStream(), storagePath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            users.setPhotoPath(filePath);
            usersRepository.save(users);
        }catch (IOException e){
            throw new Exception("Could not store file " + fileName + ". Please try again!", e);
        }


    }
    public void changePhoto(Long id, MultipartFile file) throws Exception{
        uploadPhoto(id, file);
    }


    public boolean checkIfValidOldPassword(Users user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Scheduled(cron = "0 0 20-21 * * *")
    public boolean performMaintenance() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
        int hour = now.getHour();

        // Periksa apakah waktu saat ini berada di rentang waktu maintenance (pukul 8 malam hingga 9 malam)
        if (hour >= 20 && hour < 21) {
            System.out.println("Aplikasi sedang dalam Maintenance");
            return true;
        }

        // Jika waktu saat ini bukan dalam rentang waktu maintenance
        return false;
    }

//    public void changePasswordUser(ChangePasswordRequest changePasswordDTO) {
//        validationService.validate(changePasswordDTO);
//        // Cari pengguna berdasarkan username
//        Users user = usersRepository.findByEmail1(changePasswordDTO.getEmail());
//
//        // Periksa apakah pengguna ditemukan
//        if (user == null) {
//            new MessageResponse(true, "User not found");
//        }
//
//        // Periksa apakah kata sandi lama sesuai
//        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
//            new MessageResponse(true, "Incorrect old password");
//        }
//
//        // Enkripsi kata sandi baru
//        String encodedNewPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
//
//        // Tetapkan kata sandi baru yang terenkripsi ke pengguna
//        user.setPassword(encodedNewPassword);
//
//        // Simpan perubahan ke dalam database
//        usersRepository.save(user);
//    }


//    public void changePassword(Users users, String currentPassword, String newPassword){
//        if(currentPassword == null || !passwordEncoder.matches(currentPassword, users.getPassword())){
//            throw new RuntimeException("Invalid");
//        }
//        users.setPassword(passwordEncoder.encode(newPassword));
//        usersRepository.save(users);
//    }

//    public void changePassword(Users users, String currentPassword, String newPassword) {
//        UsernamePasswordAuthenticationToken currentAuth = new UsernamePasswordAuthenticationToken(users.getEmail(), currentPassword);
//        authenticationManager.authenticate(currentAuth);
//
//        users.setPassword(passwordEncoder.encode(newPassword));
//        usersRepository.save(users);
//    }

//    public void changePassword(ChangePasswordRequest changePasswordRequest, Principal principal) {
//        // Dapatkan pengguna saat ini dari Principal
//        String currentUserEmail = principal.getName();
//        Users currentUser = usersRepository.findByEmail(currentUserEmail)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Validasi password lama
//        if (!passwordEncoder.matches(changePasswordRequest.getRawPassword(), currentUser.getPassword())) {
//            throw new BadCredentialsException("Incorrect old password");
//        }
//
//        // Validasi password baru dan konfirmasi password
//        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
//            throw new IllegalArgumentException("New password and confirm password do not match");
//        }
//
//        // Tetapkan password baru yang terenkripsi ke pengguna
//        String encodedNewPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
//        currentUser.setPassword(encodedNewPassword);
//
//        // Simpan perubahan ke dalam database
//        usersRepository.save(currentUser);
//    }

//    public ResponseEntity<?> changePassword(String userEmail, String oldPassword, String newPassword) {
//        Users loginUser = usersRepository.findByEmail1(userEmail);
//
//        boolean passwordMatches = passwordEncoder.matches(oldPassword, loginUser.getPassword());
//        if (passwordMatches) {
//            loginUser.setPassword(passwordEncoder.encode(newPassword));
//            usersRepository.save(loginUser);
//            return ResponseEntity.ok(new MessageResponse(false, "Change Password Success"));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(true, "Old password is incorrect."));
//        }
//    }

    public void changePassword(Long id, String oldPassword, String newPassword) throws InvalidPasswordException {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ExceptionUsername("a"));

        // Validate old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect.");
        }

        // Update password and save user
        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }


}
