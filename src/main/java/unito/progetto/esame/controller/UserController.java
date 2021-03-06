package unito.progetto.esame.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unito.progetto.esame.model.Client;
import unito.progetto.esame.model.Supplier;
import unito.progetto.esame.model.User;
import unito.progetto.esame.security.JWT.JwtProvider;
import unito.progetto.esame.service.UserService;
import unito.progetto.esame.vo.request.LoginForm;
import unito.progetto.esame.vo.response.JwtResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Collections;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginForm loginForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generate(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findOne(userDetails.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwt, user.getEmail(), user.getName(), user.getRole(), user.getId(),user.getPassword(),user.getPhone()));
        } catch (AuthenticationException e) {
            try {

            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @PostMapping("/registerClient")
    public ResponseEntity<?> save(@RequestBody Client client) {
        try {
            return ResponseEntity.ok(userService.saveClient(client));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/registerSupplier")
    public ResponseEntity<?> save(@RequestBody Supplier supplier) {
        try {
            return ResponseEntity.ok(userService.save(supplier));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/profileClient")
    public ResponseEntity<Client> updateClient(@RequestBody Client client, Principal principal) {

        try {
            if (!principal.getName().equals(client.getEmail())) throw new IllegalArgumentException();
            return ResponseEntity.ok(userService.updateClient(client));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/profileSupplier")
    public ResponseEntity<Supplier> updateSupplier(@RequestBody Supplier supplier, Principal principal) {

        try {
            if (!principal.getName().equals(supplier.getEmail())) throw new IllegalArgumentException();
            return ResponseEntity.ok(userService.updateSupplier(supplier));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/profile")
    public ResponseEntity<User> update(@RequestBody User user, Principal principal) {


        System.out.println(user.toString());
        try {
            if (!principal.getName().equals(user.getEmail())) throw new IllegalArgumentException();
            return ResponseEntity.ok(userService.update(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @GetMapping("/profile/{email}")
    public ResponseEntity<User> getProfile(@PathVariable("email") String email, Principal principal) {

        return ResponseEntity.ok(userService.findOne(email));
    }

    @GetMapping("/profileClient/{email}")
    public ResponseEntity<Client> getProfileClient(@PathVariable("email") String email, Principal principal) {
        if (principal.getName().equals(email)) {
            return ResponseEntity.ok(userService.findOneClient(email));
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/profileSupplier/{email}")
    public ResponseEntity<Supplier> getProfileSupplier(@PathVariable("email") String email, Principal principal) {
        if (principal.getName().equals(email)) {
            return ResponseEntity.ok(userService.findOneSupplier(email));
        } else {
            return ResponseEntity.badRequest().build();
        }

    }


    @PutMapping("/updateCredits/{discount}/{id}")
    public ResponseEntity<Client> updateCredits(@RequestBody Client client, Principal principal,
                                                @PathVariable("discount") Double discount,
                                                @PathVariable("id") Long id,
                                                BindingResult bindingResult) {

        return ResponseEntity.ok(userService.updateCredits(discount.intValue() * -10, id));
    }







}
