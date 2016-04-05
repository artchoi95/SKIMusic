package com.example.theodore.staffline;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by theodore on 3/26/2016.
 */
public class Staff {
    private int[] line = new int[5];
    private int[] upline = new int[3];
    private int[] bottomline = new int[3];
    private int[] upspace = new int[2];
    private int[] bottomspace = new int[2];
    private int[] space = new int [4];
    private int staffupperbound;
    private int stafflowerbound;
    private int linedistance;

    private List<Note> notes;

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public int[] getLine() {
        return line;
    }

    public void setLine(int[] line) {
        this.line = line;
    }

    public void addToLine(int value, int position)
    {
        if(position == 0)
        {
            setStaffupperbound(value);
        }
        else if(position == 1)
        {
            setLinedistance(value - line[0]);
            for(int i = 0; i< getUpline().length;i++)
            {
                getUpline()[i] = line[0] - linedistance * (i+1);
            }
            getUpspace()[0] = (line[0] + getUpline()[0])/2;
            for(int i = 1; i< getUpspace().length;i++)
            {
                getUpspace()[i] = (getUpline()[i+1] + getUpline()[i])/2;
            }
            setStaffupperbound(getUpline()[getUpline().length-1]-linedistance);
        }
        else if(position == 4)
        {
            setStafflowerbound(value);
            for(int i = 0; i< getBottomline().length;i++)
            {
                getBottomline()[i] = value + linedistance * (i+1);
            }
            getBottomspace()[0] = (line[line.length-1] + getBottomline()[0])/2;
            for(int i = 1; i< getBottomspace().length;i++)
            {
                getBottomspace()[i] = (getBottomline()[i+1] + getBottomline()[i])/2;
            }
            setStafflowerbound(getBottomline()[getBottomline().length-1]+linedistance);
        }
        if(position > 0 )
        {
            getSpace()[position-1] = (value + line[position-1])/2;
        }
        line[position] = value;

    }

    public int getLineValue(int position)
    {
        return line[position];
    }

    public int getStaffupperbound() {
        return staffupperbound;
    }

    public void setStaffupperbound(int staffupperbound) {
        this.staffupperbound = staffupperbound;
    }

    public int getStafflowerbound() {
        return stafflowerbound;
    }

    public void setStafflowerbound(int stafflowerbound) {
        this.stafflowerbound = stafflowerbound;
    }

    public int getLinedistance() {
        return linedistance;
    }

    public void setLinedistance(int linedistance) {
        this.linedistance = linedistance;
    }

    public void addNote(Note n) {
        if(notes==null)
        {
            notes = new ArrayList<Note>();
        }
        notes.add(n);
    }

    public int[] getSpace() {
        return space;
    }

    public void setSpace(int[] space) {
        this.space = space;
    }

    public void sortNotes()
    {
        Collections.sort(notes);
    }

    public int[] getUpline() {
        return upline;
    }

    public void setUpline(int[] upline) {
        this.upline = upline;
    }

    public int[] getBottomline() {
        return bottomline;
    }

    public void setBottomline(int[] bottomline) {
        this.bottomline = bottomline;
    }

    public int[] getUpspace() {
        return upspace;
    }

    public void setUpspace(int[] upspace) {
        this.upspace = upspace;
    }

    public int[] getBottomspace() {
        return bottomspace;
    }

    public void setBottomspace(int[] bottomspace) {
        this.bottomspace = bottomspace;
    }
}
