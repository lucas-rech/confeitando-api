package com.lucasrech.confeitandoapi.user;

import com.lucasrech.confeitandoapi.config.security.JwtService;
import com.lucasrech.confeitandoapi.user.dto.UserLoginResponseDTO;
import com.lucasrech.confeitandoapi.user.dto.UserRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
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
}
