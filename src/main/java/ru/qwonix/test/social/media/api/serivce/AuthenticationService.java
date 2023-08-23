package ru.qwonix.test.social.media.api.serivce;

import ru.qwonix.test.social.media.api.entity.Token;
import ru.qwonix.test.social.media.api.exception.TokenValidationException;

public interface AuthenticationService {

    /**
     * Serializes a Token object into a string
     *
     * @param token token to be serialized
     * @return serialized token
     */
    String serializeToken(Token token);

    /**
     * Parses and validates an access token, returning a Token object containing authentication information
     *
     * @param token access token to parse and validate
     * @return Token object containing authentication information
     * @throws TokenValidationException if the token is invalid
     */
    Token parseAccessToken(String token) throws TokenValidationException;
}
