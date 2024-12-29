package com.muxiu1997.sharewhereiam.integration.journeymap;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import journeymap.client.model.Waypoint;

@SideOnly(Side.CLIENT)
public class WaypointMarker {

    public static void render(RenderWorldLastEvent event) {
        List<Waypoint> transientBeacons = WaypointManager.getTransientBeacons();
        if (transientBeacons.isEmpty()) return;

        float partialTicks = event.partialTicks;
        Minecraft minecraft = Minecraft.getMinecraft();
        double posX = minecraft.thePlayer.prevPosX
                + (minecraft.thePlayer.posX - minecraft.thePlayer.prevPosX) * partialTicks;
        double posY = minecraft.thePlayer.prevPosY
                + (minecraft.thePlayer.posY - minecraft.thePlayer.prevPosY) * partialTicks;
        double posZ = minecraft.thePlayer.prevPosZ
                + (minecraft.thePlayer.posZ - minecraft.thePlayer.prevPosZ) * partialTicks;

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslated(-posX, -posY, -posZ);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        GL11.glBegin(GL11.GL_QUADS);
        for (Waypoint waypoint : transientBeacons) {
            markWaypoint(waypoint);
        }
        GL11.glEnd();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private static void markWaypoint(Waypoint waypoint) {
        double x = waypoint.getX();
        double y = waypoint.getY();
        double z = waypoint.getZ();

        GL11.glColor4d(255.0, 255.0, 255.0, 0.8);

        // NORTH
        GL11.glVertex3d(x, y + 1, z);
        GL11.glVertex3d(x + 1, y + 1, z);
        GL11.glVertex3d(x + 1, y, z);
        GL11.glVertex3d(x, y, z);

        // EAST
        GL11.glVertex3d(x + 1, y, z + 1);
        GL11.glVertex3d(x + 1, y, z);
        GL11.glVertex3d(x + 1, y + 1, z);
        GL11.glVertex3d(x + 1, y + 1, z + 1);

        // SOUTH
        GL11.glVertex3d(x + 1, y + 1, z + 1);
        GL11.glVertex3d(x, y + 1, z + 1);
        GL11.glVertex3d(x, y, z + 1);
        GL11.glVertex3d(x + 1, y, z + 1);

        // WEST
        GL11.glVertex3d(x, y + 1, z + 1);
        GL11.glVertex3d(x, y + 1, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y, z + 1);

        // TOP
        GL11.glVertex3d(x, y + 1, z);
        GL11.glVertex3d(x, y + 1, z + 1);
        GL11.glVertex3d(x + 1, y + 1, z + 1);
        GL11.glVertex3d(x + 1, y + 1, z);

        // BOTTOM
        GL11.glVertex3d(x + 1, y, z);
        GL11.glVertex3d(x + 1, y, z + 1);
    }
}
