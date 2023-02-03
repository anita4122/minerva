package io.ansan.minc.ast;

public record InferredType() implements IType {
  @Override
  public TypeKind getTypeKind() {
    return TypeKind.INFERRED;
  }
}
