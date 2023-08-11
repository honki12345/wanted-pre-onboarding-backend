package me.honki12345.wantedassignment.controller;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.dto.PostDTO;
import me.honki12345.wantedassignment.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<PostDTO> create(@RequestBody PostDTO postDTO) {
        PostDTO savedPostDTO = postService.create(postDTO);
        return new ResponseEntity<>(savedPostDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> list(
            @PageableDefault(page = 0, size = 10, sort = "id")
            Pageable pageable) {
        List<PostDTO> postDTOs = postService.list(pageable);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> get(@PathVariable Long id) {
        PostDTO postDTO = postService.get(id);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostDTO> update(
            @PathVariable Long id,
            @RequestBody PostDTO postDTO
    ) {
        PostDTO updatedDTO = postService.update(id, postDTO);
        return new ResponseEntity<>(updatedDTO, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
