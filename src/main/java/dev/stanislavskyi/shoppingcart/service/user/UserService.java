package dev.stanislavskyi.shoppingcart.service.user;

import dev.stanislavskyi.shoppingcart.data.RoleRepository;
import dev.stanislavskyi.shoppingcart.dto.UserDto;
import dev.stanislavskyi.shoppingcart.exceptions.AlreadyExistsException;
import dev.stanislavskyi.shoppingcart.exceptions.ResourceNotFoundException;
import dev.stanislavskyi.shoppingcart.model.Role;
import dev.stanislavskyi.shoppingcart.model.User;
import dev.stanislavskyi.shoppingcart.repository.UserRepository;
import dev.stanislavskyi.shoppingcart.request.CreateUserRequest;
import dev.stanislavskyi.shoppingcart.request.RoleUpdateRequest;
import dev.stanislavskyi.shoppingcart.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return  Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return  userRepository.save(user);
                }) .orElseThrow(() -> new AlreadyExistsException("Oops!" +request.getEmail() +" already exists!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return  userRepository.findById(userId).map(existingUser ->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete, () ->{
            throw new ResourceNotFoundException("User not found!");
        });
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    @Override
    public User assignRoleToUser(Long id, RoleUpdateRequest request) {
        User user = getUserById(id);

        Role role = roleRepository.findByName(request.getRole()).orElseThrow(
                () -> new ResourceNotFoundException("Role not found!"));

        user.getRoles().add(role);
        return userRepository.save(user);
    }


}

