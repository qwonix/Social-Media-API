package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.qwonix.test.social.media.api.dto.UserRegistrationDto;
import ru.qwonix.test.social.media.api.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(target = "username", source = "registrationDto.username")
    @Mapping(target = "email", source = "registrationDto.email")
    @Mapping(target = "passwordHash", source = "registrationDto.password")
    UserProfile map(UserRegistrationDto registrationDto);
}




