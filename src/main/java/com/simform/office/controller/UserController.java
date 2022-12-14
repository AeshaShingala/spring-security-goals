package com.simform.office.controller;

import com.simform.office.dto.GenericResponse;
import com.simform.office.dto.UserDetails;
import com.simform.office.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    //add user
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<GenericResponse> addEmployee(@RequestBody UserDetails userDetails) throws MessagingException {
        return new ResponseEntity<>(new GenericResponse(true, "user added successfully", userService.addEmployee(userDetails), HttpStatus.OK.value(), LocalDateTime.now()), HttpStatus.OK);
    }

    //get list of users excluding currently authenticated user
    @GetMapping
    public ResponseEntity<GenericResponse> getUsers() {
        return new ResponseEntity<>(new GenericResponse(true, "users returned successfully", userService.getUsers(), HttpStatus.OK.value(), LocalDateTime.now()), HttpStatus.OK);
    }

    //find user by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<GenericResponse> findEmployee(@PathVariable("id") long id) {
        return new ResponseEntity<>(new GenericResponse(true, "user found successfully", userService.findEmployeeById(id), HttpStatus.OK.value(), LocalDateTime.now()), HttpStatus.OK);
    }

    //get list of all interns
    @GetMapping("/employees")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_INTERN')")
    public ResponseEntity<GenericResponse> getEmployees() {
        return new ResponseEntity<>(new GenericResponse(true, "employees returned successfully", userService.getEmployees(), HttpStatus.OK.value(), LocalDateTime.now()), HttpStatus.OK);
    }

    //return role of respective authenticated user only
    @GetMapping("/role/{email}")
    @PostAuthorize("#email == authentication.principal.username")
    public ResponseEntity<GenericResponse> getMyRole(@PathVariable("email") String email) {
        return new ResponseEntity<>(new GenericResponse(true, "user role returned successfully", userService.findMyRole(email), HttpStatus.OK.value(), LocalDateTime.now()), HttpStatus.OK);
    }

    //soft delete an user
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponse> deleteEmployee(@PathVariable("id") long id) {
        return new ResponseEntity<>(new GenericResponse(true, "employee deleted successfully", userService.deleteEmployee(id), HttpStatus.OK.value(), LocalDateTime.now()), HttpStatus.OK);
    }
}