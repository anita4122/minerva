package io.ansan.minc.ast.expr;

import io.ansan.minc.ast.INode;
import io.ansan.minc.ast.INode.IExprNode;
import io.ansan.minc.token.Token;

public record ArithmeticNode(INode left, Token op, INode right, AstNode ast_kind) implements IExprNode {

  @Override
  public AstNode get_node_type() {
    return ast_kind;
  }
}
