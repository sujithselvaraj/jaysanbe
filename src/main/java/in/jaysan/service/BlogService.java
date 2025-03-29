package in.jaysan.service;

import in.jaysan.dto.blog.BlogRequest;
import in.jaysan.dto.blog.BlogResponse;
import in.jaysan.entity.Blog;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BlogService {

    private final BlogRepository repository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    private final S3Client s3Client;

    public BlogService(BlogRepository repository, S3Client s3Client) {
        this.repository = repository;
        this.s3Client = s3Client;
    }

    public BlogResponse addBlog(BlogRequest blogRequest, MultipartFile image) throws IOException {
        String imageUrl = image != null ? saveImage(image) : null;
        Blog blog = new Blog();
        blog.setBlogTitle(blogRequest.getBlogTitle());
        blog.setBlogContent(blogRequest.getBlogContent());
        blog.setBlogImageUrl(imageUrl);

        blog = repository.save(blog);
        return mapToBlogResponse(blog);
    }

    public List<BlogResponse> getAllBlogs() {
        return repository.findAll().stream()
                .map(this::mapToBlogResponse)
                .toList();
    }

    public BlogResponse getBlogById(Long id) {
        Blog blog = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        return mapToBlogResponse(blog);
    }

    public BlogResponse updateBlog(Long id, BlogRequest blogRequest, MultipartFile image) {
        return repository.findById(id).map(blog -> {
            if (image != null && !image.isEmpty()) {
                blog.setBlogImageUrl(saveImage(image));
            }
            blog.setBlogTitle(blogRequest.getBlogTitle());
            blog.setBlogContent(blogRequest.getBlogContent());
            Blog updatedBlog = repository.save(blog);
            return mapToBlogResponse(updatedBlog);
        }).orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
    }

    public void deleteBlog(Long id) {
        repository.deleteById(id);
    }

    private BlogResponse mapToBlogResponse(Blog blog) {
        return new BlogResponse(
                blog.getId(),
                blog.getBlogTitle(),
                blog.getBlogContent(),
                blog.getBlogImageUrl(),
                blog.getCreatedAt(),
                blog.getUpdatedAt()
        );
    }

    private String saveImage(MultipartFile file) {
        String fileNameExtension = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String key = UUID.randomUUID().toString() + "." + fileNameExtension;
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            if (response.sdkHttpResponse().isSuccessful()) {
                return "https://" + bucketName + ".s3.amazonaws.com/" + key;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while uploading the file");
        }
    }
}
