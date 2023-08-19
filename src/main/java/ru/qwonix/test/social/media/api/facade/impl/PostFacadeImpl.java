package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.dto.PostCreateDto;
import ru.qwonix.test.social.media.api.dto.PostUpdateDto;
import ru.qwonix.test.social.media.api.facade.PostFacade;
import ru.qwonix.test.social.media.api.mapper.PostMapper;
import ru.qwonix.test.social.media.api.result.CreatePostEntries;
import ru.qwonix.test.social.media.api.result.DeletePostEntries;
import ru.qwonix.test.social.media.api.result.FindPostEntries;
import ru.qwonix.test.social.media.api.result.UpdatePostEntries;
import ru.qwonix.test.social.media.api.serivce.PostService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PostFacadeImpl implements PostFacade {

    private final UserProfileService userProfileService;
    private final PostService postService;
    private final PostMapper postMapper;


    @Override
    public CreatePostEntries.Result create(PostCreateDto postCreateDto, String username) {
        var optionalUser = userProfileService.findUserByUsername(username);
        if (optionalUser.isPresent()) {
            var post = postMapper.map(postCreateDto);
            post.setUser(optionalUser.get());

            var servicePost = postService.createPost(post);
            var postResponseDto = postMapper.map(servicePost);
            return new CreatePostEntries.Result.Success(postResponseDto);
        } else {
            return CreatePostEntries.Result.PostOwnerNotFound.INSTANCE;
        }
    }

    @Override
    public UpdatePostEntries.Result update(UUID id, PostUpdateDto postUpdateDto) {
        var optionalPost = postService.findById(id);
        if (optionalPost.isPresent()) {
            var post = optionalPost.get();
            if (postUpdateDto.title() != null) {
                post.setTitle(postUpdateDto.title());
            }
            if (postUpdateDto.text() != null) {
                post.setText(postUpdateDto.text());
            }
            var updatedPost = postService.updatePost(post);
            var postResponseDto = postMapper.map(updatedPost);

            return new UpdatePostEntries.Result.Success(postResponseDto);
        } else {
            return UpdatePostEntries.Result.NotFound.INSTANCE;
        }

    }

    @Override
    public DeletePostEntries.Result delete(UUID id) {
        if (Boolean.FALSE.equals(postService.existsById(id))) {
            return DeletePostEntries.Result.NotFound.INSTANCE;
        }
        postService.delete(id);

        return DeletePostEntries.Result.Success.INSTANCE;
    }

    @Override
    public FindPostEntries.Result find(UUID id) {
        var optionalPost = postService.findById(id);

        if (optionalPost.isPresent()) {
            var post = optionalPost.get();
            var postDto = postMapper.map(post);
            return new FindPostEntries.Result.Success(postDto);
        } else {
            return FindPostEntries.Result.NotFound.INSTANCE;
        }
    }
}
