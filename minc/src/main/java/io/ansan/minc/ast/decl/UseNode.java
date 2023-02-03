package io.ansan.minc.ast.decl;


import io.ansan.minc.ast.INode.IDeclNode;
import io.ansan.minc.token.Token;

import java.util.List;

public record UseNode(Token ident, String path, List<Token> tokens) implements IDeclNode {

  @Override
  public AstNode get_node_type() {
    return AstNode.USE_DECLARATION;
  }

}
