package ru.practicum.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.user.userDto.UserDto;

@FeignClient(url = "${shareit-server.url}", path = "/users", name = "userClient")
public interface UserClient {

    @PostMapping
    UserDto createUser(@RequestBody UserDto userDto);
}