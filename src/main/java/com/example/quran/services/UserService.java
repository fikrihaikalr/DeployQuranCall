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

    public List<Users> getAllUser(){
        log.info("Success");
        return usersRepository.findAll();
    }

    public Users getUserById(Long id){
        log.info("Get Data User By Id Succses!");
        return usersRepository.findById(id).orElse(null);

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

    public Users getUserByEmail(String email){
        return usersRepository.findByEmail1(email);
    }

    public void uploadPhoto(Long id, MultipartFile file) throws Exception {
        Users users = usersRepository.findById(id)
                .orElseThrow();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            String uploadDir = "user-photos/" + id;
            Path storagePath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(storagePath);
            Files.copy(file.getInputStream(), storagePath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            users.setPhotoPath(fileName);
            usersRepository.save(users);
        }catch (IOException e){
            throw new Exception("Could not store file " + fileName + ". Please try again!", e);
        }


    }
    public void changePhoto(Long id, MultipartFile file) throws Exception{
        uploadPhoto(id, file);
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

    public ResponseEntity<MessageResponse> changePassword(Long userId, String currentPassword, String newPassword) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse(true, "Current password is incorrect"));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(false, "Password changed successfully"));
    }

}
