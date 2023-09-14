package org.sk.pdfreader.tuples;

public class Pair <A, B> {
    private A a;
    private B b;
    public Pair(A first, B second){
        this.a =first;
        this.b =second;
    }
    public A getA(){
        return a;
    }
    public B getB(){
        return b;
    }

    public void setA(A a) {
        this.a = a;
    }

    public void setB(B b) {
        this.b = b;
    }
    public static <A,B> Pair<A,B> of(A a,B b){
        return new Pair<>(a,b);
    }
}
