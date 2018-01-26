import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Evaluate{
    public static Map<String, String> vartotype = new HashMap<String, String>();
    public static Map<String, Token> vartoval = new HashMap<String, Token>();
    public static boolean checkValue(Token l, Token r, String oper){
        Token lval = getValue(l);
        Token rval = getValue(r);
        try{
            int lint = Integer.parseInt(lval.data);
            int rint = Integer.parseInt(rval.data);
            //System.out.println(lint);
            //System.out.println(rint);
            if(oper.equals(">")){
                return lint>rint;
            }
            else if(oper.equals(">=")){
                return lint>=rint;
            }
            else if(oper.equals("<")){
                return lint<rint;
            }
            else if(oper.equals("<=")){
                return lint<=rint;
            }
            else {
                return lint==rint;
            }
        }
        catch(NullPointerException e){
            return false;
        }
        catch(NumberFormatException n){
            return false;
        }
    }
    public static Token eval(Token l, Token r, String oper){
        char op = oper.charAt(0);
        Token lval = getValue(l);
        Token rval = getValue(r);
        //System.out.println("Operator is "+op);
        //System.out.println("Left operand: "+l);
        //System.out.println(getValue(l).data);
        //System.out.println("Right operand: "+r);
        //System.out.println(getValue(r).data);
        Token result;
        int f;
        try{
            int lint = Integer.parseInt(lval.data);
            int rint = Integer.parseInt(rval.data);
            switch(op){
                case '+':
                    f = lint+rint;
                    break;
                case '-':
                    f = lint-rint;
                    break;
                case '*':
                    f = lint*rint;
                    break;
                case '/':
                    if(rint!=0){
                        f = lint/rint;
                    }
                    else{
                        System.out.println("Divide by zero exception");
                        f=0;
                    }
                    break;
                case '=':
                    setValue(l, r);
                    f=rint;
                    break;
                default:
                    f=lint;
                }
            result = new Token(l.type, f+"");
            return result;
        }
        catch(NumberFormatException nfe){
            System.out.println("One of the variables has not been declared.");
            return l;
        }
        catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
            return l;
        }
    }
    
    private static Token setType(Token x){
        ArrayList<Token> res = Lexer.lex(x.data);
        return res.get(0);
    }

    public static void setValue(Token x, Token y){
        //System.out.println("Assigning values...");
        //System.out.println(vartotype);
        //System.out.println(vartoval);
        //System.out.println(x);
        //System.out.println(y);
        //System.out.println(vartotype.get(x.data));
        try{
            if(x.type.name().equals("VAR")){
                //System.out.println(x);
                //System.out.println(getValue(y));
                vartoval.put(x.data, getValue(y));
            }
        }
        catch(Exception e){
            System.out.println("Error. Cannot assign "+getValue(y).type.name()+" to "+x.type.name());
        }
    }
    
    public static Token getValue(Token t){
        //System.out.println("In getValue");
        //System.out.println(t.type.name());
        //System.out.println(vartoval.get(t));
        if(t.type.name().equals("VAR")){
            //System.out.println(t.data);
            //System.out.println(vartoval);
            for(String k : vartoval.keySet()){
                if(k.equals(t.data)){
                    //System.out.println(k);
                    return getValue(vartoval.get(k));
                }
            }
            //System.out.println("Variable is "+t.data+" and value is "+vartoval.get(t).data);
            return t;
        }
        else 
            return t;
    }
}
