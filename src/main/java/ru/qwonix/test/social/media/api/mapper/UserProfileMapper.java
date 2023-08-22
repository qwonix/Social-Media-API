package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.qwonix.test.social.media.api.dto.FullUserProfileResponse;
import ru.qwonix.test.social.media.api.dto.PublicUserProfileResponse;
import ru.qwonix.test.social.media.api.dto.UserRegistrationRequest;
import ru.qwonix.test.social.media.api.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "passwordHash", source = "password")
    UserProfile map(UserRegistrationRequest registrationRequest);

    FullUserProfileResponse mapToFull(UserProfile userProfile);

    PublicUserProfileResponse mapToPublic(UserProfile userProfile);
}




