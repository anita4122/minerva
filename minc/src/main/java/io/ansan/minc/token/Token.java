package io.ansan.minc.token;

public record Token(String lexme, Kind kind, Position pos) {

  public Token(Kind kind, Position pos) {
    this(kind.lexme, kind, pos);
  }

  public boolean is_keyword() {
    return Kind.BG_KW.ordinal() < kind.ordinal() && kind.ordinal() < Kind.END_KW.ordinal();
  }

  public boolean is_primitive() {
    return Kind.PRIMITIVE_BEGIN.ordinal() < kind.ordinal() && kind.ordinal() < Kind.PRIMITIVE_END.ordinal();
  }

  public boolean is_reserved() {
    return Kind.RESERVED_BEGIN.ordinal() < kind.ordinal() && kind.ordinal() < Kind.PRIMITIVE_END.ordinal();
  }

  public enum Kind {
    EOF("\0", "EOF", 0),
    EOL("\n", "EOL", 0),
    SPACE("SPACE", 0),
    TAB("TAB", 0),
    INVALID("invalid", 0),

    //Scope
    SCOPE_BEGIN(null, -1),
    OPEN_PARAN("(", "OPEN_PARAN", 0), // (
    CLOSE_PARAN(")", "CLOSE_PARAN", 0), // )
    OPEN_BRACKET("[", "OPEN_BRACKET", 0), // [
    CLOSE_BRACKET("]", "CLOSE_BRACKET", 0), // ]
    OPEN_BRACE("{", "OPEN_BRACE", 0), // {
    CLOSE_BRACE("}", "CLOSE_BRACE", 0), // }
    SCOPE_END(null, -1),

    //Operator
    OP_BEGIN(null, -1),

    UNARY_BEGIN(null, -1),
    INCREMENT("++", "increment", 0), // ++
    DECREMENT("--", "decrement", 0), // --
    UNARY_END(null, -1),

    ARITH_BEGIN(null, -1),
    ADD("+", "ADD", 0), // +
    SUBTRACT("-", "SUBTRACT", 0), // -
    DIVIDE("/", "DIVIDE", 0), // /
    MODULO("%", "MODULO", 0), // %
    MULTIPLY("*", "MULTIPLY", 0), // *
    ARITH_END(null, -0),

    RELAT_BEGIN(null, -1),
    LESS_THAN("<", "LESS_THAN", 0), // <
    LESS_THAN_EQ("<=", "LESS_THAN_EQUAL", 0), // <=
    GREATER_THAN(">", "GREATER_THAN", 0), // >
    GREATER_THAN_EQ(">=", "GREATER_THAN_EQUAL", 0), // >=
    NOT_EQ("!=", "NOT_EQUAL", 0), // !=
    EQUAL("==", "EQUAL", 0), // ==
    RELAT_END(null, -1),

    LOGICAL_BEGIN(null, -1),
    NOT("!", "NOT", 0), // !
    OR("||", "OR", 0), // ||
    AND("&&", "AND", 0), // &&
    LOGICAL_END(null, -1),

    BIT_BEGIN(null, -1),
    BIT_NOT("~", "BIT_NOT", 0), // ~
    BIT_AND("&", "BIT_AND", 0), // &
    BIT_OR("|", "BIT_OR", 0), // |
    SHIFT_LEFT("<<", "SHIFT_LEFT", 0), // <<
    SHIFT_RIGHT(">>", "SHIFT_RIGHT", 0), // >>
    CARROT("^", "CARROT", 0), // ^
    BIT_END(null, -1),

    ASSIGN_BEGIN(null, -1),
    ASSIGN("=", "ASSIGN", 0), // =
    ASSIGN_INFER(":=", "ASSIGN_INFER", 0), // =
    ASSIGN_END(null, -1),
    OP_END(null, -1),

    //Special
    SPECIAL_BEGIN(null, -1),
    MACRO("@", "MACRO", 0), // @
    POUND("#", "POUND", 0), // #
    RANGE("..", "RANGE", 0), // ..
    PERIOD(".", "PERIOD", 0), // .
    COMMA(",", "COMMA", 0), // ,
    SINGLE_QUOTE("'", "SINGLE_QUOTE", 0), // '
    DOUBLE_QUOTE("\"", "DOUBLE_QUOTE", 0), // "
    RIGHT_ARROW("->", "RIGHT_ARROW", 0), // ->
    LEFT_ARROW("<-", "LEFT_ARROW", 0), // <-
    COMMENT("COMMENT", 0),
    COLON(":", "COLON", 0), // :
    SEMICOLON(";", "SEMICOLON", 0), // ;
    SPECIAL_END(null, -1),
    //Rersrved

    RESERVED_BEGIN(null, 0),
    GOTO("goto", 0), // goto
    SUPER("super", 0), // super
    NEW("new", 0), // NEW
    MATCH("match", 0), // match
    RESERVED_END(null, -1),

    BG_KW(null, -1),
    //Keyword
    PKG("pkg", 0), // pkg
    USE("use", 0), // use
    DEF("def", 0), // def
    MAIN("main", 0), // main
    MUT("mut", 0), // mut
    STRUCT("struct", 0), // struct
    CLASS("class", 0), // class
    INTERFACE("interface", 0), // interface
    DEFER("defer", 0), // defer
    RETURN("return", 0), // return
    FOR("for", 0), // for
    LOOP("loop", 0), // loop
    BREAK("break", 0), // break
    FALL("fall", 0), // fall
    IF("if", 0), // if
    TRUE("true", 0), // true
    FALSE("false", 0), // false
    EXPORT("ex", 0), // ex
    ASSERT("assert", 0), // assert
    AS("as", 0), // as
    ALIAS("alias", 0),
    ELSE("else", 0), // else
    WITH("with", 0), // with
    ENUM("enum", 0), // enum
    SELF("self", 0), // self
    END_KW(null, 0),

    //Type
    PRIMITIVE_BEGIN(null, -1),
    VOID("void", 0), // void
    BOOL("bool", 0), // bool
    CHAR("char", 0), // char
    STRING("string", 0), // string
    RUNE("rune", 0), // rune
    U8("u8", 0), // u8
    I8("i8", 0), // i8
    U16("u16", 0), // u16
    I16("i16", 0), // I16
    U32("u32", 0), // u32
    I32("i32", 0), // I32
    U64("u64", 0), // u64
    I64("i64", 0), // I64
    F32("f32", 0), // f32
    F64("f64", 0), // f64
    SIZE("size", 0), // size
    NIL("nil", 0), // nil
    PRIMITIVE_END(null, 0),

    BEGIN_LIT(null, 0),
    IDENT(null, "ident", 0),
    NUMBER(null, "number", 0),
    END_LIT(null, 0),
    ;

    public final String lexme;
    public final String name;
    public final int precedence;

    Kind(String lexme, String name, int precedence) {
      this.lexme = lexme;
      this.name = name;
      this.precedence = precedence;
    }

    Kind(String lexme, int precedence) {
      this.lexme = lexme;
      this.name = lexme;
      this.precedence = precedence;
    }

    @Override
    public String toString() {
      return "Kind{" +
          "lexme='" + lexme + '\'' +
          ", name='" + name + '\'' +
          ", precedence=" + precedence +
          '}';
    }
  }

  @Override
  public String toString() {
    return "Token{" +
        "lexme='" + lexme + '\'' +
        ", kind=" + kind.toString() +
        ", pos=" + pos +
        '}';
  }
}
