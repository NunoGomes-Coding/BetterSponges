package com.nunogomes.bettersponges.item;

import com.nunogomes.bettersponges.BetterSponges;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
  public static final DeferredRegister<Item> ITEMS = 
    DeferredRegister.create(ForgeRegistries.ITEMS, BetterSponges.MOD_ID);

      
  public static void register(IEventBus eventBus) {
    ITEMS.register(eventBus);
  }
}
