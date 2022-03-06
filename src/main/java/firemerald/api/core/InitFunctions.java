package firemerald.api.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import firemerald.api.core.blocks.ICustomInventoryState;
import firemerald.api.core.blocks.ICustomStateMapper;
import firemerald.api.core.items.ICustomSubtypes;
import firemerald.api.core.items.IIgnoreSubtypes;
import firemerald.api.core.items.IItemSubtyped;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class InitFunctions
{
	public static <T extends Item> T addItem(T item, String name, IForgeRegistry<Item> registry)
	{
		registry.register(item.setRegistryName(name).setUnlocalizedName(name));
		return item;
	}

	public static ItemBlock addItemBlock(Block block, IForgeRegistry<Item> registry)
	{
		ItemBlock item = new ItemBlock(block);
		registry.register(item.setRegistryName(block.getRegistryName().toString()).setUnlocalizedName(block.getUnlocalizedName().substring(5)).setCreativeTab(block.getCreativeTabToDisplayOn()));
		return item;
	}

	public static <T extends ItemBlock> T addItemBlock(T item, IForgeRegistry<Item> registry)
	{
		Block block = item.getBlock();
		registry.register(item.setRegistryName(block.getRegistryName().toString()).setUnlocalizedName(block.getUnlocalizedName().substring(5)));
		return item;
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModels(Item item)
	{
		if (item instanceof ICustomSubtypes) ((ICustomSubtypes) item).registerModels();
		else
		{
			ModelBakery.registerItemVariants(item, item.getRegistryName());
			if (item.getHasSubtypes())
			{
				NonNullList<ItemStack> toReg = NonNullList.create();
				item.getSubItems(CreativeTabs.SEARCH, toReg);
				if (item instanceof IItemSubtyped)
				{
					IItemSubtyped subTyped = (IItemSubtyped) item;
					for (ItemStack stack : toReg) ModelLoader.setCustomModelResourceLocation(item, stack.getItemDamage(), new ModelResourceLocation(item.getRegistryName().toString() + "/" + subTyped.getSubtype(stack.getItemDamage()).replace('.', '/')));
				}
				else if (item instanceof IIgnoreSubtypes) for (ItemStack stack : toReg) ModelLoader.setCustomModelResourceLocation(item, stack.getItemDamage(), new ModelResourceLocation(item.getRegistryName().toString()));
				else for (ItemStack stack : toReg) ModelLoader.setCustomModelResourceLocation(item, stack.getItemDamage(), new ModelResourceLocation(item.getRegistryName().toString() + "_" + stack.getItemDamage()));
			}
			else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
		}
	}
	/*
	@SideOnly(Side.CLIENT)
	public static void registerFluidItemModels(Item item)
	{
		ModelResourceLocation location = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomMeshDefinition(FILLED_BOTTLE, stack -> location);
        ModelBakery.registerItemVariants(FILLED_BOTTLE, location);
	}
	*/
	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModels(ItemBlock item)
	{
		if (item instanceof ICustomSubtypes) ((ICustomSubtypes) item).registerModels();
		else
		{
			Block block = item.getBlock();
			ResourceLocation registryName = block.getRegistryName();
			if (block instanceof ICustomStateMapper && ((ICustomStateMapper) block).useForItem())
			{
				IStateMapper mapper = ((ICustomStateMapper) block).getStateMapper();
				Map<IBlockState, ModelResourceLocation> map = mapper.putStateModelLocations(block);
				List<Integer> used = new ArrayList<>();
				map.forEach((state, model) -> {
					int meta = block.damageDropped(state);
					if (!used.contains(meta))
					{
						used.add(meta);
						ModelLoader.setCustomModelResourceLocation(item, meta, model);
					}
				});
			}
			else if (item.getHasSubtypes())
			{
				NonNullList<ItemStack> toReg = NonNullList.create();
				item.getSubItems(CreativeTabs.SEARCH, toReg);
				for (ItemStack stack : toReg)
				{
					@SuppressWarnings("deprecation")
					IBlockState state = block instanceof ICustomInventoryState ? ((ICustomInventoryState) block).getState(stack.getItemDamage()) : block.getStateFromMeta(item.getMetadata(stack.getItemDamage()));
			        StringBuilder stringbuilder = new StringBuilder();
			        if (!state.getProperties().isEmpty()) COMMA_JOINER.appendTo(stringbuilder, Iterables.transform(state.getProperties().entrySet(), MAP_ENTRY_TO_STRING));
					ModelLoader.setCustomModelResourceLocation(item, stack.getItemDamage(), new ModelResourceLocation(registryName, stringbuilder.toString()));
				}
			}
			else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(registryName.toString()));
		}
	}

    public static final Joiner COMMA_JOINER = Joiner.on(',');

    public static final Function<Entry<IProperty<?>, Comparable<?>>, String> MAP_ENTRY_TO_STRING = new Function<Entry<IProperty<?>, Comparable<?>>, String>()
    {
        @Override
		@Nullable
        public String apply(@Nullable Entry<IProperty<?>, Comparable<?>> p_apply_1_)
        {
            if (p_apply_1_ == null) return "<NULL>";
            else
            {
                IProperty<?> iproperty = p_apply_1_.getKey();
                return iproperty.getName() + "=" + this.getPropertyName(iproperty, p_apply_1_.getValue());
            }
        }

        @SuppressWarnings("unchecked")
		private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> entry)
        {
            return property.getName((T) entry);
        }
    };

	public static <T extends Block> T addBlock(T block, String name, IForgeRegistry<Block> registry)
	{
		registry.register(block.setRegistryName(name).setUnlocalizedName(name));
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT && block instanceof ICustomStateMapper) ModelLoader.setCustomStateMapper(block, ((ICustomStateMapper) block).getStateMapper());
		return block;
	}
}
