package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import ru.qwonix.test.social.media.api.dto.FeedDto;
import ru.qwonix.test.social.media.api.entity.Post;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedMapper {
    FeedDto map(PostsWrapper postsWrapper);

    record PostsWrapper(List<Post> posts) {
    }

    default FeedDto map(List<Post> posts) {
        return map(new PostsWrapper(posts));
    }
}
