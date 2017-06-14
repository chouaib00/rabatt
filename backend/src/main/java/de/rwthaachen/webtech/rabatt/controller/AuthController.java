package de.rwthaachen.webtech.rabatt.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.rwthaachen.webtech.rabatt.model.User;
import de.rwthaachen.webtech.rabatt.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

  @Autowired
  UserRepository userRepository;

  @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<?> login(@RequestBody User login) {

    String jwtToken = "";

    if (login.getUsername() == null || login.getPassword() == null) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body("Please fill in username and password");
    }

    String username = login.getUsername();
    String password = login.getPassword();

    List<User> users = userRepository.findByUsername(username);

    if (users.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
    }

    User user = users.get(0);
    String pwd = user.getPassword();

    if (!password.equals(pwd)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Invalid login. Please check your name and password.");
    }

    jwtToken = Jwts.builder().setSubject(username).claim("roles", "user").setIssuedAt(new Date())
        .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

    return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
  }
}