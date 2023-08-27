package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.dto.SendMessageRequest;
import ru.qwonix.test.social.media.api.facade.ChatFacade;
import ru.qwonix.test.social.media.api.mapper.MessageMapper;
import ru.qwonix.test.social.media.api.result.FindChatEntries;
import ru.qwonix.test.social.media.api.result.SendMessageEntries;
import ru.qwonix.test.social.media.api.serivce.MessageService;
import ru.qwonix.test.social.media.api.serivce.RelationService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

@RequiredArgsConstructor
@Component
public class ChatFacadeImpl implements ChatFacade {

    private final UserProfileService userProfileService;
    private final RelationService relationService;
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Override
    public SendMessageEntries.Result sendMessage(String senderUsername, String recipientUsername, SendMessageRequest sendMessageRequest) {
        var optionalSender = userProfileService.findUserByUsername(senderUsername);
        if (optionalSender.isEmpty()) {
            return SendMessageEntries.Result.SenderNotFound.INSTANCE;
        }
        var optionalRecipient = userProfileService.findUserByUsername(recipientUsername);
        if (optionalRecipient.isEmpty()) {
            return SendMessageEntries.Result.RecipientNotFound.INSTANCE;
        }

        var sender = optionalSender.get();
        var recipient = optionalRecipient.get();
        if (relationService.areNotFriends(sender, recipient)) {
            return SendMessageEntries.Result.NonFriends.INSTANCE;
        }
        var message = messageMapper.map(sendMessageRequest);
        message.setSender(sender);
        message.setRecipient(recipient);

        return new SendMessageEntries.Result.Success(messageMapper.map(messageService.save(message)));
    }

    @Override
    public FindChatEntries.Result findChatPaginated(String senderUsername, String recipientUsername, Integer page, Integer count) {
        var optionalSender = userProfileService.findUserByUsername(senderUsername);
        if (optionalSender.isEmpty()) {
            return FindChatEntries.Result.SenderNotFound.INSTANCE;
        }
        var optionalRecipient = userProfileService.findUserByUsername(recipientUsername);
        if (optionalRecipient.isEmpty()) {
            return FindChatEntries.Result.RecipientNotFound.INSTANCE;
        }

        var messageResponseList = messageService.findMessagesPaginatedAndSortedBySendingDateDesc(
                        optionalSender.get(),
                        optionalRecipient.get(),
                        page,
                        count)
                .stream().map(messageMapper::map).toList();

        return new FindChatEntries.Result.Success(messageResponseList);
    }
}
