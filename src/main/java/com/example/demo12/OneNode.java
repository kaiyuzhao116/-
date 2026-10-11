package com.example.demo12;

public class OneNode extends AbstracteNode<String, String, String> {
    @Override
    protected Stratyhandhler<String, String, String> nextNode(String req, String ctx) {
        return new TwoNode();
    }

    @Override
    protected String doapply(String req, String ctx) {
        return "one";
    }
}
