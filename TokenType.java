public enum TokenType{
    BEGINNING("^begin:"),
    END("^end:"),
    COMMENT("^#.*\n$"),
    WHITESPACE("[ \t\f\r\n]+"),
    INTDECLARATION("int "),
    VAR("[A-Za-z]+[A-Za-z0-9_]*"),
    INTEGER("-?[0-9]+"),
    PAREN("[\\(|\\)]"),
    BINARYOP("[*|/|+|\\-|=]"),
    BOOLOP("[>|<|>=|<=]"),
    DECSEP(","),
    EOL(";");
    public final String pattern;
    private TokenType(String pattern) {
        this.pattern = pattern;
    }
    @Override
    public String toString(){
        return String.format(pattern);
    }
}
