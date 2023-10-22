package com.muxiu1997.sharewhereiam.util

import com.muxiu1997.sharewhereiam.localization.Lang
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import java.nio.charset.StandardCharsets
import java.util.*
import journeymap.client.cartography.RGB.WHITE_RGB
import journeymap.client.model.Waypoint
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent

@SideOnly(Side.CLIENT)
object WaypointUtil {
  fun waypointOfLocation(): PlayerWaypoint {
    val player = Minecraft.getMinecraft().thePlayer
    val waypoint =
        Waypoint.of(player).apply {
          name = player.displayName
          color = WHITE_RGB
        }
    return PlayerWaypoint(player, waypoint)
  }

  fun waypointOfRayTrace(): PlayerWaypoint {
    val player = Minecraft.getMinecraft().thePlayer
    val position = player.rayTrace(128.0, 1.0f)
    val waypoint =
        Waypoint.at(
                position.blockX,
                position.blockY,
                position.blockZ,
                Waypoint.Type.Normal,
                player.worldObj.provider.dimensionId)
            .apply {
              name = player.displayName
              color = WHITE_RGB
            }
    return PlayerWaypoint(player, waypoint)
  }

  data class PlayerWaypoint(val player: EntityPlayer, val waypoint: Waypoint)

  fun toBase64(waypoint: Waypoint): String {
    val waypointJSON = waypoint.toString()
    return Base64.getEncoder().encodeToString(waypointJSON.toByteArray(StandardCharsets.UTF_8))
  }

  fun fromBase64(base64: String?): Waypoint {
    val waypointJSON = String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8)
    return Waypoint.fromString(waypointJSON)
  }

  fun addShareWaypointChat(playerName: String, waypoint: Waypoint, additionalInformation: String?) {
    val player = Minecraft.getMinecraft().thePlayer
    val waypointBase64 = toBase64(waypoint)
    val chatPartA =
        ChatComponentText("")
            .appendSibling(
                ChatComponentText("[JourneyMap]")
                    .setChatStyle(ChatStyle().setColor(EnumChatFormatting.YELLOW)))
            .appendText(" ")
            .appendSibling(
                ChatComponentText(playerName)
                    .setChatStyle(ChatStyle().setColor(EnumChatFormatting.GREEN)))
            .appendText(" ")
            .appendText(Lang.SHARE_WAYPOINT_MSG())

    val chatPartB =
        ChatComponentText("")
            .appendSibling(ChatComponentText(waypoint.name).setChatStyle(ChatStyle().setBold(true)))
            .appendText(" ")
            .appendSibling(
                ChatComponentText(String.format("(%d, %d, %d)", waypoint.x, waypoint.y, waypoint.z))
                    .setChatStyle(ChatStyle().setColor(EnumChatFormatting.GRAY)))
            .appendSaveAction(waypointBase64)
            .appendEditAction(waypointBase64)
            .appendToggleAction(waypointBase64)

    val chatPartC =
        additionalInformation?.let {
          if (it.isEmpty()) {
            null
          } else {
            ChatComponentText(it).setChatStyle(ChatStyle().setColor(EnumChatFormatting.GRAY))
          }
        }

    player.addChatComponentMessage(chatPartA)
    player.addChatComponentMessage(chatPartB)
    chatPartC ?: return
    player.addChatComponentMessage(chatPartC)
  }

  private fun IChatComponent.appendSaveAction(waypointBase64: String): IChatComponent {
    return this.appendText(" ")
        .appendSibling(
            ChatComponentText("[${Lang.SHARE_WAYPOINT_SAVE()}]")
                .setChatStyle(
                    ChatStyle()
                        .setBold(true)
                        .setColor(EnumChatFormatting.BLUE)
                        .setChatHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                ChatComponentText(Lang.SHARE_WAYPOINT_SAVE_DESC())))
                        .setChatClickEvent(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/savewaypoint ${waypointBase64} false"))))
  }

  private fun IChatComponent.appendEditAction(waypointBase64: String): IChatComponent {
    return this.appendText(" ")
        .appendSibling(
            ChatComponentText("[${Lang.SHARE_WAYPOINT_EDIT()}]")
                .setChatStyle(
                    ChatStyle()
                        .setBold(true)
                        .setColor(EnumChatFormatting.BLUE)
                        .setChatHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                ChatComponentText(Lang.SHARE_WAYPOINT_EDIT_DESC())))
                        .setChatClickEvent(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND, "/savewaypoint $waypointBase64"))))
  }

  private fun IChatComponent.appendToggleAction(waypointBase64: String): IChatComponent {
    return this.appendText(" ")
        .appendSibling(
            ChatComponentText("[${Lang.SHARE_WAYPOINT_TOGGLE()}]")
                .setChatStyle(
                    ChatStyle()
                        .setBold(true)
                        .setColor(EnumChatFormatting.BLUE)
                        .setChatHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                ChatComponentText(Lang.SHARE_WAYPOINT_TOGGLE_DESC())))
                        .setChatClickEvent(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/toggletempbeacon $waypointBase64"))))
  }
  /**
    * Dereferences JourneyMap class to avoid crashes on clients that don't have JourneyMap installed.
    */
  fun waypointFromString(s: String): Waypoint {
      return Waypoint.fromString(s);
  }
  
  // Caches isModLoaded result to remove overhead from repeated calls.
  val isJourneyMapLoaded: Boolean = Loader.isModLoaded("journeymap");
}
