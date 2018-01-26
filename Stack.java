import java.util.ArrayList;

public class Stack{
    private ArrayList<Token> s;
    private int lastPos;
    
    public void push(Token newtoken){
        s.add(newtoken);
        lastPos++;
    }
    
    public Token pop(){
        try{
            Token tbr = s.get(lastPos);
            s.remove(lastPos);
            lastPos--;
            return tbr;
        }
        catch(Exception e){
            return null;
        }
    }
    
    public int height(){
        return lastPos+1;
    }
    
    public Stack(){
        lastPos = -1;
        s = new ArrayList<Token>();
    }
    
    @Override
    public String toString() {
        String tp = "";
        for(int i = 0; i<=lastPos; i++){
            tp = tp+ s.get(i).data+" ";
        }
        return tp;
    }
}