package com.otmetkaX.controller;

import com.otmetkaX.exception.CustomException;
import com.otmetkaX.model.Notification;
import com.otmetkaX.response.NotificationResponseMessage;
import com.otmetkaX.response.ResponseMessage;
import com.otmetkaX.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // получение всех уведомлений
    @GetMapping("notification")
    public ResponseEntity<ResponseMessage> getAll() {
        List<Notification> notificationList = notificationService.findAll();
        ResponseMessage response =
                new NotificationResponseMessage("Successes", null, 200, null, notificationList, null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // получение уведомления по id
    @GetMapping("notification/id")
    public ResponseEntity<ResponseMessage> getById(@RequestParam Long id) throws CustomException {
        Notification notificationList = notificationService.findById(id);
        ResponseMessage response =
                new NotificationResponseMessage("Successes", null, 200, null, null, notificationList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // поисковик уведомлений по тексту уведомления
    @GetMapping("notification/search")
    public ResponseEntity<ResponseMessage> getSearchTextMessage( @RequestParam String searchText) {
        List<Notification> notificationList = notificationService.findByTextContaining(searchText);
        ResponseMessage response =
                new NotificationResponseMessage("Successes", null, 200, null, notificationList, null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // сохранение уведомления
    @PostMapping("notification")
    public ResponseEntity<ResponseMessage> save(@RequestParam String text, @RequestParam Long senderId) throws CustomException {
        notificationService.save(text, senderId);
        ResponseMessage response = new ResponseMessage("Successes", null, 200, null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // обновление уведомления по id
    @PutMapping("notification")
    public ResponseEntity<ResponseMessage> update(@RequestParam Long id, @RequestParam String text, @RequestParam Long senderId) throws CustomException {
        notificationService.update(id, text, senderId);
        ResponseMessage response = new ResponseMessage("Successes", null, 200, null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // удаление уведомления по id
    @DeleteMapping("notification")
    public ResponseEntity<ResponseMessage> delete(@RequestParam Long id) throws CustomException {
        notificationService.deleteById(id);
        ResponseMessage response = new ResponseMessage("Successes", null, 200, null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
