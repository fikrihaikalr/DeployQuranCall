package com.example.quran.services;

import com.example.quran.data.RoleData;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;


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

//    @Transactional
//    public void changeUserPasswordByEmail(ChangePasswordRequest changePasswordRequest) throws Exception {
//        String email = changePasswordRequest.getEmail();
//        String oldPassword = changePasswordRequest.getOldPassword();
//        String newPassword = changePasswordRequest.getNewPassword();
//
//        // Cari pengguna berdasarkan email
//        Users user = usersRepository.findByEmail(email)
//                .orElseThrow(() -> new Exception("User with email " + email + " not found"));
//
//        // Verifikasi kecocokan password lama
//        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
//            throw new Exception("Old password is incorrect");
//        }
//
//        // Enkode password baru
//        String encodedPassword = passwordEncoder.encode(newPassword);
//
//        // Set password baru
//        user.setPassword(encodedPassword);
//
//        // Simpan perubahan
//        usersRepository.save(user);
//
//    }

    @Transactional
    public boolean changeUserPasswordByEmail(ChangePasswordRequest changePasswordRequest) {
        String email = changePasswordRequest.getEmail();
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        // Cari pengguna berdasarkan email
        Optional<Users> optionalUser = usersRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return false; // Pengguna tidak ditemukan
        }
        Users user = optionalUser.get();

        // Verifikasi kecocokan password lama
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false; // Password lama tidak cocok
        }

        // Enkode password baru
        String encodedPassword = passwordEncoder.encode(newPassword);

        // Set password baru
        user.setPassword(encodedPassword);

        // Simpan perubahan
        usersRepository.save(user);

        return true; // Perubahan password berhasil
    }

}
