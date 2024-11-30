package org.example.rest.mappers;

import org.example.rest.dto.UserGetDTO;
import org.example.rest.models.User;

public class UserMapper {

    public UserGetDTO userToUserGetDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole());
    }
}


