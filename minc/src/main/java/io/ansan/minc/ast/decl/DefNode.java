package io.ansan.minc.ast.decl;

import java.util.List;

import io.ansan.minc.ast.INode.IDeclNode;
import io.ansan.minc.ast.IType;
import io.ansan.minc.ast.stmt.BlockNode;
import io.ansan.minc.token.Token;

public record DefNode(Token ex, FunctionDefinition function_definition, BlockNode block) implements IDeclNode {

  @Override
  public AstNode get_node_type() {
    return AstNode.DEF_DECLARATION;
  }

  public record FunctionDefinition(Token ident, Token name, List<Parameter> parameters, IType return_type) {
  }

  public record Parameter(Token ident, IType type) {}
}
