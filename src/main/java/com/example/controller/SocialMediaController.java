package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if (!accountService.isValidRegistration(account)) {
            return ResponseEntity.status(400).build();
        }

        if (accountService.findByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(409).build();
        }

        Account savedAccount = accountService.register(account);
        return ResponseEntity.status(200).body(savedAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.login(account);

        if (loggedInAccount == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.status(200).body(loggedInAccount);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (!messageService.isValidNewMessage(message)) {
            return ResponseEntity.status(400).build();
        }

        Message savedMessage = messageService.createMessage(message);
        return ResponseEntity.status(200).body(savedMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages() {
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);

        if (message.isPresent()) {
            return ResponseEntity.status(200).body(message.get());
        }

        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        Integer rowsUpdated = messageService.deleteMessageById(messageId);

        if (rowsUpdated == null) {
            return ResponseEntity.status(200).build();
        }

        return ResponseEntity.status(200).body(rowsUpdated);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(
            @PathVariable Integer messageId,
            @RequestBody Message message
    ) {
        if (!messageService.isValidMessageText(message)) {
            return ResponseEntity.status(400).build();
        }

        Integer rowsUpdated = messageService.updateMessageById(messageId, message);

        if (rowsUpdated == null) {
            return ResponseEntity.status(400).build();
        }

        return ResponseEntity.status(200).body(rowsUpdated);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<?> getMessagesByAccountId(@PathVariable Integer accountId) {
        return ResponseEntity.status(200).body(messageService.getMessagesByAccountId(accountId));
    }
}