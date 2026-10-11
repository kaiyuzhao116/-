package com.example.demo12;

public class TwoNode extends AbstracteNode<String, String, String> {
    @Override
    protected Stratyhandhler<String, String, String> nextNode(String req, String ctx) {
        return null;
    }

    @Override
    protected String doapply(String req, String ctx) {
        return "two";
    }
}
