package org.example;
import org.example.api.token.Token;
    public class Main {
        public static void main(String[] args){
            Token token = new Token();
            String p = token.generateToken("pedrito@guapo.com");
            System.out.println(p);
        }
    }
