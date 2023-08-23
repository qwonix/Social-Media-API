package ru.qwonix.test.social.media.api.serivce;


import ru.qwonix.test.social.media.api.entity.Post;
import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {

    /**
     * @return Optional containing the found post, or an empty Optional if not found
     */
    Optional<Post> findById(UUID postId);

    /**
     * Deletes a post by id
     */
    void delete(UUID postId);

    /**
     * Creates a new post
     *
     * @param post post to create
     * @return created post
     */
    Post createPost(Post post);

    /**
     * Updates an existing post
     *
     * @param post post with updated content
     * @return updated post
     */
    Post updatePost(Post post);

    /**
     * @return {@code true} if a post with the given id exists, {@code false} otherwise.
     */
    Boolean existsById(UUID id);

    /**
     * Checks if the specified user is the owner of the given post.
     *
     * @param postId   id of the post to check
     * @param username username of the user to check
     * @return {@code true} if the user is the owner of the post, {@code false} otherwise or if post was not found
     */
    Boolean isPostOwner(UUID postId, String username);

    /**
     * Retrieves a paginated list of posts created by the specified users
     * sorted by descending date of post creating - new first
     *
     * @param userProfiles list of user profiles whose posts to retrieve
     * @param page         page number for pagination (0-indexed)
     * @param size         number of posts per page
     * @return list of posts
     */
    List<Post> findUsersPostsPaginatedAndSortedByDate(List<UserProfile> userProfiles, int page, int size);
}