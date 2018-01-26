public class Token{
    public TokenType type;
    public String data;
    public Token(TokenType t, String d){
        this.type=t;
        this.data=d;
    }
    @Override
    public String toString() {
        return String.format("(%s %s)", type.name(), data);
    }
    @Override
    public boolean equals(Object other) {
        if(Token.class.isAssignableFrom(other.getClass())){
            Token that = (Token) other;
            if(this.data.equals(that.data)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
}