package in.jaysan.controller;

import in.jaysan.dto.contact.DealerContactDto;
import in.jaysan.dto.contact.OtherContactDto;
import in.jaysan.dto.contact.SalesContactDto;
import in.jaysan.dto.contact.SupportContactDto;
import in.jaysan.entity.Contact;
import in.jaysan.service.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/contact")
@AllArgsConstructor
public class ContactController {

    private ContactService contactService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Contact contact)
    {
        contactService.save(contact);
        return ResponseEntity.ok("Contact Request Saved");
    }

    @GetMapping("/get/support")
    public ResponseEntity<List<SupportContactDto>> getSupportContactDto()
    {
        return ResponseEntity.ok(contactService.getSupportContactDto());
    }

    @GetMapping("/get/sales")
    public ResponseEntity<List<SalesContactDto>> getSalesContactDto()
    {
        return ResponseEntity.ok(contactService.getSalesContactDto());
    }

    @GetMapping("/get/dealer")
    public ResponseEntity<List<DealerContactDto>> getDealerInterestContactDto()
    {
        return ResponseEntity.ok(contactService.getDealerInterestContactDto());
    }

    @GetMapping("/get/other")
    public ResponseEntity<List<OtherContactDto>> getOtherContactDto()
    {
        return ResponseEntity.ok(contactService.getOtherContactDto());
    }

}
