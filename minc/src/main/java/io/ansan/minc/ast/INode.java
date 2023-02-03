package io.ansan.minc.ast;

public interface INode {

  interface IExprNode  extends INode {}
  interface IStmtNode  extends INode {}
  interface IDeclNode  extends INode {}
  interface IBasicNode extends INode {}

  AstNode get_node_type();

  enum AstNode {

    DECLARATION_START(null),
      DEF_DECLARATION("DEF_DECLARATION"),
      ENUM_DECLARATION("ENUM_DECLARATION"),
      INTERFACE_DECLARATION("INTERFACE_DECLARATION"),
      PACKAGE_DECLARATION("PACKAGE_DECLARATION"),
      USE_DECLARATION("USE_DECLARATION"),
      STRUCT_DECLARATION("STRUCT_DECLARATION"),
    DECLARATION_END(null),

    STATEMENT_START(null),
      STATEMENT_BLOCK("BLOCK_STATEMENT"),
      STATEMENT_WITH("WITH_STATEMENT"), 
      STATEMENT_DEFER("DEFER_STATEMENT"),
    STATEMENT_END(null),

    BASIC_START(null),
      BASIC_STRING("STRING_BASIC"),
      BASIC_NUMBER("NUMBER_BASIC"),
      BASIC_IDENT("IDENT_BASIC"),
      BASIC_TYPE("TYPE_BASIC"),
    BASIC_END(null)
    ;

    public final String name;

    AstNode(String name) {
      this.name = name;
    }
  }

}
