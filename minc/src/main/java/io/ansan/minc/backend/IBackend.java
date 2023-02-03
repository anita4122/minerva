package io.ansan.minc.backend;

import io.ansan.minc.ast.INode;

public interface IBackend {
  String get_name();
  boolean code_gen(INode node);
}