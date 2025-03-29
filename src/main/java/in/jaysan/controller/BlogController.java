package in.jaysan.controller;

import in.jaysan.dto.blog.BlogRequest;
import in.jaysan.dto.blog.BlogResponse;
import in.jaysan.entity.Blog;
import in.jaysan.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/blog")
public class BlogController {

    private final BlogService blogService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BlogResponse> addBlog(
            @RequestPart("blogRequest")BlogRequest blogRequest,
            @RequestPart("blogImage")MultipartFile blogImage
            ) throws IOException {
        return ResponseEntity.ok(blogService.addBlog(blogRequest,blogImage));
    }

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getBlogById(@PathVariable Long id) {
        try {
            return  ResponseEntity.ok(blogService.getBlogById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public BlogResponse updateBlog(
            @PathVariable Long id,
            @RequestPart("blogRequest") BlogRequest blogRequest,
            @RequestPart(value = "blogImage" , required = false) MultipartFile blogImage
    ) {
        return blogService.updateBlog(id,blogRequest,blogImage);
    }

    @DeleteMapping("/{id}")
    public void deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
    }
}
