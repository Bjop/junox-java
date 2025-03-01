package localhost.pythonController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/wishlist/action")
public class Controller {
    private final ProjectService projectService;
    
    public Controller(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping()
    public ResponseEntity<Resource> downloadNotebook() {
        System.out.println("Downloading notebook");
        Resource fileResource = projectService.get();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=notebook.ipynb")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
    }

    @PostMapping(value = "/upload-notebook", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadNotebook(@RequestPart("file") MultipartFile file) {
        System.out.println("Uploading notebook");
        if (file == null
                || file.isEmpty()
                || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".ipynb")) {
            throw new IllegalArgumentException("Uploaded file is not a valid Jupyter Notebook (.ipynb).");
        }
        projectService.upload(file);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadBinary(@RequestPart("file") MultipartFile file) {
        System.out.println("Uploading binary");
        if (file == null
                || file.isEmpty()
                || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".zip")) {
            throw new IllegalArgumentException("Uploaded file is not a valid zip file (.zip).");
        }
        projectService.saveBinary(file);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/execute-notebooks")
    public ResponseEntity<Void> executeNotebooks() {
        System.out.println("Executing notebooks");
        projectService.executeNoteBook();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteNotebook() {
        System.out.println("Deleting notebook");
        projectService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
