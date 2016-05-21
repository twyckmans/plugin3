package be.polfredo.pluginBoS;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.ChatColor;

/**
 * Created by Polfredo on 18-05-16.
 */
public class PluginListener implements Listener {

    private Main main;        // permet d'accéder aux méthodes de la main.

    private PlayerBoS[] redTeam;
    private PlayerBoS[] blueTeam;
    private PlayerBoS[] greenTeam;
    private PlayerBoS[] yellowTeam;

    public PluginListener (PlayerBoS[] redTeam, PlayerBoS[] blueTeam, PlayerBoS[] greenTeam, PlayerBoS[] yellowTeam){
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
        this.greenTeam = greenTeam;
        this.yellowTeam = yellowTeam;
    }


    public PlayerBoS[] getRedTeam() { return redTeam;}
    public PlayerBoS[] getBlueTeam() { return blueTeam;}
    public PlayerBoS[] getGreenTeam() { return greenTeam;}
    public PlayerBoS[] getYellowTeam() { return yellowTeam;}
    public void setRedTeam(PlayerBoS[] a) { this.redTeam = a;}
    public void setBlueTeam(PlayerBoS[] a) { this.blueTeam = a;}
    public void setGreenTeam(PlayerBoS[] a) { this.greenTeam = a;}
    public void setYellowTeam(PlayerBoS[] a) { this.yellowTeam = a;}


    @EventHandler                                                   // gère l'accès à une équipe pour un joueur
    public void onPlayerInteract(PlayerInteractEvent event){
        PlayerBoS actionMan = new PlayerBoS(event.getPlayer(), null);
        boolean test = false;
        if (actionMan.isInside(this.redTeam))           {actionMan.setLoc("red");    test = true;}
        else if (actionMan.isInside(this.blueTeam))     {actionMan.setLoc("blue");   test = true;}
        else if (actionMan.isInside(this.greenTeam))    {actionMan.setLoc("green");  test = true;}
        else if (actionMan.isInside(this.yellowTeam))   {actionMan.setLoc("yellow"); test = true;}
        Block b = event.getClickedBlock();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(b.getType() == Material.SIGN ||b.getTypeId() == 68) {
                Sign s = (Sign) b.getState();
                if (s.getLine(0).contains("Join") && s.getLine(1).contains("Red Team")) {registeredPlayer(event.getPlayer(), "red");}
                if (s.getLine(0).contains("Join") && s.getLine(1).contains("Blue Team")) {registeredPlayer(event.getPlayer(), "blue");}
                if (s.getLine(0).contains("Join") && s.getLine(1).contains("Green Team")) {registeredPlayer(event.getPlayer(), "green");}
                if (s.getLine(0).contains("Join") && s.getLine(1).contains("Yellow Team")) {registeredPlayer(event.getPlayer(), "yellow");}
                s.update();
            }
        }
        if (test && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)){
            if(b.getType() == Material.CHEST ||b.getTypeId() == 54) {
                Location loc = b.getLocation();
                if (loc != actionMan.getLocChest()){
                    // interdire l'accès à ce foutu chest !!!
                }
                if (loc == actionMan.getLocChest()){
                    // compte le nombre de Diams dans le coffre au moment de la fermeture et lance la fin de la game si nécessaire !
                }
            }
        }
    }
    @EventHandler                                                   // gère le respawn d'un joueur dans son camp
    public void onPlayerDeathEvent(PlayerDeathEvent event){
        boolean test = false;
        PlayerBoS deathPlayer = new PlayerBoS(event.getEntity(),null);

        if (deathPlayer.isInside(redTeam)){ deathPlayer.setLoc("red");              test = true;}
        else if (deathPlayer.isInside(blueTeam)){ deathPlayer.setLoc("blue");       test = true;}
        else if (deathPlayer.isInside(greenTeam)){ deathPlayer.setLoc("green");     test = true;}
        else if (deathPlayer.isInside(yellowTeam)){ deathPlayer.setLoc("yellow");   test = true;}

        if (test) {deathPlayer.getPlayer().teleport(deathPlayer.getLoc());}
    }
    @EventHandler                                                   // gère l'affichage des panneaux du lobby
    public void onChangeSign(SignChangeEvent event) {
        if (event.getLine(0).contains("Join")){
            if (event.getLine(1).contains("Red Team")) {
                event.setLine(0, ChatColor.RED + "Join");
                event.setLine(1, ChatColor.RED + "Red TEAM");
                event.setLine(2, getNbrOfPlayer(this.redTeam) + "/" + redTeam.length);
            }
            if (event.getLine(1).contains("Blue Team")) {
                event.setLine(0, ChatColor.BLUE + "Join");
                event.setLine(1, ChatColor.BLUE + "Blue TEAM");
                event.setLine(2, getNbrOfPlayer(this.blueTeam) + "/" + blueTeam.length);
            }
            if (event.getLine(1).contains("Green Team")) {
                event.setLine(0, ChatColor.GREEN + "Join");
                event.setLine(1, ChatColor.GREEN + "Green TEAM");
                event.setLine(2, getNbrOfPlayer(this.greenTeam) + "/" + greenTeam.length);
            }
            if (event.getLine(1).contains("Yellow Team")) {
                event.setLine(0, ChatColor.GOLD + "Join");
                event.setLine(1, ChatColor.GOLD + "Yellow TEAM");
                event.setLine(2, getNbrOfPlayer(this.yellowTeam) + "/" + yellowTeam.length);
            }
        }
    }

    // méthodes annexes
    public void registeredPlayer (Player p, String Color){
        PlayerBoS newPlayer = new PlayerBoS(p, Color);

        if (Color != "red") {newPlayer.removePlayer(this.redTeam);}
        if (Color != "blue") {newPlayer.removePlayer(this.blueTeam);}
        if (Color != "green") {newPlayer.removePlayer(this.greenTeam);}
        if (Color != "yellow") {newPlayer.removePlayer(this.yellowTeam);}

        if (newPlayer.getColor() == "red") {newPlayer.AddPlayer(this.redTeam);}
        if (newPlayer.getColor() == "blue") {newPlayer.AddPlayer(this.blueTeam);}
        if (newPlayer.getColor() == "green") {newPlayer.AddPlayer(this.greenTeam);}
        if (newPlayer.getColor() == "yellow") {newPlayer.AddPlayer(this.yellowTeam);}

        boolean start = true;                   // test si on peut lancer la partie
        if(this.redTeam.length != getNbrOfPlayer(this.redTeam)) {start = false;}
        if(this.blueTeam.length != getNbrOfPlayer(this.blueTeam)) {start = false;}
        if(this.greenTeam.length != getNbrOfPlayer(this.greenTeam)) {start = false;}
        if(this.yellowTeam.length != getNbrOfPlayer(this.yellowTeam)) {start = false;}

        if(start) {main.startGame(this.redTeam, this.blueTeam, this.greenTeam, this.yellowTeam);}        // lance la partie
    }      // ajoute un joueur à une team et lance la partie si les teams sont complètes
    public int getNbrOfPlayer (PlayerBoS[] team){
        int count = 0;
        for (int i = 0; i < team.length; i++) { if (team[i] != null) {count ++;} }
        return count;
    }               // retourne le nombre de joueur dans la liste
}
