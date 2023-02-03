package io.ansan.minc.backend.llvm;

import io.ansan.minc.ast.INode;
import io.ansan.minc.backend.IBackend;

public class LLVMBackend implements IBackend {

  @Override
  public String get_name() {
    return "llvm";
  }

  @Override
  public boolean code_gen(INode node) {
    return false;
  }
}