package dev.seanboaden.hls.filesystem.controller;

import java.nio.file.Path;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.filesystem.model.FolderNode;
import dev.seanboaden.hls.filesystem.service.FileSystemService;
import dev.seanboaden.hls.user.model.Role;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/filesystem")
@Tag(name = "FileSystem", description = "Read from file system")
public class FileSystemController {
  @Autowired
  private FileSystemService fileSystemService;

  @PreAuthorize(Role.PreAuthorized.ADMIN)
  @GetMapping("/folders")
  public ResponseEntity<LinkedList<FolderNode>> getFolders(@RequestParam(required = false) String path) {
    if (path == null)
      return ResponseEntity.ok(new LinkedList<FolderNode>());

    return ResponseEntity
        .ok(this.fileSystemService.listFoldersUnderMediaRoot(Path.of(path)));
  }
}
