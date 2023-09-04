package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.dto.AttachImageRequest;
import ru.qwonix.test.social.media.api.dto.CreatePostRequest;
import ru.qwonix.test.social.media.api.dto.DetachImageRequest;
import ru.qwonix.test.social.media.api.dto.UpdatePostRequest;
import ru.qwonix.test.social.media.api.facade.PostFacade;
import ru.qwonix.test.social.media.api.mapper.PostMapper;
import ru.qwonix.test.social.media.api.result.*;
import ru.qwonix.test.social.media.api.serivce.ImageService;
import ru.qwonix.test.social.media.api.serivce.PostService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PostFacadeImpl implements PostFacade {

    private final UserProfileService userProfileService;
    private final PostService postService;
    private final ImageService imageService;
    private final PostMapper postMapper;


    @Override
    public CreatePost.Result create(CreatePostRequest createPostRequest, String username) {
        var optionalUser = userProfileService.findUserByUsername(username);
        if (optionalUser.isEmpty()) {
            return CreatePost.Result.PostOwnerNotFound.INSTANCE;
        }
        var post = postMapper.map(createPostRequest);
        post.setUser(optionalUser.get());

        var servicePost = postService.createPost(post);
        return new CreatePost.Result.Success(postMapper.map(servicePost));

    }

    @Override
    public UpdatePost.Result update(UUID id, UpdatePostRequest updatePostRequest) {
        var optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return UpdatePost.Result.NotFound.INSTANCE;
        }

        var post = optionalPost.get();
        if (updatePostRequest.title() != null) {
            post.setTitle(updatePostRequest.title());
        }
        if (updatePostRequest.text() != null) {
            post.setText(updatePostRequest.text());
        }

        var updatedPost = postService.updatePost(post);

        return new UpdatePost.Result.Success(postMapper.map(updatedPost));
    }

    @Override
    public DeletePost.Result delete(UUID id) {
        if (!postService.existsById(id)) {
            return DeletePost.Result.NotFound.INSTANCE;
        }
        postService.delete(id);

        return DeletePost.Result.Success.INSTANCE;
    }

    @Override
    public FindPost.Result find(UUID id) {
        var optionalPost = postService.findById(id);

        if (optionalPost.isEmpty()) {
            return FindPost.Result.NotFound.INSTANCE;
        }
        var post = optionalPost.get();
        return new FindPost.Result.Success(postMapper.map(post));
    }

    @Override
    public AttachImageToPost.Result attachImage(UUID id, AttachImageRequest attachImageRequest) {
        var optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return AttachImageToPost.Result.PostNotFound.INSTANCE;
        }
        var optionalImage = imageService.findByName(attachImageRequest.imageName());
        if (optionalImage.isEmpty()) {
            return AttachImageToPost.Result.ImageNotFound.INSTANCE;
        }

        var post = optionalPost.get();
        var image = optionalImage.get();
        if (post.getImages().contains(image)) {
            return AttachImageToPost.Result.ImageAlreadyAttached.INSTANCE;
        }

        post.getImages().add(image);
        var updatedPost = postService.updatePost(post);
        return new AttachImageToPost.Result.Success(postMapper.map(updatedPost));
    }

    @Override
    public DetachImageFromPost.Result detachImage(UUID id, DetachImageRequest detachImageRequest) {
        var optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return DetachImageFromPost.Result.PostNotFound.INSTANCE;
        }
        var optionalImage = imageService.findByName(detachImageRequest.imageName());
        if (optionalImage.isEmpty()) {
            return DetachImageFromPost.Result.ImageNotFound.INSTANCE;
        }

        var post = optionalPost.get();
        var image = optionalImage.get();
        if (!post.getImages().contains(image)) {
            return DetachImageFromPost.Result.ImageNotAttached.INSTANCE;
        }

        post.getImages().remove(image);
        var updatedPost = postService.updatePost(post);
        return new DetachImageFromPost.Result.Success(postMapper.map(updatedPost));
    }
}
