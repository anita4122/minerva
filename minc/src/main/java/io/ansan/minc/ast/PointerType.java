package io.ansan.minc.ast;

import io.ansan.minc.token.Token;

public record PointerType(Token p_mark, Token ident) implements IType {

  @Override
  public TypeKind getTypeKind() {
    return TypeKind.POINTER;
  }
}
