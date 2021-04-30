
package net.mcreator.morecreatures.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;

import net.mcreator.morecreatures.TiegeratorsCreaturesModElements;

@TiegeratorsCreaturesModElements.ModElement.Tag
public class MoreCreaturesItemGroup extends TiegeratorsCreaturesModElements.ModElement {
	public MoreCreaturesItemGroup(TiegeratorsCreaturesModElements instance) {
		super(instance, 2);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabmore_creatures") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(Items.PLAYER_HEAD, (int) (1));
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
	public static ItemGroup tab;
}
