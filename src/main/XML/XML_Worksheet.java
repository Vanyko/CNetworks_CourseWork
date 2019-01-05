package main.XML;

import java.util.ArrayList;

public class XML_Worksheet {
    String name;
    ArrayList<XML_Table> tables;

    public XML_Worksheet(String name) {
        this.name = name;
        this.tables = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<XML_Table> getTables() {
        return tables;
    }

    public void setTables(ArrayList<XML_Table> tables) {
        this.tables.clear();
        this.tables.addAll(tables);
    }

    public void addTable(XML_Table table){
        tables.add(table);
    }

    public void removeTable(XML_Table table){
        tables.remove(table);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("<Worksheet ss:Name=\"");

        s.append(this.name);
        s.append("\">");

        for (XML_Table table : tables){
            s.append(table.toString());
        }

        s.append("</Worksheet>");

        return s.toString();
    }
}
