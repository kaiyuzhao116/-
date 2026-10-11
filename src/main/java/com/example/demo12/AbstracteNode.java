package com.example.demo12;

public abstract class AbstracteNode<I, D, O> implements Stratyhandhler<I, D, O> {

    protected abstract Stratyhandhler<I, D, O> nextNode(I req, D ctx);

    protected abstract O doapply(I req, D ctx);

    @Override
    public final O apply(I req, D ctx) {
        O result = doapply(req, ctx);
        if(result != null) {
            return result;
        }
        Stratyhandhler<I, D, O> idoStratyhandhler = nextNode(req, ctx);
        if(idoStratyhandhler == null) {
            return null;
        }
        return idoStratyhandhler.apply(req, ctx);
    }
}
