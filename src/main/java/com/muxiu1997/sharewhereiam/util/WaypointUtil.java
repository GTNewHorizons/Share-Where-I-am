package com.muxiu1997.sharewhereiam.util;

import static journeymap.client.cartography.RGB.WHITE_RGB;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;

import com.muxiu1997.sharewhereiam.localization.Lang;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import journeymap.client.model.Waypoint;

@SideOnly(Side.CLIENT)
public class WaypointUtil {

    public static PlayerWaypoint waypointOfLocation() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        Waypoint waypoint = Waypoint.of(player);
        waypoint.setName(player.getDisplayName());
        waypoint.setColor(WHITE_RGB);
        return new PlayerWaypoint(player, waypoint);
    }

    public static PlayerWaypoint waypointOfRayTrace() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        MovingObjectPosition position = player.rayTrace(128.0, 1.0f);
        Waypoint waypoint = Waypoint.at(
                position.blockX,
                position.blockY,
                position.blockZ,
                Waypoint.Type.Normal,
                player.worldObj.provider.dimensionId);
        waypoint.setName(player.getDisplayName());
        waypoint.setColor(WHITE_RGB);
        return new PlayerWaypoint(player, waypoint);
    }

    public static class PlayerWaypoint {

        public final EntityPlayer player;
        public final Waypoint waypoint;

        public PlayerWaypoint(EntityPlayer player, Waypoint waypoint) {
            this.player = player;
            this.waypoint = waypoint;
        }
    }

    public static String toBase64(Waypoint waypoint) {
        String waypointJSON = waypoint.toString();
        return Base64.getEncoder().encodeToString(waypointJSON.getBytes(StandardCharsets.UTF_8));
    }

    public static Waypoint fromBase64(String base64) {
        String waypointJSON = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
        return Waypoint.fromString(waypointJSON);
    }

    public static void addShareWaypointChat(String playerName, Waypoint waypoint, String additionalInformation) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        String waypointBase64 = toBase64(waypoint);

        IChatComponent chatPartA = new ChatComponentText("").appendSibling(
                new ChatComponentText("[JourneyMap]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)))
                .appendText(" ")
                .appendSibling(
                        new ChatComponentText(playerName)
                                .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)))
                .appendText(" ").appendText(Lang.SHARE_WAYPOINT_MSG.translate());

        IChatComponent chatPartB = new ChatComponentText("")
                .appendSibling(new ChatComponentText(waypoint.getName()).setChatStyle(new ChatStyle().setBold(true)))
                .appendText(" ")
                .appendSibling(
                        new ChatComponentText(
                                String.format("(%d, %d, %d)", waypoint.getX(), waypoint.getY(), waypoint.getZ()))
                                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)))
                .appendText(" ")
                // appendSaveAction
                .appendSibling(
                        new ChatComponentText("[Save]").setChatStyle(
                                new ChatStyle().setBold(true).setColor(EnumChatFormatting.BLUE).setChatHoverEvent(
                                        new HoverEvent(
                                                HoverEvent.Action.SHOW_TEXT,
                                                new ChatComponentText(Lang.SHARE_WAYPOINT_SAVE_DESC.translate())))
                                        .setChatClickEvent(
                                                new ClickEvent(
                                                        ClickEvent.Action.RUN_COMMAND,
                                                        "/savewaypoint " + waypointBase64 + " false"))))
                // appendEditAction
                .appendText(" ")
                .appendSibling(
                        new ChatComponentText("[Edit]").setChatStyle(
                                new ChatStyle().setBold(true).setColor(EnumChatFormatting.BLUE).setChatHoverEvent(
                                        new HoverEvent(
                                                HoverEvent.Action.SHOW_TEXT,
                                                new ChatComponentText(Lang.SHARE_WAYPOINT_EDIT_DESC.translate())))
                                        .setChatClickEvent(
                                                new ClickEvent(
                                                        ClickEvent.Action.RUN_COMMAND,
                                                        "/savewaypoint " + waypointBase64))))
                // appendToggleAction
                .appendText(" ").appendSibling(
                        new ChatComponentText("[Toggle]").setChatStyle(
                                new ChatStyle().setBold(true).setColor(EnumChatFormatting.BLUE).setChatHoverEvent(
                                        new HoverEvent(
                                                HoverEvent.Action.SHOW_TEXT,
                                                new ChatComponentText(Lang.SHARE_WAYPOINT_TOGGLE_DESC.translate())))
                                        .setChatClickEvent(
                                                new ClickEvent(
                                                        ClickEvent.Action.RUN_COMMAND,
                                                        "/toggletempbeacon " + waypointBase64))));

        IChatComponent chatPartC = (additionalInformation != null && !additionalInformation.isEmpty())
                ? new ChatComponentText(additionalInformation)
                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY))
                : null;

        player.addChatComponentMessage(chatPartA);
        player.addChatComponentMessage(chatPartB);

        if (chatPartC != null) {
            player.addChatComponentMessage(chatPartC);
        }
    }

    public static Waypoint waypointFromString(String s) {
        return Waypoint.fromString(s);
    }
}
