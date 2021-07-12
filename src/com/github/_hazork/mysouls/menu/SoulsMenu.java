package com.github._hazork.mysouls.menu;

import com.github._hazork.mysouls.SoulsPlugin;
import com.github._hazork.mysouls.data.lang.LangEnum;
import com.github._hazork.mysouls.souls.PlayerSoul;
import com.github._hazork.mysouls.souls.SoulCommunicator;
import com.github.arthurfiorette.sinklibrary.components.SinkPlugin;
import com.github.arthurfiorette.sinklibrary.item.ItemBuilders;
import com.github.arthurfiorette.sinklibrary.menu.PageableMenu;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SoulsMenu extends PageableMenu<PlayerSoul> {

  public SoulsMenu(final SinkPlugin plugin, final Player player) {
    super(plugin, player, "", 6);
  }

  @Override
  protected int[] pageableSlots() {
    return new int[] { 1, 2, 3, 4, 5 };
  }

  @Override
  protected Collection<PlayerSoul> requestValues() {
    return SoulsPlugin.get().getStorage().get(getPlayer().getUniqueId()).getKeySet();
  }

  @Override
  protected MenuItem toItem(final PlayerSoul ps) {
    final OfflinePlayer player = Bukkit.getOfflinePlayer(ps.getOwnerId());
    final Replacer name = new Replacer().add("{player}", player.getName());
    final ItemStack item = ItemBuilders
      .ofHead(player)
      .setAmount(ps.amount())
      .setName(LangEnum.INVENTORY_SOUL_NAME.getText(name))
      .setLore(LangEnum.INVENTORY_SOUL_LORE.getList())
      .build();

    final MenuItem menu = new MenuItem(
      item,
      (is, type) -> SoulCommunicator.get(player).collectSouls(is)
    );
    return menu;
  }

  @Override
  protected List<MenuItem> items() {
    // TODO Auto-generated method stub
    return null;
  }
}
