package io.ansan.minc.ast.expr;

import io.ansan.minc.ast.INode;
import io.ansan.minc.ast.INode.IExprNode;
import io.ansan.minc.ast.IType;
import io.ansan.minc.token.Token;

import java.util.List;

public record AssignNode(Token mutablity, List<IType> idents, Token assign, List<INode> assignment) implements IExprNode {

  @Override
  public AstNode get_node_type() {
    //TODO(anita): Implement
    return null;
  }
}
