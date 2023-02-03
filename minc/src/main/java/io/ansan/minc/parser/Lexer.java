package io.ansan.minc.parser;

import io.ansan.minc.Compiler;
import io.ansan.minc.ast.FileNode;
import io.ansan.minc.token.Position;
import io.ansan.minc.token.Token;
import io.ansan.minc.token.Token.Kind;

import java.util.ArrayList;

public class Lexer {

  private final Compiler compiler;
  private final FileNode node;
  int idx     = 0;
  int line    = 1;
  int column  = 1;

  public Lexer(Compiler compiler, FileNode node) {
    this.compiler = compiler;
    this.node     = node;
    node.tokens = new ArrayList<>();

    while (lexToken().kind() != Token.Kind.EOL){
      node.tokens.add(lexToken());
      this.advance();
    }
  }

  private Token lexToken() {
    var start = idx;
    return switch (peek()) {
      case '\0' -> addToken(Kind.EOF, start, idx);
      case '\n', '\r' -> addToken(Kind.EOL, start, idx);
      case ' ' -> addToken(Kind.SPACE, start, idx);
      case '\t'  -> addToken(Kind.TAB, start, idx);
      case '(' -> addToken(Kind.OPEN_PARAN,    start, idx);
      case ')' -> addToken(Kind.CLOSE_PARAN,   start, idx);
      case '[' -> addToken(Kind.OPEN_BRACKET,  start, idx);
      case ']' -> addToken(Kind.CLOSE_BRACKET, start, idx);
      case '}' -> addToken(Kind.OPEN_BRACE,    start, idx);
      case '{' -> addToken(Kind.CLOSE_BRACE,   start, idx);
      case '+' -> lexPlus(start);
      case '-' -> lexMinus(start);
      case '/' -> lexDiv(start);
      case '%' -> addToken(Kind.MODULO,    start, idx);
      case '*' -> addToken(Kind.MULTIPLY,  start, idx);
      case '<' -> lexLess(start);
      case '>' -> lexGreat(start);
      case '!' -> lexBang(start);
      case '=' -> lexEq(start);
      case '|' -> lexPipe(start);
      case '~' -> addToken(Kind.BIT_NOT, start, idx);
      case '&' -> lexAnd(start);
      case '^' -> addToken(Kind.CARROT, start, idx);
      case ':' -> lexColon(start);
      case '@' -> addToken(Kind.MACRO, start, idx);
      case '#' -> addToken(Kind.POUND, start, idx);
      case '.' -> lexDot(start);
      case ',' -> addToken(Kind.COMMA, start, idx);
      case '\'' -> addToken(Kind.SINGLE_QUOTE, start, idx);
      case '"' -> addToken(Kind.DOUBLE_QUOTE, start, idx);
      default -> numbersAndIdents(start);
    };
  }

  private Token lexPlus(int start) {
    if (match('+')) {
      advance();
      return addToken(Kind.INCREMENT, start, idx);
    } else {
      return addToken(Kind.ADD, start, idx);
    }
  }

  private Token lexMinus(int start) {
    if (match('-')) {
      advance();
      return addToken(Kind.DECREMENT, start, idx);
    } else if (match('>')) {
      advance();
      return addToken(Kind.RIGHT_ARROW, start, idx);
    } else {
      return addToken(Kind.SUBTRACT, start, idx);
    }
  }

  private Token lexLess(int start) {
    if (match('=')) {
      advance();
      return addToken(Kind.LESS_THAN_EQ, start, idx);
    } else if (match('<')) {
      advance();
      return addToken(Kind.SHIFT_LEFT, start, idx);
    } else if (match('-')) {
      advance();
      return addToken(Kind.LEFT_ARROW, start, idx);
    } else {
      return addToken(Kind.LESS_THAN, start, idx);
    }
  }

  private Token lexGreat(int start) {
    if (match('=')) {
      advance();
      return addToken(Kind.GREATER_THAN_EQ, start, idx);
    } else if (match('>')) {
      advance();
      return addToken(Kind.SHIFT_LEFT, start, idx);
    } else {
      return addToken(Kind.LESS_THAN, start, idx);
    }
  }

  private Token lexPipe(int start) {
    if (match('|')) {
      advance();
      return addToken(Kind.OR, start, idx);
    } else {
      return addToken(Kind.BIT_OR, start, idx);
    }
  }

  private Token lexBang(int start) {
    if (match('=')) {
      advance();
      return addToken(Kind.NOT_EQ, start, idx);
    } else {
      return addToken(Kind.NOT, start, idx);
    }
  }

  private Token lexEq(int start) {
    if (match('=')) {
      advance();
      return addToken(Kind.EQUAL, start, idx);
    } else {
      return addToken(Kind.ASSIGN, start, idx);
    }
  }

  private Token numbersAndIdents(int start) {
   if (Character.isDigit(peek())) return lexNum(start);
   if (Character.isAlphabetic(peek())) return  lexIdent(start);
   return null;
  }

  private Token lexIdent(int start) {
    var sb = new StringBuilder();
    if (Character.isAlphabetic(peek())) {
      sb.append(peek());
      advance();
      while (Character.isLetterOrDigit(peek())) {
        sb.append(peek());
        advance();
      }
    }
    return addToken(fromString(sb.toString()), start, idx);
  }

  private Token lexNum(int start) {
    var sb = new StringBuilder();
    if (match('0', 'x')) {
      sb.append('0');
      sb.append('x');
      advance(2);
      while (Character.isDigit(peek()) || (peek() >= 'A' && peek() <= 'F') || (peek() >= 'A' && peek() <= 'F')) {
        sb.append(peek());
        advance();
      }
      return addToken(sb.toString(), Kind.NUMBER, start, idx);
    }

    while (Character.isDigit(peek())) {
      sb.append(peek());
      advance();
    }
    return addToken(sb.toString(), Kind.NUMBER, start, idx);
  }

  //TODO(anita): Fix to use match instead of this peek bullshit
  private Token lexNumber() {
    var sb = new StringBuilder();
    var start = idx;
    if (peek() == '0') {
      sb.append(peek());
      advance();
      if (peek() == 'x') {
       sb.append(peek());
       while (Character.isDigit(peek()) || (peek() >= 'A' && peek() <= 'F') || (peek() >= 'A' && peek() <= 'F')) {
        sb.append(peek());
        advance();
       }
       return addToken(sb.toString(), Kind.NUMBER, start, idx);
      }
    }
    while (Character.isDigit(peek())) {
      sb.append(peek());
      advance();
    }
    return addToken(sb.toString(), Kind.NUMBER, start, idx);
  }

  public Token lexDiv(int start) {
    var sb = new StringBuilder();
    if (match('/') && match('/')) {
      sb.append("//");
      advance(2);
      while (!match('\n')) {
        sb.append(peek());
        advance();
      }
      return addToken(sb.toString(), Kind.COMMENT, start, idx);
    } else {
      return addToken(Kind.DIVIDE, start, idx);
    }
  }

  public Token lexAnd(int start) {
    if (match('&') && match(1, '&')) {
      advance(2);
      return addToken(Token.Kind.AND, start, idx);
    } else {
      return addToken(Token.Kind.BIT_AND, start, idx);
    }
  }

  public Token lexColon(int start) {
    if (match(':') && match(1,'=')) {
      advance(2);
      return addToken(Kind.ASSIGN_INFER, start, idx);
    } else {
      return addToken(Kind.COLON, start, idx);
    }
  }

  private Token lexDot(int start) {
    if (match('.') && match(1, '.')) {
      advance(2);
      return addToken(Kind.RANGE, start, idx);
    } else {
      return addToken(Kind.PERIOD, start, idx);
    }
  }
  public Token.Kind fromString(String string) {
    for (var kind : Kind.values()) {
      if (kind.lexme.equals(string)) {
        return kind;
      }
    }
    return Kind.IDENT;
  }

  private void advance(int n) {
    this.idx = this.idx + n;
  }

  private Token addToken(String lexme, Kind kind, int start, int end) {
    return new Token(lexme, kind, new Position(start, end, this.line, this.column));
  }
  private Token addToken(Kind kind, int start, int end) {
    return new Token(kind, new Position(start, end, this.line, this.column));
  }

  private char peek() {
    return peek(0);
  }

  private char peek(int n) {
    return node.source.get(this.idx + n);
  }

  private void advance() {
    this.idx++;
    this.column++;
    if (peek() == '\n') {
      this.line++;
      this.column = 1;
    }
  }
  private boolean match(char c) {
    return match(0, c);
  }

  private boolean match(char... chars) {
    var index = 0;
    for (var c : chars) {
      if (peek(index) != c) return false;
      index++;
    }
    return true;
  }

  private boolean match(int i, char c) {
    return this.peek(i) == c;
  }
}
