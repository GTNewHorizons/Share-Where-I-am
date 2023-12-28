@file:JvmName("NetworkHandler")

package com.muxiu1997.sharewhereiam.network

import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper

@JvmField
val network: SimpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("sharewhereiam")

interface IServerSideHandler

interface IClientSideHandler
