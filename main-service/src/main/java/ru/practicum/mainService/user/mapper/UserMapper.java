package ru.practicum.mainService.user.mapper;

import ru.practicum.mainService.user.User;
import ru.practicum.mainService.user.dto.PartialUserDto;
import ru.practicum.mainService.user.dto.UserDto;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static PartialUserDto toPartialUserDto(User user) {
        return new PartialUserDto(
                user.getId(),
                user.getName()
        );
    }

}