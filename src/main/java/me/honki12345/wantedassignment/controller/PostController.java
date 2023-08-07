package me.honki12345.wantedassignment.controller;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.dto.PostDTO;
import me.honki12345.wantedassignment.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody PostDTO postDTO) {
        postService.create(postDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> list() {
        // TODO 페이지네이션
        List<PostDTO> postDTOs = postService.list();
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> get(@PathVariable Long id) {
        PostDTO postDTO = postService.get(id);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable Long id,
            @RequestBody PostDTO postDTO
    ) {
        // TODO 사용자 검증
        postService.update(id, postDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        // TODO 사용자 검증
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
