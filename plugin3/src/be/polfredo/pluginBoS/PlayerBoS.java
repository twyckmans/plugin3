package be.polfredo.pluginBoS;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Polfredo on 18-05-16.
 */

public class PlayerBoS {

    Main plugin;

    // initialisation des zones de Spawn
    private Location redLoc = plugin.getLocation("red", 1);
    private Location blueLoc = plugin.getLocation("blue", 1);
    private Location greenLoc = plugin.getLocation("green", 1);
    private Location yellowLoc = plugin.getLocation("yellow", 1);

    private Location redChest = plugin.getLocation("red", 2);
    private Location blueChest = plugin.getLocation("blue", 2);
    private Location greenChest = plugin.getLocation("green", 2);
    private Location yellowChest = plugin.getLocation("yellow", 2);

    // constructeur
    private Player p;
    private String color;
    private Location locSpawn;
    private Location locChest;

    public PlayerBoS(Player p, String color){
        this.p = p;
        this.color = color;
        if (this.color == "red" ) {          this.locSpawn = redLoc;          this.locChest = redChest;}
        else if (this.color == "blue" ) {    this.locSpawn = blueLoc;         this.locChest = blueChest;}
        else if (this.color == "green" ) {   this.locSpawn = greenLoc;        this.locChest = greenChest;}
        else if (this.color == "yellow" ) {  this.locSpawn = yellowLoc;       this.locChest = yellowChest;}
        else {                               this.locSpawn = null;            this.locChest = null;}
    }

    // get && set
    public String getColor() {return this.color;}
    public Player getPlayer() {return this.p;}
    public Location getLoc() {return this.locSpawn;}
    public Location getLocChest() {return this.locChest;}
    public void setLoc (String color) {
        if (color == "red"){this.locSpawn = redLoc;                 this.locChest = redChest;}
        else if (color == "blue"){this.locSpawn = blueLoc;          this.locChest = blueChest;}
        else if (color == "green") {this.locSpawn = greenLoc;       this.locChest = greenChest;}
        else if (color == "yellow") {this.locSpawn = yellowLoc;     this.locChest = yellowChest;}
    }

    // usefull methode
    public boolean isInside (PlayerBoS[] team){
        for (int i = 0; i < team.length; i++) { if (team[i].getPlayer() == this.p) {return true;} }
        return false;
    }
    public void AddPlayer(PlayerBoS[] team){
        if (isInside(team)){ this.p.sendMessage("You are already registered in this Team"); }
        else {
            for (int i = 0; i < team.length; i++) {
                if (team[i] == null) {
                    this.p.sendMessage("You are now a member of the " + team[1].getColor() + " Team !!");
                    team[i] = new PlayerBoS(this.p, this.color);
                }
            }
            this.p.sendMessage("This team is already full !");
        }
    }
    public void removePlayer (PlayerBoS[] team){
        if (isInside(team)) {
            for (int i = 0; i < team.length; i++) {
                if (team[i].getPlayer() == this.p){team[i] = null;}
            }
            this.p.getPlayer().sendMessage("You aren't a member of the " + this.color + " Team anymore !!");
        }
        else { this.p.getPlayer().sendMessage("You weren't in the team !!"); }
    }
}
