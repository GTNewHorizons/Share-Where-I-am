package com.muxiu1997.sharewhereiam.network

import com.muxiu1997.sharewhereiam.util.WaypointUtil
import cpw.mods.fml.common.network.ByteBufUtils
import cpw.mods.fml.common.network.simpleimpl.IMessage
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler
import cpw.mods.fml.common.network.simpleimpl.MessageContext
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import io.netty.buffer.ByteBuf

class MessageShareWaypoint : IMessage {
  lateinit var playerName: String
  lateinit var waypointJson: String
  lateinit var additionalInformation: String

  @Suppress("unused") constructor()

  @SideOnly(Side.CLIENT)
  constructor(playerWaypoint: WaypointUtil.PlayerWaypoint) : this(playerWaypoint, "")

  @SideOnly(Side.CLIENT)
  constructor(playerWaypoint: WaypointUtil.PlayerWaypoint, additionalInformation: String) {
    this.playerName = playerWaypoint.player.displayName
    this.waypointJson = playerWaypoint.waypoint.toString()
    this.additionalInformation = additionalInformation
  }

  override fun fromBytes(buf: ByteBuf) {
    playerName = ByteBufUtils.readUTF8String(buf)
    waypointJson = ByteBufUtils.readUTF8String(buf)
    additionalInformation = ByteBufUtils.readUTF8String(buf)
  }

  override fun toBytes(buf: ByteBuf) {
    ByteBufUtils.writeUTF8String(buf, playerName)
    ByteBufUtils.writeUTF8String(buf, waypointJson)
    ByteBufUtils.writeUTF8String(buf, additionalInformation)
  }

  companion object {
    object Handler :
        IMessageHandler<MessageShareWaypoint, IMessage?>, IClientSideHandler, IServerSideHandler {
      override fun onMessage(message: MessageShareWaypoint, ctx: MessageContext): IMessage? {
        if (ctx.side.isClient) {
          handleClientSideMessage(message)
        } else {
          handleServerSideMessage(message)
        }
        return null
      }

      @SideOnly(Side.CLIENT)
      private fun handleClientSideMessage(message: MessageShareWaypoint) {
        if (WaypointUtil.isJourneyMapLoaded)
            WaypointUtil.addShareWaypointChat(
                message.playerName,
                WaypointUtil.waypointFromString(
                    message
                        .waypointJson), // Dereferences JourneyMap class import to stop crashes on
                // clients that don't have JourneyMap installed.
                message.additionalInformation)
      }

      private fun handleServerSideMessage(message: MessageShareWaypoint) {
        network.sendToAll(message)
      }
    }
  }
}
