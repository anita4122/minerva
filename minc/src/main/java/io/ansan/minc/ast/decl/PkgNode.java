package io.ansan.minc.ast.decl;

import io.ansan.minc.ast.INode.IDeclNode;
import io.ansan.minc.token.Token;

public record PkgNode(Token ident, Token value) implements IDeclNode {

  @Override
  public AstNode get_node_type() {
    return AstNode.PACKAGE_DECLARATION;
  }
}
