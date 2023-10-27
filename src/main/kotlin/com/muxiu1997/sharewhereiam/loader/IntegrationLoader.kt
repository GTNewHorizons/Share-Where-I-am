package com.muxiu1997.sharewhereiam.loader

import com.muxiu1997.sharewhereiam.integration.Mods
import java.util.Arrays

object IntegrationLoader {
  fun load() {
    Arrays.stream(Mods.values()).forEach { obj: Mods -> obj.check() }
  }
}
