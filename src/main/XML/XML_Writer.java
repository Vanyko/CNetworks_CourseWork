package main.XML;

import main.Network;

import java.io.FileWriter;
import java.io.IOException;

public class XML_Writer {
    private FileWriter file = null;

    public XML_Writer(String filename) {
        try {
            file = new FileWriter(filename);
            file.write("<?xml version=\"1.0\"?>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String s){
        try {
            file.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeNetwork(Network network){

    }
}