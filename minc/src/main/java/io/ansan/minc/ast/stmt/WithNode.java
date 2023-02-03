package io.ansan.minc.ast.stmt;

import io.ansan.minc.ast.IType;
import io.ansan.minc.ast.INode.IStmtNode;
import io.ansan.minc.token.Token;

public record WithNode(Token ident, Token name, IType pointer) implements IStmtNode {

  @Override
  public AstNode get_node_type() {
    return AstNode.STATEMENT_WITH;
  }
}
