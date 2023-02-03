package io.ansan.minc.ast.decl;

import io.ansan.minc.ast.INode.IDeclNode;
import io.ansan.minc.token.Token;
import io.ansan.minc.ast.decl.DefNode.FunctionDefinition;

import java.util.List;

public record InterfaceNode(Token ex, Token ident, Token name, List<Token> parents, List<FunctionDefinition> definitions) implements IDeclNode {

  @Override
  public AstNode get_node_type() {
    return AstNode.INTERFACE_DECLARATION;
  }
}
