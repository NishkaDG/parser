import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;

public class Parser{
    //public static Map<String, String> typemap = new HashMap<String, String>();
    public static Map<String, String> vartotype = new HashMap<String,String>();
    public static Map<String, Token> vartoval = new HashMap<String, Token>();
    public static Map<String, Token> stringtotoken = new HashMap<String, Token>();
    private static int ctr = 0;
    private static Map<String, Integer> precedence = new HashMap<String, Integer>();
    public static void main(String[] fname) throws IOException{
        ArrayList<Token> alltokens = Lexer.interpret(fname[0]);
        if(alltokens!=null){
            vartotype = Lexer.typetoname;
            vartoval = Lexer.vartoval;
            setPrecedence();
            linebyline(alltokens);
        }
        Map<String, String> isPrinted = new HashMap<String, String>();
        for(Token t : alltokens){
            if(t.type.name().equals("VAR")){
                if(isPrinted.get(t.data)==null){
                    System.out.println(t.data + "="+getValue(t).data);
                    isPrinted.put(t.data, "YES");
                }
            }
        }
        ArrayList<Token> postcondition = Lexer.interpret(fname[1]);
        checkPost(postcondition);
    }
    
    private static void setPrecedence(){
        precedence.put("/",4);
        precedence.put("*",3);
        precedence.put("+",2);
        precedence.put("-",1);
    }
    
    private static int getPrecedence(String o){
        try{
            return precedence.get(o);
        }
        catch(NullPointerException n){
            System.out.println(n);
            return 0;
        }
    }
    
    private static void linebyline(ArrayList<Token> tokens){
        ArrayList<Token> stmt = new ArrayList<Token>();
        for(Token t : tokens){
            //System.out.println(t);
            if(!(t.type.name().equals("EOL") || t.type.name().equals("DECSEP") || t.type.name().equals("COMMENT")||t.type.name().equals("BEGINNING") || t.type.name().equals("END"))){
                stmt.add(t);
            }
            else{
                sortChaff(stmt, "=");
                stmt = new ArrayList<Token>();
            }
        }
        //System.out.println("Done. Final values are:");
        //System.out.println(vartoval);
    }
    
    private static void sortChaff(ArrayList<Token> stmt, String sep){
        String s = "";
        ArrayList<Token> lhs = new ArrayList<Token>();
        ArrayList<Token> rhs = new ArrayList<Token>();
        int ctr = 0;
        try{
            if(stmt.size()==1){
                String vname = stmt.get(0).data;
                //System.out.println(vname+": "+vartoval.get(vname).data);
            }
            else{
                for(Token t : stmt){
                    if(t.data.equals(sep)){
                        rhs.addAll(stmt.subList(ctr+1, stmt.size()));
                        Token l = lhs.get(lhs.size()-1);
                        if(sep.equals("=")){
                            Evaluate.setValue(l, dijkstra(rhs));
                        }
                        else{
                            //System.out.println(sep);
                            Token lval = dijkstra(lhs);
                            Token rval = dijkstra(rhs);
                            //System.out.println(lval.data+","+rval.data);
                            boolean prec = Evaluate.checkValue(lval, rval, sep);
                            if(!prec){
                                throw new ConditionError();
                            }
                            else{
                                System.out.println("Condition satisfied.");
                            }
                        }
                        lhs = new ArrayList<Token>();
                        rhs = new ArrayList<Token>();
                    }
                    else{
                        lhs.add(t);
                        ctr++;
                    }
                }
            }
        }
        catch(ConditionError c){
            System.out.println("Postcondition failed.");
        }
    }

    private static void checkPost(ArrayList<Token> postcondition){
        for(Token t : postcondition){
            if(t.type.name().equals("BOOLOP")){
                sortChaff(postcondition, t.data);
                break;
            }
        }
    }
    
    private static Token dijkstra(ArrayList<Token> expr){
        //System.out.println(expr);
        Stack operators = new Stack();
        Stack operands = new Stack();
        for(Token t : expr){
            String tname = t.type.name();
            if(tname.equals("VAR") || tname.equals("INTEGER")){
                //System.out.println(getValue(t));
                operands.push(t);
            }
            else if(t.data.equals("(")){
                operators.push(t);
            }
            else if(tname.equals("BINARYOP")){
                Token op = operators.pop();
                while(op!= null && !(op.data.equals("(")) && getPrecedence(op.data)>=getPrecedence(t.data)){
                    Token x = operands.pop();
                    Token y = operands.pop();
                    Token z = Evaluate.eval(y, x, op.data);
                    operands.push(z);
                    op = operators.pop();
                }
                if(op!=null){
                    operators.push(op);
                }
                operators.push(t);
            }
            else if(t.data.equals(")")){
                Token op = operators.pop();
                while(op!= null && !(op.data.equals("("))){
                    Token y = operands.pop();
                    Token x = operands.pop();
                    Token z = Evaluate.eval(x, y, op.data);
                    op = operators.pop();
                    operands.push(z);
                }
            }
            //System.out.println(operators);
            //System.out.println(operands);
        }
        while(operators.height()>0){
            Token op = operators.pop();
            Token y = operands.pop();
            Token x = operands.pop();
            Token z = Evaluate.eval(x, y, op.data);
            operands.push(z);
        }
        //System.out.println(vartoval);
        //System.out.println(operators);
        //System.out.println(operands);
        return operands.pop();
    }
    
    private static Token getValue(Token thistoken){
        return Evaluate.getValue(thistoken);
    }
}
