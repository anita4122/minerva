package io.ansan.minc.ast.stmt;

import io.ansan.minc.ast.INode.IStmtNode;
import io.ansan.minc.token.Token;

import java.util.List;

public record BlockNode(Token start, List<IStmtNode> statements, Token end) implements IStmtNode {

  @Override
  public AstNode get_node_type() {
    return AstNode.STATEMENT_BLOCK;
  }
}
