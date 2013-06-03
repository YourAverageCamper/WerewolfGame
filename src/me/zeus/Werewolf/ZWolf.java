

package me.zeus.Werewolf;

import java.io.Serializable;


public class ZWolf implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -5170036421374863213L;
    String name;
    boolean activated;
    
    public ZWolf(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public boolean isActive()
    {
        return activated;
    }
    
}
