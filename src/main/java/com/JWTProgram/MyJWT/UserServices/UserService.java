package com.JWTProgram.MyJWT.UserServices;

import com.JWTProgram.MyJWT.Model.Users;
import com.JWTProgram.MyJWT.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager Manager;

    @Autowired
    private JWTServices jwtServices;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public Users register(Users user) {
        System.out.println("Saving user: " + user.getUsername());

        // Check if the username already exists
        if (userRepo.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("User already exists with username: " + user.getUsername());
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public List<Users> getAllStudents() {
        return userRepo.findAll();
    }




    //Upadte

    @Transactional
    public Users updateUser(int id, Users updatedUser) {
        Optional<Users> existingUser = userRepo.findById(id);
        if (existingUser.isPresent()) {
            Users user = existingUser.get();
            user.setUsername(updatedUser.getUsername());

            // Only update the password if a new one is provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(encoder.encode(updatedUser.getPassword()));
            }

            return userRepo.save(user);
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }


    //delete

    @Transactional
    public void deleteUser(int id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
        }else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }



    //Login Programm
    public String verify(Users user) {
        Authentication authentication =Manager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()){
        return jwtServices.createToken(user.getUsername());

        }
        else{
            return "Login Failed";
        }



    }
}
