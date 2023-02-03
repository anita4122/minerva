package io.ansan.minc.ast;

import io.ansan.minc.token.Token;

import java.nio.CharBuffer;
import java.nio.file.Path;
import java.util.List;

public final class FileNode implements INode {
    public String name;
    public Path path;
    public List<INode> nodes;
    public CharBuffer source;
    public List<Token> tokens;
    public INode ast;


  @Override
  public AstNode get_node_type() {
    return null;
  }
}
