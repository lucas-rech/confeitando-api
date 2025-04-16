package com.lucasrech.confeitandoapi.user;

import com.lucasrech.confeitandoapi.config.security.JwtService;
import com.lucasrech.confeitandoapi.exceptions.UserException;
import com.lucasrech.confeitandoapi.user.dto.LoginRequestDTO;
import com.lucasrech.confeitandoapi.user.dto.UserLoginResponseDTO;
import com.lucasrech.confeitandoapi.user.dto.UserRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<UserLoginResponseDTO> saveUser(UserRequestDTO userRequestDTO) {
        String encodedPassword = passwordEncoder.encode(userRequestDTO.password());
        UserRequestDTO userDTO = new UserRequestDTO(
                userRequestDTO.name(),
                userRequestDTO.email(),
                encodedPassword
        );
        userService.createUser(userDTO);
        return ResponseEntity.ok(new UserLoginResponseDTO(userRequestDTO.name(), null));
    }

    //TODO: Refatorar para que a verificação de senha seja feita no service User
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(LoginRequestDTO loginRequestDTO) {
        String token = jwtService.generateToken(loginRequestDTO.email());
        UserDetails user = userService.loadUserByUsername(loginRequestDTO.email());
        if(user == null) {
            throw new UserException("User not found with email: " + loginRequestDTO.email());
        }
        // Check if the password matches the encoded password
        if(passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            return ResponseEntity.ok(new UserLoginResponseDTO(user.getUsername(), token));
        } else {
            throw new UserException("Invalid credentials. Password: " + loginRequestDTO.password());
        }
    }
}
