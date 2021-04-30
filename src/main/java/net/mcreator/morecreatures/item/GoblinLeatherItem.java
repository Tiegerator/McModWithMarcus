
package net.mcreator.morecreatures.item;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.world.World;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.block.BlockState;

import net.mcreator.morecreatures.itemgroup.MoreCreaturesItemGroup;
import net.mcreator.morecreatures.TiegeratorsCreaturesModElements;

import java.util.List;

@TiegeratorsCreaturesModElements.ModElement.Tag
public class GoblinLeatherItem extends TiegeratorsCreaturesModElements.ModElement {
	@ObjectHolder("tiegerators_creatures:goblin_leather")
	public static final Item block = null;
	public GoblinLeatherItem(TiegeratorsCreaturesModElements instance) {
		super(instance, 5);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}
	public static class ItemCustom extends Item {
		public ItemCustom() {
			super(new Item.Properties().group(MoreCreaturesItemGroup.tab).maxStackSize(64).rarity(Rarity.RARE));
			setRegistryName("goblin_leather");
		}

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
			return 1F;
		}

		@Override
		public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
			super.addInformation(itemstack, world, list, flag);
			list.add(new StringTextComponent("Yeah its that what it sounds like... Perhaps you want to craft an Armor from this"));
		}
	}
}
