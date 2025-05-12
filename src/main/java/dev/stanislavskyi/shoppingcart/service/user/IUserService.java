package dev.stanislavskyi.shoppingcart.service.user;

import dev.stanislavskyi.shoppingcart.dto.UserDto;
import dev.stanislavskyi.shoppingcart.model.User;
import dev.stanislavskyi.shoppingcart.request.CreateUserRequest;
import dev.stanislavskyi.shoppingcart.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
