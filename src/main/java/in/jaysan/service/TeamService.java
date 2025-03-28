package in.jaysan.service;

import in.jaysan.dto.team.TeamRequest;
import in.jaysan.dto.team.TeamResponse;
import in.jaysan.entity.Team;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.repository.TeamRepository;
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
public class TeamService {

    private final TeamRepository repository;
    @Value("${aws.s3.bucketname}")
    private String bucketName;


    private final S3Client s3Client;

    public TeamService(TeamRepository repository, S3Client s3Client) {
        this.repository = repository;
        this.s3Client = s3Client;
    }

    public TeamResponse addTeamMember(TeamRequest teamRequest, MultipartFile image) throws IOException {
        String filePath = image != null ? saveImage(image) : null;
        Team team = new Team();

        team.setName(teamRequest.getName());
        team.setDesignation(teamRequest.getDesignation());
        team.setImageUrl(filePath);
        team.setEmail(teamRequest.getEmail());
        team.setWhatsappNumber(teamRequest.getWhatsappNumber());

        team = repository.save(team);
        return mapToTeamResponse(team);
    }

    public List<TeamResponse> getAllTeam() {
        return repository.findAll().stream().map(this::mapToResponse).toList();
    }

    public TeamResponse updateTeamMember(Long id, TeamRequest teamRequest,MultipartFile image) {
        return repository.findById(id).map(team -> {
            System.out.println("Updating Team with ID: " + id);

            if (image != null && !image.isEmpty()) {
                System.out.println("Saving new image...");
                team.setImageUrl(saveImage(image));
            }

            team.setDesignation(teamRequest.getDesignation());
            team.setName(teamRequest.getName());
            team.setEmail(teamRequest.getEmail());
            team.setWhatsappNumber(teamRequest.getWhatsappNumber());

            Team team1 = repository.save(team);
            System.out.println("Team updated successfully!");

            return mapToTeamResponse(team1);
        }).orElseThrow(() -> new ResourceNotFoundException("Team Member not found"));
    }

    public void deleteTeamMember(Long id) {
        repository.deleteById(id);
    }


    private TeamResponse mapToTeamResponse(Team team) {
        String imageUrl = team.getImageUrl() != null ? "/images/" + team.getImageUrl() : null;
        return new TeamResponse(team.getId(), team.getName(), team.getDesignation(), team.getEmail(), team.getWhatsappNumber(),imageUrl);
    }

    private TeamResponse mapToResponse(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getDesignation(),
                team.getEmail(),
                team.getWhatsappNumber(),
                team.getImageUrl()
        );
    }
    private String saveImage(MultipartFile file) {

        String fileNameExtension= file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf(".")+1);

        String key= UUID.randomUUID().toString()+"."+fileNameExtension;
        try {
            PutObjectRequest putObjectRequest= PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse response= s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful())
            {
                return "https://"+bucketName+".s3.amazonaws.com/"+key;

            }
            else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File Upload failed");
            }
        }

        catch(IOException e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error Occured while uplaodung the file");
        }
    }
}
