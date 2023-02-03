package io.ansan.minc.ast.decl;

import io.ansan.minc.ast.INode.IDeclNode;
import io.ansan.minc.ast.IType;
import io.ansan.minc.token.Token;

import java.util.List;

public record EnumNode(Token ex, Token ident, Token name, List<EnumMember> members) implements IDeclNode {

  @Override
  public AstNode get_node_type() {
    return AstNode.ENUM_DECLARATION;
  }

  public record EnumMember(Token ident, List<IType> types) {}
}
