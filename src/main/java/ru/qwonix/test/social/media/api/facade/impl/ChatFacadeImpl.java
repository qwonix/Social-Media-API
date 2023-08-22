package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.dto.SendMessageDto;
import ru.qwonix.test.social.media.api.entity.Message;
import ru.qwonix.test.social.media.api.facade.ChatFacade;
import ru.qwonix.test.social.media.api.mapper.MessageMapper;
import ru.qwonix.test.social.media.api.result.GetChatEntries;
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
    public SendMessageEntries.Result sendMessage(String senderUsername, String recipientUsername, SendMessageDto sendMessageDto) {
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
        if (Boolean.FALSE.equals(relationService.isFriends(sender, recipient))) {
            return SendMessageEntries.Result.NonFriends.INSTANCE;
        }
        Message message = messageMapper.map(sendMessageDto);
        message.setSender(sender);
        message.setRecipient(recipient);

        return new SendMessageEntries.Result.Success(messageMapper.map(messageService.save(message)));
    }

    @Override
    public GetChatEntries.Result getChatPaginated(String senderUsername, String recipientUsername, Integer page, Integer size) {
        var optionalSender = userProfileService.findUserByUsername(senderUsername);
        if (optionalSender.isEmpty()) {
            return GetChatEntries.Result.SenderNotFound.INSTANCE;
        }
        var optionalRecipient = userProfileService.findUserByUsername(recipientUsername);
        if (optionalRecipient.isEmpty()) {
            return GetChatEntries.Result.RecipientNotFound.INSTANCE;
        }

        var chatPaginated = messageService.getMessagesPaginated(
                optionalSender.get(),
                optionalRecipient.get(),
                page,
                size);

        return new GetChatEntries.Result.Success(chatPaginated.stream()
                .map(messageMapper::map).toList());
    }
}
