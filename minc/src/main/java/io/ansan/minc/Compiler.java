package io.ansan.minc;

import io.ansan.minc.ast.FileNode;
import io.ansan.minc.parser.Lexer;
import io.ansan.minc.parser.Scanner;

import java.nio.file.Path;

public final class Compiler {

  private FileNode node;
  public final Path working_directory;
  public final String verion;

  public Compiler() {
    working_directory = Path.of(".");
    verion = "0.0.1";
  }

  public void compiler_raw_message(String msg) {
    System.out.printf("%s", msg);
  }


  public FileNode load_file(String target) {
    var node  = new FileNode();
    node.path = Path.of(target);
    node.name = node.path.getFileName().toString();
    new Scanner(node);
    new Lexer(this, node);

    return node;
  }
  private void lex_file(String target) {
    Lexer lexer = new Lexer(this, null);
  }
}
