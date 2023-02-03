
package io.ansan.minc.ast.basic;

import io.ansan.minc.ast.INode.IBasicNode;
import io.ansan.minc.token.Token;

public record NumberNode(Token ident) implements IBasicNode {
  
  @Override
  public AstNode get_node_type() {
    return AstNode.BASIC_NUMBER;
  }

}
