package be.polfredo.pluginBoS;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

/**
 * Created by Polfredo on 18-05-16.
 */
public class Main extends JavaPlugin {

    private File config;

    private String startMessage;
    private String endMessage;

    private long timer;
    private int nbrPlayer;

    private Location redLoc, blueLoc, greenLoc, yellowLoc;
    private Location redChest, blueChest, greenChest, yellowChest;

    private PlayerBoS[] redTeam, blueTeam, greenTeam, yellowTeam;

    //          TODO LIST
    // Event : le joueur veut rejoindre la partie      DONE
    // Event : le joueur meurt pendant la partie       DONE
    // La partie peut etre lancée                      DONE
    // Tâche fin de game après X ticks                 DONE
    // Event : le joueur veut ouvrir son coffre
    // Compte le nombre de diamands par équipe
    // Affichage state game
    // Clean de la partie                              DONE
    ////////////////////////////////////////////////////////////
    // Bonus : jouer avec le fichier config            DONE

    @Override
    public void onEnable() {
        getLogger().info("Plugin BoS started !!");

        config = new File("config");
        configVariable();

        Listener l = new PluginListener(redTeam, blueTeam, greenTeam, yellowTeam);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(l, this);
    }
    @Override
    public void onDisable() {
    }



    // GESTION DE LA PARTIE
    public void startGame (PlayerBoS[]a, PlayerBoS[]b, PlayerBoS[]c, PlayerBoS[]d) {
        PlayerBoS[] allPlayer = concat(a, b, c, d);
        BukkitRunnable endTask = new EndTask();

        sendMessage(allPlayer, startMessage);
        teleport(allPlayer);
        endTask.runTaskLater(this, timer);
    }
    public void endGame() {
        // annoncer le vainqueur
        // vider les listes
        //freeGame(l);
        // respawn la map
    }
    public void freeGame(PluginListener l) {
        l.setRedTeam(null);
        l.setBlueTeam(null);
        l.setGreenTeam(null);
        l.setYellowTeam(null);
    }
    public void configVariable(){
        FileConfiguration c = YamlConfiguration.loadConfiguration(this.config);
        World world = Bukkit.getWorld(c.getString("World"));
        redLoc = new Location(world, c.getDouble("RedLoc.X"), c.getDouble("RedLoc.Y"), c.getDouble("RedLoc.Z"));
        blueLoc = new Location(world, c.getDouble("BlueLoc.X"), c.getDouble("BlueLoc.Y"), c.getDouble("BlueLoc.Z"));
        greenLoc = new Location(world, c.getDouble("GreenLoc.X"), c.getDouble("GreenLoc.Y"), c.getDouble("GreenLoc.Z"));
        yellowLoc = new Location(world, c.getDouble("YellowLoc.X"), c.getDouble("YellowLoc.Y"), c.getDouble("YellowLoc.Z"));

        redChest = new Location(world, c.getDouble("RedChest.X"), c.getDouble("RedChest.Y"), c.getDouble("RedChest.Z"));
        blueChest = new Location(world, c.getDouble("BlueChest.X"), c.getDouble("BlueChest.Y"), c.getDouble("BlueChest.Z"));
        greenChest = new Location(world, c.getDouble("GreenChest.X"), c.getDouble("GreenChest.Y"), c.getDouble("GreenChest.Z"));
        yellowChest = new Location(world, c.getDouble("YellowChest.X"), c.getDouble("YellowChest.Y"), c.getDouble("YellowChest.Z"));

        redTeam = new PlayerBoS[nbrPlayer];
        blueTeam = new PlayerBoS[nbrPlayer];
        greenTeam = new PlayerBoS[nbrPlayer];
        yellowTeam = new PlayerBoS[nbrPlayer];

        startMessage = c.getString("StartMessage");
        endMessage = c.getString("EndMessage");

        nbrPlayer = c.getInt("NbrPlayer");
        timer = c.getLong("Timer");
    }



    // GET && SET       type = 1 : SpawnLoc     type = 2 : ChestLoc
    public Location getLocation (String color, int type) {
        if (color == "red"){if(type==1) {return redLoc;} return redChest;}
        else if (color == "blue"){if(type==1) {return blueLoc;} return blueChest;}
        else if (color == "green"){if(type==1) {return greenLoc;} return greenChest;}
        else if (color == "yellow"){if(type==1) {return yellowLoc;} return yellowChest;}
        return null;
    }
    public File getFile () {return this.config;}



    // METHODE ANNEXE
    public void sendMessage(PlayerBoS[] team, String message) {
        for (int i = 0; i < team.length; i++ ) { team[i].getPlayer().sendMessage("message"); }
    }
    public void teleport (PlayerBoS[] team){
        for (int i = 0; i < team.length; i++) {
            if (team[i] != null) {
                team[i].getPlayer().teleport(team[i].getLoc());
            }
        }
    }
    public PlayerBoS[] concat (PlayerBoS[]a, PlayerBoS[]b, PlayerBoS[]c, PlayerBoS[]d){
        PlayerBoS[] newTab = new PlayerBoS[4*a.length];
        for (int i = 0, j = 0; i < newTab.length && j < a.length; i = i+4, j++) {
            newTab[i] = a[j];
            newTab[i + 1] = b[j];
            newTab[i + 2] = c[j];
            newTab[i + 3] = d[j];
        }
        return newTab;
    }
}
