package model;

public class Parameters<T1, T2> {

    private T1 parameter1;
    private T2 parameter2;

    public Parameters(T1 parameter1, T2 parameter2){
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    public T1 getParameter1() {
        return parameter1;
    }

    public T2 getParameter2() {
        return parameter2;
    }

    public void setParameter2(T2 parameter2) {
        this.parameter2 = parameter2;
    }
}
