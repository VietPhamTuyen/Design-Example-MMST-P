package plt.tud.de.example.model;

/**
 * Created by Viet on 22.01.2016.
 */
public class Tour {
    String name;
    boolean check = false;

    public Tour(String name){
        this.name = name;
    }


    public String getName(){
        return name;
    }


    public void check(){
        check = true;
    }
}
