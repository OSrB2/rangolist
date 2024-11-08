package com.pedrooliveira.rangolist.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class FileUpConfig {
  private static final String UPLOAD_DIR;

  static {
    Dotenv dotenv = Dotenv.load();
    UPLOAD_DIR = dotenv.get("UPLOAD_DIR");
  }

  public static String getUploadDir() {
    return UPLOAD_DIR;
  }
}
