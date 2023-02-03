package io.ansan.minc.ast.decl;

import io.ansan.minc.ast.INode.IDeclNode;
import io.ansan.minc.ast.IType;
import io.ansan.minc.token.Token;

import java.util.List;

public record StructNode(Token ex, Token ident, Token name, Token parent, Token start, List<StructFieldNode> fields, Token end) implements IDeclNode {

  @Override
  public AstNode get_node_type() {
    return AstNode.STRUCT_DECLARATION;
  }

  public record StructFieldNode(Token ident, IType type) {}
}
