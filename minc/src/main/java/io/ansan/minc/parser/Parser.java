package io.ansan.minc.parser;

import static io.ansan.minc.token.Token.Kind.*;

import io.ansan.minc.Compiler;
import io.ansan.minc.ast.BuiltinType;
import io.ansan.minc.ast.FileNode;
import io.ansan.minc.ast.INode;
import io.ansan.minc.ast.INode.IDeclNode;
import io.ansan.minc.ast.INode.IStmtNode;
import io.ansan.minc.ast.INode.IExprNode;
import io.ansan.minc.ast.IType;
import io.ansan.minc.ast.basic.IdentNode;
import io.ansan.minc.ast.basic.NumberNode;
import io.ansan.minc.ast.basic.StringNode;
import io.ansan.minc.ast.PointerType;
import io.ansan.minc.ast.UserType;
import io.ansan.minc.ast.decl.DefNode;
import io.ansan.minc.ast.decl.DefNode.FunctionDefinition;
import io.ansan.minc.ast.decl.DefNode.Parameter;
import io.ansan.minc.ast.decl.EnumNode;
import io.ansan.minc.ast.decl.EnumNode.EnumMember;
import io.ansan.minc.ast.decl.InterfaceNode;
import io.ansan.minc.ast.decl.PkgNode;
import io.ansan.minc.ast.decl.StructNode;
import io.ansan.minc.ast.decl.StructNode.StructFieldNode;
import io.ansan.minc.ast.expr.ArithmeticNode;
import io.ansan.minc.ast.expr.AssignNode;
import io.ansan.minc.ast.decl.UseNode;
import io.ansan.minc.ast.stmt.BlockNode;
import io.ansan.minc.ast.stmt.DeferNode;
import io.ansan.minc.ast.stmt.WithNode;
import io.ansan.minc.token.Token;
import io.ansan.minc.token.Token.Kind;
import io.ansan.minc.util.Message;

import java.util.ArrayList;
import java.util.Objects;

public final class Parser {

  public  final FileNode node;
  private final Compiler compiler;
  private int   idx;

  public Parser(Compiler compiler, FileNode node) {
    this.compiler = compiler;
    this.node     = node;
    this.idx      = 0;
  }

  // TODO(anita): implement
  private IStmtNode parse_stmt() {
    return null;
  }

  private IExprNode parse_expr() {
    return null;
  }

  // TODO(anita): add classes to the parser
  private IDeclNode parse_decl() {
    switch (peek().kind()) {
    case PKG:
      return pkg_parse();
    case USE:
      return use_parse();
    case DEF:
      return def_parse(false);
    case STRUCT:
      return struct_parse(false);
    case INTERFACE:
      return interface_parse(false);
    case EXPORT: {
      switch (peek(1).kind()) {
      case DEF:
        return def_parse(true);
      case STRUCT:
        return struct_parse(true);
      case INTERFACE:
        return interface_parse(true);
      default:
        log_error("Illegal token after export " + peek());
        return null;

      }
    }
    default:
      log_error("Illegal token found in declaration position" + peek());
      return null;
    }
  }

  private PkgNode pkg_parse() {
    var ident = consume(PKG);
    var name  = consume(IDENT);
    return new PkgNode(ident, name);
  }

  private UseNode use_parse() {
    var ident       = consume(USE);
    var path        = new StringBuilder();
    var token_stream = new ArrayList<Token>();
    for (;;) {
      path.append(Objects.requireNonNull(consume(IDENT)).lexme());
      if (match(PERIOD)) {
        advance();
      } else if (match(EOL)) {
        break;
      } else {
        log_error("Illegal token found in use expected a period or an EOL of a {" + peek().toString() + "} instead!");
      }
    }
    return new UseNode(ident, path.toString(), token_stream);
  }

  private StructNode struct_parse(boolean is_public) {
    Token ex = null;
    if (is_public) {
      ex = consume(EXPORT);
    }
    var ident     = consume(STRUCT);
    var name      = consume(IDENT);
    Token parent  = null;

    if (match(COLON)) {
      consume(COLON);
      parent = consume(IDENT);
    }

    var start   = consume(OPEN_BRACE);
    var fields  = new ArrayList<StructFieldNode>();

    for (;;) {
      fields.add(struct_field_node());
      if (match(CLOSE_BRACE)) {
        break;
      } else if (match(EOL)) {
        consume(EOL);
      } else {
        log_error("Illegal token at " + peek().toString());
      }
    }
    var end = consume(CLOSE_BRACE);
    return new StructNode(ex, ident, name, parent, start, fields, end);
  }

  private StructFieldNode struct_field_node() {
    var ident = consume(IDENT);
    var type  = consume_type();
    return new StructFieldNode(ident, type);
  }

  private EnumNode enum_parse(boolean is_public) {
    Token ex = null;
    if (is_public) {
      ex = consume(EXPORT);
    }
    var ident   = consume(ENUM);
    var name    = consume(IDENT);
    var members = new ArrayList<EnumNode.EnumMember>();

    consume(OPEN_BRACE);

    while (!match(CLOSE_BRACE)) {
      var member = enum_member();
      members.add(member);
      consume(EOL);
    }
    return new EnumNode(ex, ident, name, members);
  }

  private EnumMember enum_member() {
    var ident = consume(IDENT);
    var types = new ArrayList<IType>();
    if (match(OPEN_PARAN)) {
      consume(OPEN_PARAN);
      for (;;) {
        var type = consume_type();
        types.add(type);
        if (match(COMMA)) {
          consume(COMMA);
        }

        if (match(CLOSE_PARAN)) {
          consume(CLOSE_PARAN);
          break;
        }
      }
    } else if (!match(EOL)) {
      log_error("Illegal token " + peek().toString() + "expected an EOL");
    } else {
      log_error("How did we get here? Illegal token " + peek().toString() + " in enum member");
      System.exit(-1);
    }
    return new EnumMember(ident, types);
  }

  private DefNode def_parse(boolean is_public) {
    Token ex = null;
    if (match(EXPORT)) {
      ex = consume(EXPORT);
    }
    var func_def  = def_definition();
    var block     = block_parse();
    return new DefNode(ex, func_def, block);
  }

  private FunctionDefinition def_definition() {
    var ident = consume(DEF);
    var name  = consume(IDENT);
    var parameters = new ArrayList<Parameter>();

    if (match(OPEN_PARAN)) {
      consume(OPEN_PARAN);
      for (;;) {
        var param = def_parameter();
        parameters.add(param);
        if (match(CLOSE_PARAN)) {
          consume(CLOSE_PARAN);
          break;
        } else if (match(COMMA)) {
          consume(COMMA);
        } else {
          log_error("How did we get here? illegal token " + peek().toString() + " in parameter");
        }
      }
    }

    var return_type = consume_type();

    return new FunctionDefinition(ident, name, parameters, return_type);
  }

  private Parameter def_parameter() {
    var ident = consume(IDENT);
    consume(COLON);
    var type = consume_type();
    return new Parameter(ident, type);
  }

  private InterfaceNode interface_parse(boolean is_public) {
    Token ex = null;
    if (is_public) {
      ex = consume(EXPORT);
    }
    var ident       = consume(INTERFACE);
    var name        = consume(IDENT);
    var parents     = new ArrayList<Token>();
    var definitions = new ArrayList<FunctionDefinition>();

    if (match(COLON)) {
      consume(COLON);
      for (;;) {
        var parent = consume(IDENT);
        parents.add(parent);
        if (match(COMMA)) {
          consume(COMMA);
        } else if (match(EOL)) {
          break;
        } else {
          log_error("Issue reading parent token " + peek().toString());
        }
      }
    } else if (match(OPEN_BRACE)) {
      consume(OPEN_BRACE);
      consume(EOL);

      while (!match(CLOSE_PARAN)) {
        var def = def_definition();
        definitions.add(def);
        consume(EOL);
      }
      consume(CLOSE_BRACE);
    }

    return new InterfaceNode(ex, ident, name, parents, definitions);
  }

  /** Statement Parser **/

  private WithNode with_parse() {
    var ident = consume(WITH);
    var name  = consume(IDENT);
    consume(COLON);
    var ptr = consume_type();
    assert ptr != null;
    if (ptr.getTypeKind() != IType.TypeKind.POINTER) {
      log_error("With must return with a pointer type " + peek(-1).toString());
    }
    return new WithNode(ident, name, ptr);
  }

  private DeferNode defer_parse() {
    var ident = consume(DEFER);
    BlockNode blk_node = null;
    if (!match(OPEN_BRACE)) {
      //TODO(anita): do a match for a call node
    } else if (match(OPEN_BRACE)) {
      blk_node = block_parse();
    } else {
      log_error("illegal parse on defer");
    }
    return new DeferNode(ident, blk_node);
  }

  private BlockNode block_parse() {
    var start = consume(OPEN_BRACE);
    var stmt_list = new ArrayList<IStmtNode>();
    while (match(CLOSE_BRACE)) {
      stmt_list.add(parse_stmt());
    }
    var end = consume(OPEN_BRACE);
    return new BlockNode(start, stmt_list, end);
  }


  /* Expressions */

  private AssignNode assign_parse() {
    Token mut     = null;
    var type_list = new ArrayList<IType>();
    if (match(MUT)) {
      mut = consume(MUT);
    }

    while (!match(ASSIGN)) {
      type_list.add(consume_type());
    }

    var assign      = consume(ASSIGN);
    var assign_list = new ArrayList<INode>();

    return new AssignNode(mut, type_list, assign, assign_list);
  }

  /** Arithmetic parser **/

  private ArithmeticNode arithmetic_node_parse() {

    return null;
  }

  /* Other Parser */

  private NumberNode number_node_parse() {
    return new NumberNode(consume(NUMBER));
  }

  private StringNode string_node_parse() {
    return new StringNode(consume(STRING));
  }

  private IdentNode ident_node_parse() {
    return new IdentNode(consume(IDENT));
  }

  private IType consume_type() {
    if (is_primitive()) {
      var current_tok = peek();
      advance();
      return new BuiltinType(current_tok);
    } else if (match(IDENT)) {
      return new UserType(consume(IDENT));
    } else if (match(CARROT)) {
      var marker = consume(CARROT);
      var ident = consume(IDENT);
      // Check to see if the pointer semantics are correct
      assert marker != null : "How did this becomes null?";
      var mark_pos = marker.pos();

      assert ident != null : "How did this become null";
      var ident_pos = ident.pos();

      if ((ident_pos.offset_start() - mark_pos.offset_start()) != 1) {
        log_error("Pointer must be next to Type identifier");
      }
      return new PointerType(marker, ident);
    } else {
      log_error("No valid type found found " + peek().toString() + "instead");
    }
    return null;
  }

  private boolean is_comparison() {
    return is_comparison(0);
  }

  private boolean is_comparison(int n) {
    return switch (peek(n).kind()) {
      case GREATER_THAN, GREATER_THAN_EQ, LESS_THAN, LESS_THAN_EQ, EQUAL, NOT_EQ, OR -> true;
      default -> false;
    };
  }

  private boolean is_assignment() {
    return is_assignment(0);
  }
  private boolean is_assignment(int n) {
    return switch (peek(n).kind()) {
      case ASSIGN, ASSIGN_INFER -> true;
      default -> false;
    };
  }

  private boolean is_arithmetic() {
    return is_arithmetic(0);
  }

  private boolean is_arithmetic(int n) {
    return switch (peek(n).kind()) {
      case ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO -> true;
      default -> false;
    };
  }

  private boolean is_logical() {
    return is_logical(0);
  }

  private boolean is_logical(int n) {
    return switch (peek(n).kind()) {
      case AND, OR, NOT -> true;
      default -> false;
    };
  }

  private boolean is_bitwise() {
    return is_bitwise(0);
  }

  private boolean is_bitwise(int n) {
    return switch (peek(n).kind()) {
      case NOT, BIT_AND, BIT_NOT, CARROT, SHIFT_LEFT, SHIFT_RIGHT -> true;
      default -> false;
    };
  }

  public enum Associative {
    RIGHT_TO_LEFT,
    LEFT_TO_RIGHT,
  }

  private Associative get_associativity(int n) {
    return switch (peek(n).kind()) {
      case INCREMENT, DECREMENT, ASSIGN, ASSIGN_INFER -> Associative.RIGHT_TO_LEFT;
      case MULTIPLY, DIVIDE, MODULO, ADD, SUBTRACT, GREATER_THAN, GREATER_THAN_EQ, LESS_THAN, LESS_THAN_EQ,
        EQUAL, NOT_EQ, AND, OR, SHIFT_LEFT, SHIFT_RIGHT, BIT_AND, CARROT, BIT_OR, BIT_NOT-> Associative.LEFT_TO_RIGHT;
      default -> throw new IllegalStateException("Unexpected value: " + peek(n).kind());
    };
  }

  private Associative get_associativity() {
    return get_associativity(0);
  }

  private void advance() {
    advance(1);
  }

  private void advance(int n) {
    this.idx = this.idx + n;
  }

  private Token peek(int n) {
    return node.tokens.get(idx + n);
  }

  private Token peek() {
    while (match(SPACE) || match(TAB)) {
      advance();
    }
    return peek(0);
  }

  private Token peek_raw() {
    return peek(0);
  }
  private boolean match(int n, Kind kind) {
    return peek(n).kind() == kind;
  }

  private boolean match(Kind kind) {
    return match(0, kind);
  }

  private Token consume(Kind kind) {
    if (match(kind)) {
      return peek();
    }
    log_error("Type mismatch on token " + peek().lexme() + " expected kind " + kind.lexme);
    return null;
  }

  private boolean is_primitive() {
    return peek().is_primitive();
  }

  private void log_error(String msg) {
    var base_msg = String.format("%sParse Error: %s%s\n", Message.Color.CYAN.regular, msg, Message.Color.RESET.regular);
    //compiler.compiler_raw_message(base_msg);
    System.exit(-1);
  }
}
