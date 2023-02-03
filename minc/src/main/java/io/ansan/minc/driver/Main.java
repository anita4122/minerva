package io.ansan.minc.driver;

import io.ansan.minc.Compiler;
import io.ansan.minc.util.Message;

public class Main {

  public static void main(String[] args) {
    Compiler compiler = new Compiler();
    System.out.println(compiler.working_directory.toAbsolutePath().toString());
    System.out.println(compiler.verion);
    var file = compiler.load_file("minc/tests/lang/main.min");
    file.tokens.forEach(System.out::println);

  }
}
