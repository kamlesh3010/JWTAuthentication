package com.JWTProgram.MyJWT.Controller;

import com.JWTProgram.MyJWT.Model.Users;
import com.JWTProgram.MyJWT.UserServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        System.out.println("Received user: " + user.getUsername());
        return userService.register(user);
    }

    @GetMapping("/all")
    public List<Users> getStudents() {
        return userService.getAllStudents(); // Call the service method
    }

    @PutMapping("/update/{id}")
    public Users updateUser(@PathVariable int id, @RequestBody Users updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }




    //Extra Features
    @PostMapping("/login")
    public String login(@RequestBody Users user){
        return userService.verify(user);
    }

}
