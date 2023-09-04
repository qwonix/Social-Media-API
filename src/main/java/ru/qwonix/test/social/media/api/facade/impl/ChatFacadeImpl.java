package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.dto.SendMessageRequest;
import ru.qwonix.test.social.media.api.facade.ChatFacade;
import ru.qwonix.test.social.media.api.mapper.MessageMapper;
import ru.qwonix.test.social.media.api.result.FindChat;
import ru.qwonix.test.social.media.api.result.SendMessage;
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
    public SendMessage.Result sendMessage(String senderUsername, String recipientUsername, SendMessageRequest sendMessageRequest) {
        var optionalSender = userProfileService.findUserByUsername(senderUsername);
        if (optionalSender.isEmpty()) {
            return SendMessage.Result.SenderNotFound.INSTANCE;
        }
        var optionalRecipient = userProfileService.findUserByUsername(recipientUsername);
        if (optionalRecipient.isEmpty()) {
            return SendMessage.Result.RecipientNotFound.INSTANCE;
        }

        var sender = optionalSender.get();
        var recipient = optionalRecipient.get();
        if (relationService.areNotFriends(sender, recipient)) {
            return SendMessage.Result.NonFriends.INSTANCE;
        }
        var message = messageMapper.map(sendMessageRequest);
        message.setSender(sender);
        message.setRecipient(recipient);

        return new SendMessage.Result.Success(messageMapper.map(messageService.save(message)));
    }

    @Override
    public FindChat.Result findChatPaginated(String senderUsername, String recipientUsername, Integer page, Integer count) {
        var optionalSender = userProfileService.findUserByUsername(senderUsername);
        if (optionalSender.isEmpty()) {
            return FindChat.Result.SenderNotFound.INSTANCE;
        }
        var optionalRecipient = userProfileService.findUserByUsername(recipientUsername);
        if (optionalRecipient.isEmpty()) {
            return FindChat.Result.RecipientNotFound.INSTANCE;
        }

        var messageResponseList = messageService.findMessagesPaginatedAndSortedBySendingDateDesc(
                        optionalSender.get(),
                        optionalRecipient.get(),
                        page,
                        count)
                .stream().map(messageMapper::map).toList();

        return new FindChat.Result.Success(messageResponseList);
    }
}
