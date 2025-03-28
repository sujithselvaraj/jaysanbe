package in.jaysan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.jaysan.dto.team.TeamRequest;
import in.jaysan.dto.team.TeamResponse;
import in.jaysan.service.TeamService;
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
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<TeamResponse> addTeamMember(
            @RequestPart("teamRequest")TeamRequest request,
            @RequestPart(value = "imageFile")MultipartFile imageFile
            ) throws IOException {
        return ResponseEntity.ok(teamService.addTeamMember(request,imageFile));
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeamMembers()
    {
        return ResponseEntity.ok(teamService.getAllTeam());
    }

    @PutMapping("/{id}")
    public TeamResponse updateTeam(
            @PathVariable Long id,
            @RequestPart("teamRequest") TeamRequest teamRequestJson,
            @RequestPart(value = "imageFile" , required = false) MultipartFile imageFile
    ) throws JsonProcessingException {
       return teamService.updateTeamMember(id,teamRequestJson,imageFile);

    }

    @DeleteMapping("/{id}")
    public void deleteTeamMember(@PathVariable Long id) {
        teamService.deleteTeamMember(id);
    }

}
