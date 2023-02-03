package io.ansan.minc.ast;

import io.ansan.minc.token.Token;

public record UserType(Token ident) implements IType {

  @Override
  public TypeKind getTypeKind() {
    return TypeKind.USER;
  }
}
