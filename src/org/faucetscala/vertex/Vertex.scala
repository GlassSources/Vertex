package org.faucetscala.vertex

import java.io.{File, InputStreamReader, BufferedReader}
import java.net.{URL, HttpURLConnection}

import net.md_5.bungee.api.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.{BanList, GameMode, Bukkit}
import org.bukkit.command.{Command, CommandSender}
import org.bukkit.entity.{EntityType, Player}
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by FaucetTeam on 9/28/2015.
 */
class Vertex extends JavaPlugin{
  /**
   * Core Constants
   */
  val VERSION = this.getDescription.getVersion
  val NAME = "Vertex"
  val ABOUT = "Vertex is a rewrite of Source's Basics Demonstration directly made by the FaucetTeam using Scala."
  val ERROR_MESSAGE = "" + ChatColor.RED + "Sorry, you don't have permission."
  val NULL_ERROR = "" + ChatColor.RED + "The player specified couldn't be found."
  val RELEASE_TYPE = "DEVELOPMENT"

  /**
   * Core Spigot Functions
   */
  override def onEnable() {
    getLogger.info(s"Vertex $VERSION on Scala is running!")
    val pluginParent = this.getDataFolder.getParentFile
    val vertexConfigFolder = new File(pluginParent, "VertexScala")
    val configFile = new File(vertexConfigFolder, "config.yml")
    val config: YamlConfiguration = new YamlConfiguration
    config.options().header("VertexScala Configuration - Please make sure you input valid values!")
    config.addDefault("security.mojangauth", true)
    if(!vertexConfigFolder.exists()){
      vertexConfigFolder.mkdir()
      configFile.createNewFile()
      config.options().copyHeader(true)
      config.options().copyDefaults(true)
      config.save(configFile)
      config.load(configFile)
    }else{
      config.load(configFile)
    }
    if(config.getBoolean("security.mojangauth")){
      if(!Bukkit.getServer.getOnlineMode){
        getLogger.warning("Vertex has found a security issue! You're server is currently allowing hacked clients to join!")
        getLogger.warning("The Mojang Authentication Servers are not enabled on this server! It is suggested to turn online-mode to true!")
      }
    }
    getLogger.info("Vertex will now try to search for updates..")
    var latestVersion: String = null;
    try{
    val id: Int = 12793
    val con: HttpURLConnection = new URL("http://www.spigotmc.org/api/general.php").openConnection.asInstanceOf[HttpURLConnection]
    con.setDoOutput(true)
    con.setRequestMethod("POST")
    con.getOutputStream.write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + id).getBytes("UTF-8"))
    latestVersion = new BufferedReader(new InputStreamReader(con.getInputStream)).readLine
    } catch {
      case e: Exception => getLogger.warning("Vertex had failed searching for updates.")
    }
    if(latestVersion == null){
      // DO NOTHING
    }else{
      if(!latestVersion.equals(this.getDescription.getVersion)){
        getLogger.warning("Vertex has found a update! Update " + latestVersion + " is now available! Please upgrade!")
      }
      if(latestVersion.equals(this.getDescription.getVersion)){
        getLogger.info("Vertex " + this.getDescription.getVersion + " is at the latest version!")
      }
    }
  }
  override def onDisable() {
    getLogger.info(s"Vertex $VERSION is now shutting down.")
  }

  /**
   *
   * CommandEngine created directly by FaucetTeam and derived to be more lightweight and using Scala by FaucetScala.
   */
  override def onCommand(sender: CommandSender, cmd: Command, label: String, args: Array[String]): Boolean ={
    if(cmd.getName.equalsIgnoreCase("scala")){
      if(!sender.hasPermission("vertexscala.info.scalaruntime")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      sender.sendMessage("" + ChatColor.BLUE + "" + ChatColor.BOLD + "PortableScalaRuntime")
      sender.sendMessage("" + ChatColor.GOLD + "PortableScalaRuntime is a closed-source runtime to converting Scala programs to completely Java using Scala Libraries.")
      sender.sendMessage("" + ChatColor.GOLD + "The closed-source runtime was developed by FaucetScala to show a tech demo of VertexScala and for the official versions of VertexScala.")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("instance")){
      if(!sender.hasPermission("vertexscala.info.instance")) {
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(sender.isInstanceOf[Player]){
        sender.sendMessage("" + ChatColor.GOLD + "[VertexPlugin] You are a instance of " + ChatColor.BOLD + "PLAYER.")
        return true
      }else{
        sender.sendMessage("" + ChatColor.GOLD + "[VertexLogger] You are a instance of " + ChatColor.BOLD + "CONSOLE.")
        return true
      }
    }
    if(cmd.getName.equalsIgnoreCase("freeze")){
      if(!sender.hasPermission("vertexscala.admin.freeze")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /freeze <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /freeze <player>")
        return true
      }
      val chosenPlayer = Bukkit.getPlayer(args{0})
      if(chosenPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      val speed = 0.toFloat
      chosenPlayer.setWalkSpeed(speed)
      chosenPlayer.setFlySpeed(speed)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] You have successfully frozen " + chosenPlayer.getPlayerListName + ".")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("unfreeze")){
      if(!sender.hasPermission("vertexscala.admin.unfreeze")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /unfreeze <player>")
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /unfreeze <player>")
        return true
      }
      val chosenPlayer = Bukkit.getPlayer(args{0})
      if(chosenPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      val speed = 0.2.toFloat
      val speed2 = 0.1.toFloat
      chosenPlayer.setWalkSpeed(speed)
      chosenPlayer.setFlySpeed(speed2)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] You have successfully unfrozen " + chosenPlayer.getPlayerListName + ".")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("broadcast")){
      if(!sender.hasPermission("vertexscala.admin.broadcast")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args == null){
        sender.sendMessage("" + ChatColor.RED + "Usage: /broadcast <message>")
        return true
      }
      val message = new StringBuilder()
      for ( s <- args ){
        message.append(s)
      }
      val messageString = message.toString
      if(messageString == ""){
        sender.sendMessage("" + ChatColor.RED + "Usage: /broadcast <message>")
        return true
      }
      Bukkit.broadcastMessage("" + ChatColor.GREEN + "[VertexBroadcast] " + messageString)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexBukkit] You have successfully broadcasted to the server!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("commandengine")){
      if(!sender.hasPermission("vertexscala.info.commandengine")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      sender.sendMessage("" + ChatColor.GREEN + "" + ChatColor.BOLD + "CommandEngine v1.2-DEV-Scala")
      sender.sendMessage("" + ChatColor.GOLD + "CommandEngine is a lightweight command closed-source engine built with Java by FaucetTeam and derived to Scala by the FaucetScala team.")
      sender.sendMessage("" + ChatColor.GOLD + "CommandEngine in Scala has been 90% remade natively from Scala instead from the previous Java source.")
      sender.sendMessage("" + ChatColor.GOLD + "In Basics, 10 command classes were used to create limited commands with Command Engine. We now added more functions and made it more lightweight, so it's just 1 class.")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("seen")){
      if(!sender.hasPermission("vertexscala.info.seen")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /seen <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /seen <player>")
        return true
      }
      val chosenPlayer = Bukkit.getOfflinePlayer(args{0})
      // i don't know if this check statement will work
      if(chosenPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      // end statement
      val playerName = chosenPlayer.getName
      val isOnline = chosenPlayer.isOnline
      val hasPlayedBefore = chosenPlayer.hasPlayedBefore
      val lastPlayed = chosenPlayer.getLastPlayed
      if(hasPlayedBefore){
        sender.sendMessage("" + ChatColor.BLUE + "" + ChatColor.BOLD + playerName)
        if(isOnline){
          sender.sendMessage("" + ChatColor.GREEN + "Player is online!")
        }
        if(!isOnline){
          sender.sendMessage("" + ChatColor.RED + "Player is offline!")
        }
        sender.sendMessage("" + ChatColor.GREEN + "The player has last played at: " + lastPlayed.toString)
        return true
      }
      if(!hasPlayedBefore){
        sender.sendMessage("" + ChatColor.RED + "Sorry, this player hasn't joined this server yet.")
        return true
      }
    }
    if(cmd.getName.equalsIgnoreCase("gamemode") || cmd.getName.equalsIgnoreCase("gm")){
      if(!sender.hasPermission("vertexscala.admin.gamemode")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      // args 0 is gamemode, args 1 is player
      if(args.length > 2){
        sender.sendMessage("" + ChatColor.RED + "Usage: /gamemode <gamemode> <player>")
        return true
      }
      if(args.length < 2){
        sender.sendMessage("" + ChatColor.RED + "Usage: /gamemode <gamemode> <player>")
        return true
      }
      val chosenPlayer = Bukkit.getPlayer(args{1})
      if(chosenPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      val mode1: String = "survival"
      val mode2: String = "creative"
      val mode3: String = "adventure"
      val mode4: String = "spectator"
      val mode5: String = "0"
      val mode6: String = "1"
      val mode7: String = "2"
      val mode8: String = "3"
      if(args{0}.equalsIgnoreCase(mode1) || args{0}.equalsIgnoreCase(mode5)){
        chosenPlayer.setGameMode(GameMode.SURVIVAL)
        sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] The gamemode has been set successfully!")
        return true
      }
      if(args{0}.equalsIgnoreCase(mode2) || args{0}.equalsIgnoreCase(mode6)){
        chosenPlayer.setGameMode(GameMode.CREATIVE)
        sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] The gamemode has been set successfully!")
        return true
      }
      if(args{0}.equalsIgnoreCase(mode3) || args{0}.equalsIgnoreCase(mode7)){
        chosenPlayer.setGameMode(GameMode.ADVENTURE)
        sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] The gamemode has been set successfully!")
        return true
      }
      if(args{0}.equalsIgnoreCase(mode4) || args{0}.equalsIgnoreCase(mode8)){
        chosenPlayer.setGameMode(GameMode.SPECTATOR)
        sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] The gamemode has been set successfully!")
        return true
      }
      sender.sendMessage("" + ChatColor.RED + "[VertexPlugin] Sorry, that was not recongized. Try: /gamemode <gamemode> <player>.")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("disableplugin")){
      if(!sender.hasPermission("vertexscala.admin.disableplugin")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /disableplugin <pluginname>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /disableplugin <pluginname>")
        return true
      }
      // args 0 is pluginname
      val specifiedPlugin = Bukkit.getPluginManager.getPlugin(args{0})
      if(specifiedPlugin == null){
        sender.sendMessage("" + ChatColor.RED + "The plugin specified wasn't found.")
        return true
      }
      val isSpecifiedPluginEnabled = specifiedPlugin.isEnabled
      if(isSpecifiedPluginEnabled){
        Bukkit.getPluginManager.disablePlugin(specifiedPlugin)
        sender.sendMessage("" + ChatColor.GREEN + "[VertexManager] Plugin " + specifiedPlugin.getDescription.getName + " has successfully been disabled!")
        return true
      }
      if(!isSpecifiedPluginEnabled){
        sender.sendMessage("" + ChatColor.RED + "[VertexManager] Plugin " + specifiedPlugin.getDescription.getName + " has already been disabled.")
        return true
      }
      sender.sendMessage("" + ChatColor.RED + "[VertexManager] Something failed when executing the command.. Please report this to the author.")
      return false
    }
    if(cmd.getName.equalsIgnoreCase("enableplugin")){
      if(!sender.hasPermission("vertexscala.admin.enableplugin")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /enableplugin <pluginname>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /enableplugin <pluginname>")
        return true
      }
      // args 0 is pluginname
      val specifiedPlugin = Bukkit.getPluginManager.getPlugin(args{0})
      if(specifiedPlugin == null){
        sender.sendMessage("" + ChatColor.RED + "The plugin specified wasn't found.")
        return true
      }
      val isSpecifiedPluginEnabled = specifiedPlugin.isEnabled
      if(!isSpecifiedPluginEnabled){
        Bukkit.getPluginManager.enablePlugin(specifiedPlugin)
        sender.sendMessage("" + ChatColor.GREEN + "[VertexManager] Plugin " + specifiedPlugin.getDescription.getName + " has successfully been enabled!")
        return true
      }
      if(isSpecifiedPluginEnabled){
        sender.sendMessage("" + ChatColor.RED + "[VertexManager] Plugin " + specifiedPlugin.getDescription.getName + " has already been enabled.")
        return true
      }
      sender.sendMessage("" + ChatColor.RED + "[VertexManager] Something failed when executing the command.. Please report this to the author.")
      return false
    }
    if(cmd.getName.equalsIgnoreCase("ban")){
      if(!sender.hasPermission("vertexscala.admin.ban")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      // arguments: /ban <player> <reason>
      // arg number: <player> = 0 : <reason> = 1
      if(args.length > 2){
        sender.sendMessage("" + ChatColor.RED + "Usage: /ban <player> <reason>")
        return true
      }
      if(args.length < 2){
        sender.sendMessage("" + ChatColor.RED + "Usage: /ban <player> <reason>")
        return true
      }
      val specifiedPlayer = Bukkit.getOfflinePlayer(args{0})
      val banList = Bukkit.getBanList(BanList.Type.NAME)
      banList.addBan(specifiedPlayer.getName, args{1}, null, null)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexServer] Player " + specifiedPlayer.getName + " has been banned!")
      if(specifiedPlayer.isOnline){
        val playerObject = Bukkit.getPlayer(specifiedPlayer.getName)
        playerObject.kickPlayer("" + ChatColor.RED + "You have been banned from the server!")
      }
      return true
    }
    if(cmd.getName.equalsIgnoreCase("unban") || cmd.getName.equalsIgnoreCase("pardon")){
      if(!sender.hasPermission("vertexscala.admin.unban")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /unban <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /unban <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getOfflinePlayer(args{0})
      val banList = Bukkit.getBanList(BanList.Type.NAME)
      banList.pardon(specifiedPlayer.getName)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexServer] Player " + specifiedPlayer.getName + " has been unbanned!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("teleport") || cmd.getName.equalsIgnoreCase("tp")){
      if(!sender.hasPermission("vertexscala.admin.teleport")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      // /teleport <player>
      // args: <player> = 0
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /teleport <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /teleport <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      val playerLocation = specifiedPlayer.getLocation
      val playerSender = sender.asInstanceOf[Player]
      playerSender.teleport(playerLocation)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexWorld] You have successfully been teleported to " + specifiedPlayer.getPlayerListName + ".")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("lightning")){
      if(!sender.hasPermission("vertexscala.admin.lightning")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      // /lightning Swatcommader6
      // <PLAYER> is args 0
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /lightning <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /lightning <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      val specifiedLocation = specifiedPlayer.getLocation
      val worldName = specifiedPlayer.getWorld.getName
      Bukkit.getServer.getWorld(worldName).spawnEntity(specifiedLocation, EntityType.LIGHTNING)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexWorld] You have successfully sent a lightning strike to " + specifiedPlayer.getName + ".")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("checkupdates")){
      if(!sender.hasPermission("vertexscala.info.checkupdates")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      sender.sendMessage("" + ChatColor.GREEN + "[VertexUpdater] Checking for updates...")
      var latestVersion: String = null;
      try{
        val id: Int = 12793
        val con: HttpURLConnection = new URL("http://www.spigotmc.org/api/general.php").openConnection.asInstanceOf[HttpURLConnection]
        con.setDoOutput(true)
        con.setRequestMethod("POST")
        con.getOutputStream.write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + id).getBytes("UTF-8"))
        latestVersion = new BufferedReader(new InputStreamReader(con.getInputStream)).readLine
      } catch {
        case e: Exception => sender.sendMessage("Vertex had failed searching for updates.")
      }
      if(latestVersion == null){
        // DO NOTHING
        return true
      }else{
        if(!latestVersion.equals(this.getDescription.getVersion)){
          sender.sendMessage("" + ChatColor.RED + "Vertex has found a update! Update " + latestVersion + " is now available! Please upgrade!")
          return true
        }
        if(latestVersion.equals(this.getDescription.getVersion)){
          sender.sendMessage("" + ChatColor.GREEN + "Vertex " + this.getDescription.getVersion + " is at the latest version!")
          return true
        }
      }
    }
    if(cmd.getName.equalsIgnoreCase("heal")){
      if(!sender.hasPermission("vertexscala.admin.heal")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      // /heal <player>
      // args: 0 = <player>
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /heal <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /heal <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      specifiedPlayer.setHealth(20.0)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] You have successfully healed " + specifiedPlayer.getPlayerListName + "!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("feed") || cmd.getName.equalsIgnoreCase("eat")){
      if(!sender.hasPermission("vertexscala.admin.feed")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /feed <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /feed <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      specifiedPlayer.setFoodLevel(20)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] You have successfully gave food to " + specifiedPlayer.getPlayerListName + "!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("invsee")){
      if(!sender.hasPermission("vertexscala.admin.invsee")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /invsee <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /invsee <player>")
        return true
      }
      if(!sender.isInstanceOf[Player]){
        sender.sendMessage("" + ChatColor.RED + "Sorry, you are a instance of CONSOLE and cannot recieve interfaces.")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      val inventory = specifiedPlayer.getInventory
      val player = sender.asInstanceOf[Player]
      player.openInventory(inventory)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] You have successfully opened " + specifiedPlayer.getPlayerListName + "'s inventory!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("time")){
      if(!sender.hasPermission("vertexscala.admin.time")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /time <time>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /time <time>")
        return true
      }
      val timeSpecified = args{0}
      val intTime = Integer.parseInt(timeSpecified)
      val floatTime = intTime.toLong
      if(!sender.isInstanceOf[Player]){
        sender.sendMessage("" + ChatColor.RED + "Sorry, you cannot change the time in a CONSOLE instance.")
        return true
      }
      val player = sender.asInstanceOf[Player]
      val worldPlayerName = player.getWorld.getName
      Bukkit.getWorld(worldPlayerName).setTime(floatTime)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexWorld] You have successfully set the time!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("uuid")){
      if(!sender.hasPermission("vertexscala.info.uuid")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /uuid <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /uuid <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      val UUID = specifiedPlayer.getUniqueId
      sender.sendMessage("" + ChatColor.BLUE + "" + ChatColor.BOLD + specifiedPlayer.getPlayerListName)
      sender.sendMessage("" + ChatColor.GREEN + "The UUID for the player is: " + UUID )
      return true
    }
    if(cmd.getName.equalsIgnoreCase("ban-ip")){
      if(!sender.hasPermission("vertexscala.admin.banip")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /ban-ip <ip>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /ban-ip <ip>")
        return true
      }
      Bukkit.banIP(args{0})
      sender.sendMessage("" + ChatColor.GREEN + "[VertexBan] IP " + args{0} + " has been banned!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("unbanip")){
      if(!sender.hasPermission("vertexscala.admin.unbanip")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /unbanip <ip>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /unbanip <ip>")
        return true
      }
      Bukkit.unbanIP(args{0})
      sender.sendMessage("" + ChatColor.GREEN + "[VertexBan] IP " + args{0} + " has been unbanned!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("explode")){
      if(!sender.hasPermission("vertexscala.admin.explode")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /explode <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /explode <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      val specifiedWorld = specifiedPlayer.getWorld
      val playerLocation = specifiedPlayer.getLocation
      Bukkit.getWorld(specifiedWorld.getName).createExplosion(playerLocation, 20)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] You have successfully sent a explosion towards " + specifiedPlayer.getName + ".")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("clear")){
      if(!sender.hasPermission("vertexscala.admin.clear")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /clear <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /clear <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      specifiedPlayer.getInventory.clear()
      sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] You have successfully cleared the inventory of " + specifiedPlayer.getPlayerListName + "!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("fly")){
      if(!sender.hasPermission("vertexscala.admin.fly")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /fly <flyboolean>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /fly <flyboolean>")
        return true
      }
      val booleanArg = args{0}
      if(!sender.isInstanceOf[Player]){
        sender.sendMessage("" + ChatColor.RED + "Sorry, the CONSOLE instance cannot not issue this command.")
        return true
      }
      val playerInstance = sender.asInstanceOf[Player]
      if(booleanArg.equalsIgnoreCase("true")){
        playerInstance.setAllowFlight(true)
        sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] You have successfully set your allow flight value to TRUE!")
        return true
      }
      if(booleanArg.equalsIgnoreCase("false")){
        playerInstance.setAllowFlight(false)
        sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] You have successfully set your allow flight value to FALSE!")
        return true
      }
      sender.sendMessage("" + ChatColor.RED + "Sorry, that value was invalid. Please try again: /fly <flyboolean>")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("permcheck")){
      if(!sender.hasPermission("vertexscala.admin.permcheck")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 2){
        sender.sendMessage("" + ChatColor.RED + "Usage: /permcheck <player> <permission>")
        return true
      }
      if(args.length < 2){
        sender.sendMessage("" + ChatColor.RED + "Usage: /permcheck <player> <permission>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      val permissionArg = args{1}
      if(args{1} == ""){
        sender.sendMessage("" + ChatColor.RED + "The permission couldn't be checked because it was invalid.")
        return true
      }
      if(specifiedPlayer.hasPermission(permissionArg)){
        sender.sendMessage("" + ChatColor.GREEN + "[VertexPlugin] Player " + specifiedPlayer.getPlayerListName + " has the specified permission!")
        return true
      }
      if(!specifiedPlayer.hasPermission(permissionArg)){
        sender.sendMessage("" + ChatColor.RED + "[VertexPlugin] Player " + specifiedPlayer.getPlayerListName + " doesn't have the specified permission!")
        return true
      }
      sender.sendMessage("" + ChatColor.RED + "Vertex encountered a error when trying to issue this command, please report this error to the author!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("op")){
      if(!sender.hasPermission("vertexscala.admin.op")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /op <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /op <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      specifiedPlayer.setOp(true)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexServer] You have successfully op " + specifiedPlayer.getPlayerListName + "!")
      return true
    }
    if(cmd.getName.equalsIgnoreCase("deop")){
      if(!sender.hasPermission("vertexscala.admin.deop")){
        sender.sendMessage(ERROR_MESSAGE)
        return true
      }
      if(args.length > 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /deop <player>")
        return true
      }
      if(args.length < 1){
        sender.sendMessage("" + ChatColor.RED + "Usage: /deop <player>")
        return true
      }
      val specifiedPlayer = Bukkit.getPlayer(args{0})
      if(specifiedPlayer == null){
        sender.sendMessage(NULL_ERROR)
        return true
      }
      specifiedPlayer.setOp(false)
      sender.sendMessage("" + ChatColor.GREEN + "[VertexServer] You have successfully deop " + specifiedPlayer.getPlayerListName + "!")
      return true
    }
    return false // if commandengine fails, this will return a false boolean
  }
}
