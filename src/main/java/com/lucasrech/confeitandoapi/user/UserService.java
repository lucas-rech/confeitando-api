package com.lucasrech.confeitandoapi.user;

import com.lucasrech.confeitandoapi.exceptions.UserException;
import com.lucasrech.confeitandoapi.user.dto.UserRequestDTO;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void createUser(UserRequestDTO user) {
       if(user.email() == null || user.email().isEmpty()) {
            throw new UserException("Email cannot be null or empty");
        }
        if(user.password() == null || user.password().isEmpty()) {
            throw new UserException("Password cannot be null or empty");
        }
        if(user.name() == null || user.name().isEmpty()) {
            throw new UserException("Name cannot be null or empty");
        }
        if(userRepository.findByEmail(user.email()).isPresent()) {
            throw new UserException("Email already exists");
        }
        UserEntity newUser = new UserEntity(
                user.name(),
                user.email(),
                user.password()
        );

        userRepository.save(newUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("ADMIN")
                .build();
    }
}
