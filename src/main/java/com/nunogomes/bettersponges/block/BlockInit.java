package com.nunogomes.bettersponges.block;

import java.util.function.Supplier;
import com.nunogomes.bettersponges.BetterSponges;
import com.nunogomes.bettersponges.block.CustomBlocks.CompactSponge;
import com.nunogomes.bettersponges.block.CustomBlocks.SuperSponge;
import com.nunogomes.bettersponges.block.CustomBlocks.WetCompactSponge;
import com.nunogomes.bettersponges.block.CustomBlocks.WetSuperSponge;
import com.nunogomes.bettersponges.item.ItemInit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
  public static final DeferredRegister<Block> BLOCKS = 
      DeferredRegister.create(ForgeRegistries.BLOCKS, BetterSponges.MOD_ID);

  public static final RegistryObject<Block> COMPACT_SPONGE = registerBlock("compact_sponge", 
      () -> new CompactSponge(5), CreativeModeTab.TAB_BUILDING_BLOCKS);
      
  public static final RegistryObject<Block> WET_COMPACT_SPONGE = registerBlock("wet_compact_sponge", 
      () -> new WetCompactSponge(), CreativeModeTab.TAB_BUILDING_BLOCKS);

  public static final RegistryObject<Block> SUPER_SPONGE = registerBlock("super_sponge", 
      () -> new SuperSponge(10), CreativeModeTab.TAB_BUILDING_BLOCKS);

  public static final RegistryObject<Block> WET_SUPER_SPONGE = registerBlock("wet_super_sponge", 
      () -> new WetSuperSponge(), CreativeModeTab.TAB_BUILDING_BLOCKS);


  private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
    RegistryObject<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn, tab);
    return toReturn;
  }

  private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
    return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
  }

  
  
  public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
}
