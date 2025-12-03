package com.example.InvProject.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductImageController {

  private final String uploadDir = System.getProperty("user.dir") + "/backend/uploads/";

  @PostMapping("/{id}/image")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<?> upload(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception {
    if (file.isEmpty()) return ResponseEntity.badRequest().body("No file");
    String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
    String name = "p" + id + "-" + UUID.randomUUID() + (ext != null ? "." + ext : "");
    Files.createDirectories(Path.of(uploadDir));
    Path dest = Path.of(uploadDir + name);
    file.transferTo(dest.toFile());

    // Persist imageUrl to Product (pseudo—adapt to your service/repo)
    String imageUrl = "/uploads/" + name;
    // productService.updateImageUrl(id, imageUrl);

    return ResponseEntity.ok().body(imageUrl);
  }
}
