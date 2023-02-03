package io.ansan.minc.parser;

import io.ansan.minc.ast.FileNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Scanner {

  public Scanner(FileNode node) {
    try {
      var buf = Files.readAllBytes(node.path);
      var buffer = ByteBuffer.wrap(buf);
      node.source = StandardCharsets.UTF_8.decode(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
