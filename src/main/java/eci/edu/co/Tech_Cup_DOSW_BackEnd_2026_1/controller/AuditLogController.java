package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/audit/logs")
@Tag(name = "Audit", description = "Audit and logs")
public class AuditLogController {

    private final Path logDirectory;

    public AuditLogController(@Value("${app.audit.log-dir:logs}") String logDirectory) {
        Path configured = Paths.get(logDirectory);
        this.logDirectory = configured.isAbsolute() ? configured.normalize()
                : Paths.get(".").resolve(configured).normalize();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "List audit logs", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<List<Map<String, Object>>> listLogs() {
        try {
            if (!Files.exists(logDirectory)) {
                return ResponseEntity.ok(List.of());
            }
            List<Map<String, Object>> files = Files.list(logDirectory)
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            Map<String, Object> item = new LinkedHashMap<>();
                            item.put("name", path.getFileName().toString());
                            item.put("size", Files.size(path));
                            item.put("lastModified", Instant.ofEpochMilli(Files.getLastModifiedTime(path).toMillis()));
                            return item;
                        } catch (IOException e) {
                            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Unable to read log metadata", e);
                        }
                    })
                    .toList();
            return ResponseEntity.ok(files);
        } catch (IOException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Unable to list logs", e);
        }
    }

    @GetMapping("/{fileName}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','ORGANIZER')")
    @Operation(summary = "Read audit log", description = "Allowed roles: ADMINISTRATOR, ORGANIZER")
    public ResponseEntity<Map<String, Object>> readLog(
            @Parameter(required = true) @PathVariable String fileName,
            @RequestParam(defaultValue = "200") int lines) {
        if (lines <= 0 || lines > 2000) {
            throw new ResponseStatusException(BAD_REQUEST, "lines must be between 1 and 2000");
        }

        Path file = resolveSafeFile(fileName);
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new ResponseStatusException(NOT_FOUND, "Log file not found");
        }

        try {
            List<String> content = tail(file, lines);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("file", file.getFileName().toString());
            response.put("lines", content.size());
            response.put("content", content);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Unable to read log file", e);
        }
    }

    @DeleteMapping("/{fileName}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete audit log", description = "Allowed roles: ADMINISTRATOR")
    public ResponseEntity<Void> deleteLog(@Parameter(required = true) @PathVariable String fileName) {
        Path file = resolveSafeFile(fileName);
        try {
            if (!Files.exists(file) || !Files.isRegularFile(file)) {
                throw new ResponseStatusException(NOT_FOUND, "Log file not found");
            }
            Files.delete(file);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Unable to delete log file", e);
        }
    }

    private Path resolveSafeFile(String fileName) {
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid file name");
        }
        return logDirectory.resolve(fileName).normalize();
    }

    private List<String> tail(Path file, int maxLines) throws IOException {
        ArrayDeque<String> queue = new ArrayDeque<>(maxLines);
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (queue.size() == maxLines) {
                    queue.removeFirst();
                }
                queue.addLast(line);
            }
        }
        return List.copyOf(queue);
    }
}
