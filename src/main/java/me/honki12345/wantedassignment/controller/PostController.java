package me.honki12345.wantedassignment.controller;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.controller.dto.PostCreateRequestDTO;
import me.honki12345.wantedassignment.controller.dto.PostCreateResponseDTO;
import me.honki12345.wantedassignment.controller.dto.PostGetResponseDTO;
import me.honki12345.wantedassignment.controller.dto.PostUpdateRequestDTO;
import me.honki12345.wantedassignment.controller.dto.PostUpdateResponseDTO;
import me.honki12345.wantedassignment.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<PostCreateResponseDTO> create(
            @Validated @RequestBody PostCreateRequestDTO requestDTO) {
        PostCreateResponseDTO responseDTO = postService.create(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostGetResponseDTO>> list(
            @PageableDefault(page = 0, size = 10, sort = "id")
            Pageable pageable) {
        List<PostGetResponseDTO> postDTOs = postService.list(pageable);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostGetResponseDTO> get(@PathVariable Long id) {
        PostGetResponseDTO responseDTO = postService.get(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostUpdateResponseDTO> update(
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDTO requestDTO
    ) {
        PostUpdateResponseDTO responseDTO = postService.update(id, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
