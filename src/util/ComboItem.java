/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

public class ComboItem 
{
    private final int id;
    private final String label;

    public ComboItem(int id, String label) 
    {
        this.id = id;
        this.label = label;
    }

    public int getId() { return id; }

    @Override
    public String toString() { return label; }
}
