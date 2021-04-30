
package net.mcreator.morecreatures.world.biome;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.Biome;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.block.Blocks;

import net.mcreator.morecreatures.TiegeratorsCreaturesModElements;

@TiegeratorsCreaturesModElements.ModElement.Tag
public class HellIncarnateBiome extends TiegeratorsCreaturesModElements.ModElement {
	@ObjectHolder("tiegerators_creatures:hell_incarnate")
	public static final CustomBiome biome = null;
	public HellIncarnateBiome(TiegeratorsCreaturesModElements instance) {
		super(instance, 22);
	}

	@Override
	public void initElements() {
		elements.biomes.add(() -> new CustomBiome());
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		BiomeDictionary.addTypes(biome, BiomeDictionary.Type.NETHER);
	}
	static class CustomBiome extends Biome {
		public CustomBiome() {
			super(new Biome.Builder().downfall(0.5f).depth(0.1f).scale(0.4f).temperature(0.5f).precipitation(Biome.RainType.RAIN)
					.category(Biome.Category.NETHER).waterColor(-3573743).waterFogColor(-3573743).parent("tiegerators_creatures:hell_incarnate")
					.surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(Blocks.SOUL_SAND.getDefaultState(),
							Blocks.NETHERRACK.getDefaultState(), Blocks.NETHERRACK.getDefaultState())));
			setRegistryName("hell_incarnate");
			DefaultBiomeFeatures.addCarvers(this);
			this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.GHAST, 8, 1, 1));
			this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.ZOMBIE_PIGMAN, 50, 1, 7));
			this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.BLAZE, 3, 1, 2));
		}

		@OnlyIn(Dist.CLIENT)
		@Override
		public int getGrassColor(double posX, double posZ) {
			return -6075116;
		}

		@OnlyIn(Dist.CLIENT)
		@Override
		public int getFoliageColor() {
			return -7268328;
		}

		@OnlyIn(Dist.CLIENT)
		@Override
		public int getSkyColor() {
			return -5688782;
		}
	}
}
