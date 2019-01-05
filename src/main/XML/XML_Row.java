package main.XML;

import java.util.ArrayList;

public class XML_Row {
    private ArrayList<XML_Cell> cells;

    public XML_Row() {
        cells = new ArrayList<>();
    }

    public ArrayList<XML_Cell> getCells() {
        return cells;
    }

    public void setCells(ArrayList<XML_Cell> cells) {
        this.cells.clear();
        this.cells.addAll(cells);
    }

    public void addCell(XML_Cell cell){
        cells.add(cell);
    }

    public void removeCell(XML_Cell cell){
        cells.remove(cell);
    }

    public int getColumns(){
        return cells.size();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("<Row>");

        for (XML_Cell cell : cells){
            s.append(cell.toString());
        }

        s.append("</Row>");

        return s.toString();
    }
}
