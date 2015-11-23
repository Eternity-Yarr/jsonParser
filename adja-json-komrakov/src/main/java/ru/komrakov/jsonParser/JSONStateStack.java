package ru.komrakov.jsonParser;

import java.util.ArrayList;
import java.util.List;


//FIXME: Cleanup
public class JSONStateStack {
    private List<String> stack;
    private Integer size;

    JSONStateStack(){
        stack = new ArrayList<>();
        size = 0;
    }

    void push(String state){
        stack.add(state);
        size++;
    }

    String pop(){
        if(size == 0){
            return null;
        }
        String state = stack.get(size - 1);
        if (size - 1 > 0){
            stack.remove(size - 1);
            size--;
        }
        return state;
    }

    String readLast(){
        String last = "";
        if (size > 0){
            last = stack.get(size - 1);
        }
        return last;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String s:stack){
            sb.append(s);
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
