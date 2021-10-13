package com.junghalee.todo.controller;

import com.junghalee.todo.dto.ResponseDTO;
import com.junghalee.todo.dto.TodoDTO;
import com.junghalee.todo.model.TodoEntity;
import com.junghalee.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user"; // temporary user id

            TodoEntity entity = TodoDTO.toEntity(dto); // convert dto to entity

            entity.setId(null); // id should be null when creating

            entity.setUserId(temporaryUserId); // set temporary user id - will be updated later

            List<TodoEntity> entities = service.create(entity); // create todoEntity using service

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList()); // convert entity list to dto list

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build(); // initiate responseDTO using dto list

            return ResponseEntity.ok().body(response); // return responseDTO
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
