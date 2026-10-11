package com.example.demo12;

public interface Stratyhandhler<I,D,O> {
    O apply(I req, D ctx);

}
