import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class Lexer{
    public static Map<String, String> typetoname = new HashMap<String, String>();
    public static Map<String, Token> vartoval = new HashMap<String, Token>();
    public static boolean existsError = false;
    public static ArrayList<Token> lex(String line){
        ArrayList<Token> tokens = new ArrayList<Token>();
        StringBuffer tpb = new StringBuffer();
        for(TokenType tt : TokenType.values()){
            tpb.append(String.format("|(?<%s>%s)", tt.name(), tt.pattern));
        }
        Pattern tPatterns = Pattern.compile(new String(tpb.substring(1)));
        Matcher matcher = tPatterns.matcher(line);
        while (matcher.find()) {
            for (TokenType tk: TokenType.values()) {
                if(matcher.group(TokenType.WHITESPACE.name()) != null)
                    continue;
                else if (matcher.group(tk.name()) != null) {
                    tokens.add(new Token(tk, matcher.group(tk.name())));    
                    break;
                }
            }
        }
        //System.out.println(tokens);
        return tokens;
    }
    
    public static void allDeclared(ArrayList<Token> alltokens) throws DeclarationError{
        Token prevtoken=null;
        for(int i = 0; i<alltokens.size(); i++){
            Token t = alltokens.get(i);
            String vartype=t.type.name();
            String varname=t.data;
            //System.out.println(varname);
            //System.out.println(vartype);
            if(vartype.equals("VAR")){
                if(typetoname.containsKey(varname)){
                    continue;
                }
                else{
                    if(prevtoken==null){
                        throw new DeclarationError("Variable "+varname+" has not been declared.");
                    }
                    else{
                        String prevname = prevtoken.type.name();
                        String prevvalue = prevtoken.data;
                        if(prevname.equals("INTDECLARATION")){
                            if(!(typetoname.containsKey(varname))){
                                typetoname.put(varname,prevvalue);
                            }
                        }
                        else if(prevname.equals("DECSEP")){
                            boolean foundInLine=false;
                            Token jpast = alltokens.get(0);
                            String jval = jpast.type.name();
                            if(prevname.equals("INTDECLARATION")){
                                typetoname.put(varname,jpast.data);
                                foundInLine=true;
                            }
                            else{
                                throw new DeclarationError("Variable "+varname+" is after a comma.");
                            }
                        }
                        else{
                            if(!(typetoname.containsKey(varname))){
                                //System.out.println(typetoname);
                                throw new DeclarationError("Variable "+varname+" is not in the map.");
                            }
                        }
                    }
                }
            }
            prevtoken=t;
        }
    }
                       
    public static ArrayList<Token> interpret(String filename){
        String line;
        String expr="";
        ArrayList<Token> t = new ArrayList<Token>();
        try{
            FileReader fileReader=new FileReader(filename);
            BufferedReader br = new BufferedReader(fileReader);
            while((line=br.readLine())!=null){
                if(line.length()>0){
                    if(line.charAt(0)=='#'){
                        t.addAll(lex(line));
                    }
                    else{
                        for(int i=0; i<line.length(); i++){
                            char c = line.charAt(i);
                            expr=expr+c;
                            if(c==';'){
                                ArrayList<Token> e = lex(expr);
                                allDeclared(e);
                                t.addAll(e);
                                expr="";
                            }
                        }
                    }
                }
            }
            if(expr.length()>0){
                t.addAll(lex(expr));
                expr = "";
            }
        }
        catch(DeclarationError derror){
            System.out.println(derror);
            derror.printStackTrace();
            return null;
        }
        catch(IOException ioe){
            System.out.println(ioe);
            return null;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return t;
    }
}
