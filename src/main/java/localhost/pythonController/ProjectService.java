package localhost.pythonController;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class ProjectService {

    public Resource get() {
        Path folder = Paths.get("/files/toUse");
        if (!Files.exists(folder)) {
            throw new RuntimeException("Folder does not exist: " + folder.toAbsolutePath());
        }

        try (var files = Files.list(folder)) {
            var notebookFiles = files.filter(file -> file.toString().endsWith(".nbconvert.ipynb")).toList();
            if (notebookFiles.isEmpty()) {
                throw new RuntimeException("No Jupyter notebook file found in the folder.");
            }
            if (notebookFiles.size() > 1) {
                throw new RuntimeException("Multiple Jupyter notebook files found. Expected only one.");
            }
            return new PathResource(notebookFiles.get(0));
        } catch (IOException e) {
            throw new RuntimeException("Error while accessing the files.", e);
        }
    }

    public void upload(MultipartFile file) {
        try {
            Path folder = Paths.get("/files/toUse");
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            Path filePath = folder.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            System.out.println(filePath.toAbsolutePath());
            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to save Jupyter notebook file", e);
        }
    }


    public void saveBinary(MultipartFile binaryFile) {
        try {
            Path targetDirectory = Paths.get("/files/toUse");
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            // Check if the uploaded file is a ZIP file
            if (Objects.requireNonNull(binaryFile.getOriginalFilename()).endsWith(".zip")) {
                try (ZipInputStream zipInputStream = new ZipInputStream(binaryFile.getInputStream())) {
                    ZipEntry entry;
                    while ((entry = zipInputStream.getNextEntry()) != null) {
                        Path extractedFile = targetDirectory.resolve(entry.getName());
                        if (entry.isDirectory()) {
                            Files.createDirectories(extractedFile);
                        } else {
                            Files.copy(zipInputStream, extractedFile, StandardCopyOption.REPLACE_EXISTING);
                        }
                        zipInputStream.closeEntry();
                    }
                }
            } else {
                Path targetFile = targetDirectory.resolve(Objects.requireNonNull(binaryFile.getOriginalFilename()));
                binaryFile.transferTo(targetFile.toFile());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save or extract the binary file", e);
        }
    }

    public void executeNoteBook() {
        startPythonEnvironment();
        Path folder = Paths.get("/files/toUse");
        System.out.println(folder.toAbsolutePath());
        if (Files.exists(folder)) {
            try (var files = Files.list(folder)) {
                files.filter(file -> file.toString().endsWith(".ipynb")).forEach(file -> {
                    System.out.println("Executing notebook: " + file.getFileName());
                    try {
//                        ProcessBuilder pb = new ProcessBuilder("jupyter", "--execute", "notebook", file.toString());
                        ProcessBuilder pb = new ProcessBuilder("jupyter", "nbconvert", "--execute", "--to", "notebook", file.toString());
                        pb.inheritIO();
                        Process process = pb.start();
                        int exitCode = process.waitFor();
                        if (exitCode != 0) {
                            // Log that execution did not complete successfully
                            log.error("Notebook execution exited with code: {}", exitCode);

                            // Capture the error stream (or optionally the standard output)
                            String errorOutput = IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8);


                            // Save the file containing the error details (you can choose an appropriate path/name)
                            Files.write(file.toAbsolutePath(), errorOutput.getBytes());
                            log.info("Error notebook saved at: {}", file.toAbsolutePath());
                        }

                    } catch (IOException | InterruptedException e) {
                        System.out.println("could not find file " +  file.getFileName() + " at location " + file.toAbsolutePath());
                        System.out.println("files found " +  file.getFileName());
                        throw new RuntimeException("Error while executing notebook: " + file.getFileName(), e);

                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Failed to list files in the folder", e);
            }
        } else {
            throw new RuntimeException("Folder does not exist: " + folder.toAbsolutePath());
        }
    }

    public void startPythonEnvironment() {
        try {
            // This calls "python -m venv myenv" to create a virtual environment named "myenv"
            ProcessBuilder builder = new ProcessBuilder("python", "-m", "venv", "myenv");
            builder.inheritIO();
            Process process = builder.start();

            // Optionally wait for the process to complete:
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Failed to create Python environment. Exit code: " + exitCode);
            }
            // At this point, you could also source the environment or run additional Python commands.
        } catch (IOException | InterruptedException e) {
            log.info(Arrays.toString(e.getStackTrace()));
        }
    }


    public void deleteAll() {
        Path folder = Paths.get("/files/toUse");
        if (Files.exists(folder)) {
            try (var files = Files.list(folder)) {
                files.forEach(file -> {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete file: " + file, e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete files in the folder", e);
            }
        }
    }
}
