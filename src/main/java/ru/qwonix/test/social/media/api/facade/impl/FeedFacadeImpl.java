package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.dto.FeedDto;
import ru.qwonix.test.social.media.api.dto.PostResponseDto;
import ru.qwonix.test.social.media.api.facade.FeedFacade;
import ru.qwonix.test.social.media.api.mapper.FeedMapper;
import ru.qwonix.test.social.media.api.mapper.PostMapper;
import ru.qwonix.test.social.media.api.result.GetFeedEntries;
import ru.qwonix.test.social.media.api.serivce.PostService;
import ru.qwonix.test.social.media.api.serivce.RelationService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FeedFacadeImpl implements FeedFacade {

    private final UserProfileService userProfileService;
    private final PostService postService;
    private final RelationService relationService;
    private final FeedMapper feedMapper;
    private final PostMapper postMapper;


    @Override
    public GetFeedEntries.Result getFeedPaginated(String username, int page, int size) {
        var optionalUser = userProfileService.findUserByUsername(username);
        if (optionalUser.isEmpty()) {
            return GetFeedEntries.Result.UserNotFound.INSTANCE;
        }

        var userSubscriptions = relationService.findAllSubscriptions(optionalUser.get());
        var usersPosts = postService.findUsersPostsPaginated(userSubscriptions, page, size);
        var postResponseDtos = usersPosts.stream()
                .map(postMapper::map).toList();
        return new GetFeedEntries.Result.Success(new FeedDto(postResponseDtos));
    }
}
