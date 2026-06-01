package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountService accountService) {
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public Integer deleteMessageById(Integer messageId) {
        if (!messageRepository.existsById(messageId)) {
            return null;
        }

        messageRepository.deleteById(messageId);
        return 1;
    }

    public Integer updateMessageById(Integer messageId, Message newMessageData) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isEmpty()) {
            return null;
        }

        Message existingMessage = optionalMessage.get();
        existingMessage.setMessageText(newMessageData.getMessageText());

        messageRepository.save(existingMessage);

        return 1;
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    public boolean isValidNewMessage(Message message) {
        if (message == null) {
            return false;
        }

        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            return false;
        }

        if (message.getMessageText().length() > 255) {
            return false;
        }

        if (message.getPostedBy() == null || !accountService.accountExists(message.getPostedBy())) {
            return false;
        }

        return true;
    }

    public boolean isValidMessageText(Message message) {
        if (message == null) {
            return false;
        }

        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            return false;
        }

        if (message.getMessageText().length() > 255) {
            return false;
        }

        return true;
    }
}