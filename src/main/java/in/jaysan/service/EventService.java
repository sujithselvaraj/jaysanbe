package in.jaysan.service;

import in.jaysan.dto.event.EventRequest;
import in.jaysan.dto.event.EventResponse;
import in.jaysan.entity.Event;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.repository.EventRepository;
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
public class EventService {

    private final EventRepository eventRepository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    private final S3Client s3Client;

    public EventService(EventRepository eventRepository, S3Client s3Client) {
        this.eventRepository = eventRepository;
        this.s3Client = s3Client;
    }

    public EventResponse addEvent(EventRequest eventRequest, MultipartFile image) {
        String imageUrl = image != null ? saveImage(image) : null;
        Event event = new Event();
        event.setEventName(eventRequest.getEventName());
        event.setEventDesc(eventRequest.getEventDesc());
        event.setImageUrl(imageUrl);

        event = eventRepository.save(event);
        return mapToEventResponse(event);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToEventResponse)
                .toList();
    }

    public EventResponse updateEvent(Long id, EventRequest eventRequest, MultipartFile image) {
        return eventRepository.findById(id).map(event -> {
            if (image != null && !image.isEmpty()) {
                event.setImageUrl(saveImage(image));
            }
            event.setEventName(eventRequest.getEventName());
            event.setEventDesc(eventRequest.getEventDesc());
            Event updatedEvent = eventRepository.save(event);
            return mapToEventResponse(updatedEvent);
        }).orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private EventResponse mapToEventResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getEventName(),
                event.getImageUrl(),
                event.getEventDesc()
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
