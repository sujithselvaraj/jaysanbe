package in.jaysan.controller;

import in.jaysan.dto.event.EventRequest;
import in.jaysan.dto.event.EventResponse;
import in.jaysan.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/event")
public class EventController {

    private EventService eventService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<EventResponse> addEvent(
            @RequestPart("eventRequest") EventRequest eventRequest,
            @RequestPart("eventImage") MultipartFile eventImage
    ) throws IOException {
        return ResponseEntity.ok(eventService.addEvent(eventRequest,eventImage));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PutMapping("/{id}")
    public EventResponse updateEvent(
            @PathVariable Long id,
            @RequestPart("eventRequest") EventRequest eventRequest,
            @RequestPart(value = "eventImage" , required = false) MultipartFile eventImage
    ) {
        return eventService.updateEvent(id,eventRequest,eventImage);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }


}
