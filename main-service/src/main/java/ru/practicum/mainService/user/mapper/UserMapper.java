package ru.practicum.mainService.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainService.user.User;
import ru.practicum.mainService.user.dto.PartialUserDto;
import ru.practicum.mainService.user.dto.UserDto;

@UtilityClass
public final class UserMapper {

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public PartialUserDto toPartialUserDto(User user) {
        return new PartialUserDto(
                user.getId(),
                user.getName()
        );
    }

}