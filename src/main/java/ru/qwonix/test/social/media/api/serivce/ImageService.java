package ru.qwonix.test.social.media.api.serivce;

import ru.qwonix.test.social.media.api.entity.Image;

import java.util.Optional;


public interface ImageService {

    /**
     * Saves the given image
     *
     * @param image image to be saved
     * @return saved image
     */
    Image save(Image image);

    /**
     * Checks if an image with the specified name exists
     *
     * @param name name of the image to check
     * @return {@code true} if an image with the given name exists, {@code false} otherwise
     */
    boolean existsByName(String name);

    /**
     * Finds an image by its name
     *
     * @param name name of the image to find
     * @return an Optional containing the found image, or an empty Optional if not found
     */
    Optional<Image> findByName(String name);

    /**
     * Checks if the specified user is the owner of the given image
     *
     * @param imageName name of the image to check
     * @param username  username of the user to check
     * @return {@code true} if the user is the owner of the image, {@code false} otherwise or if image was not found
     */
    boolean isImageOwner(String imageName, String username);
}
