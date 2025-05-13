package dev.stanislavskyi.shoppingcart.controller;

import dev.stanislavskyi.shoppingcart.dto.UserDto;
import dev.stanislavskyi.shoppingcart.exceptions.ResourceNotFoundException;
import dev.stanislavskyi.shoppingcart.model.User;
import dev.stanislavskyi.shoppingcart.request.RoleUpdateRequest;
import dev.stanislavskyi.shoppingcart.response.ApiResponse;
import dev.stanislavskyi.shoppingcart.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/userManagement")
public class UserManagementController {
    private final IUserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse> updateUserRoles(@PathVariable Long id, @RequestBody RoleUpdateRequest request){
        try {
            User user = userService.assignRoleToUser(id, request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Manage user Success!", userDto));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
