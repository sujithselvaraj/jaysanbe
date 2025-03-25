package in.jaysan.service;

import in.jaysan.dto.contact.DealerContactDto;
import in.jaysan.dto.contact.OtherContactDto;
import in.jaysan.dto.contact.SalesContactDto;
import in.jaysan.dto.contact.SupportContactDto;
import in.jaysan.entity.Contact;
import in.jaysan.option.Status;
import in.jaysan.repository.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactService {

    private ContactRepository contactRepository;

    public Contact save(Contact contact) {
        contactRepository.save(contact);
        return contact;
    }


    public List<SupportContactDto> getSupportContactDto() {
        return contactRepository.findByPurposeIgnoreCase("Support").stream()
                .map(contact -> new SupportContactDto(contact.getId(), contact.getName(), contact.getPhoneNumber(), contact.getLocation(), contact.getProduct(), contact.getIssue(),contact.getStatus(), contact.getComments()))
                .collect(Collectors.toList());
    }

    public List<SalesContactDto> getSalesContactDto() {
        return contactRepository.findByPurposeIgnoreCase("Sales Enquiry").stream()
                .map(contact -> new SalesContactDto(contact.getId(), contact.getName(), contact.getPhoneNumber(), contact.getLocation(), contact.getProduct(), contact.getStatus(), contact.getComments()))
                .collect(Collectors.toList());
    }

    public List<DealerContactDto> getDealerInterestContactDto() {
        return contactRepository.findByPurposeIgnoreCase("Dealer Partnership").stream()
                .map(contact -> new DealerContactDto(contact.getId(), contact.getName(), contact.getPhoneNumber(), contact.getLocation(), contact.getProduct(), contact.getCompanyName(),contact.getStatus(), contact.getComments()))
                .collect(Collectors.toList());
    }

    public List<OtherContactDto> getOtherContactDto() {
        return contactRepository.findByPurposeIgnoreCase("Other").stream()
                .map(contact -> new OtherContactDto(contact.getId(), contact.getName(), contact.getPhoneNumber(), contact.getLocation(),contact.getStatus(), contact.getComments()))
                .collect(Collectors.toList());
    }

    public List<Contact> getFilteredContacts(String purpose, Status status) {
        return contactRepository.findByPurposeAndStatus(purpose, status);
    }

    public void updateStatus(Long id, Status status, String comments) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setStatus(status);
        if (comments != null) {
            contact.setComments(comments);
        }
        contactRepository.save(contact);
    }
}
