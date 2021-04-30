
package net.mcreator.morecreatures.item;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.world.World;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.IItemTier;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.block.Blocks;

import net.mcreator.morecreatures.itemgroup.MoreCreaturesItemGroup;
import net.mcreator.morecreatures.TiegeratorsCreaturesModElements;

import java.util.List;

@TiegeratorsCreaturesModElements.ModElement.Tag
public class BoneClubItem extends TiegeratorsCreaturesModElements.ModElement {
	@ObjectHolder("tiegerators_creatures:bone_club")
	public static final Item block = null;
	public BoneClubItem(TiegeratorsCreaturesModElements instance) {
		super(instance, 13);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new SwordItem(new IItemTier() {
			public int getMaxUses() {
				return 555;
			}

			public float getEfficiency() {
				return 4f;
			}

			public float getAttackDamage() {
				return 2f;
			}

			public int getHarvestLevel() {
				return 1;
			}

			public int getEnchantability() {
				return 2;
			}

			public Ingredient getRepairMaterial() {
				return Ingredient.fromStacks(new ItemStack(Blocks.BONE_BLOCK, (int) (1)));
			}
		}, 3, -3f, new Item.Properties().group(MoreCreaturesItemGroup.tab)) {
			@Override
			public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
				super.addInformation(itemstack, world, list, flag);
				list.add(new StringTextComponent("An easy build Weapon from the Goblins"));
			}
		}.setRegistryName("bone_club"));
	}
}
