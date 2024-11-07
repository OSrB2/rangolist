package com.pedrooliveira.rangolist.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class UploadUtil {
  private static final String UPLOAD_DIR = FileUpConfig.getUploadDir();

  public static String saveFile(MultipartFile file) throws IOException {
    String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
    Path uploadPath = Paths.get(UPLOAD_DIR);

    // Cria o diretório de upload, se não existir
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }
    // Define o caminho completo do arquivo
    Path filePath = uploadPath.resolve(filename);
    // Salva o arquivo no sistema de arquivos
    Files.copy(file.getInputStream(), filePath);
    // Retorna o caminho completo do arquivo salvo
    return filePath.toString();
  }
}
