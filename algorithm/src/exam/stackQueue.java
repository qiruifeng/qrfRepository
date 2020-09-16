package exam;

import utils.Node;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

public class stackQueue {

    private Stack<Integer> in=new Stack<>();
    private Stack<Integer> help=new Stack<>();

    public void push(int num){
        in.push(num);

    }

    public int pop(){
        while (help.isEmpty()){
            help.push(in.peek());
            in.pop();
        }

        int tmp=help.peek();
        return tmp;
    }

    public int size(){
        int num1=in.size();
        int num2=help.size();
        return num1+num2;
    }

//    public int top(){
//        while (!in.isEmpty()){
//
//        }
//    }

    public boolean empty(){
        if (in.empty()&&help.empty()){
            return true;
        }

        return false;
    }


    public static void main(String[] args) {

    }
}
