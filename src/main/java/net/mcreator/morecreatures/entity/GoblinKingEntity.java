
package net.mcreator.morecreatures.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.block.material.Material;
import net.minecraft.block.Blocks;

import net.mcreator.morecreatures.itemgroup.MoreCreaturesItemGroup;
import net.mcreator.morecreatures.TiegeratorsCreaturesModElements;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@TiegeratorsCreaturesModElements.ModElement.Tag
public class GoblinKingEntity extends TiegeratorsCreaturesModElements.ModElement {
	public static EntityType entity = null;
	public GoblinKingEntity(TiegeratorsCreaturesModElements instance) {
		super(instance, 19);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@Override
	public void initElements() {
		entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.CREATURE).setShouldReceiveVelocityUpdates(true)
				.setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new).size(0.6f, 1.8f)).build("goblin_king")
						.setRegistryName("goblin_king");
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -10053376, -3355648, new Item.Properties().group(MoreCreaturesItemGroup.tab))
				.setRegistryName("goblin_king_spawn_egg"));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
			boolean biomeCriteria = false;
			if (ForgeRegistries.BIOMES.getKey(biome).equals(new ResourceLocation("tiegerators_creatures:goblin_plains")))
				biomeCriteria = true;
			if (!biomeCriteria)
				continue;
			biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(entity, 5, 1, 1));
		}
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos,
						random) -> (world.getBlockState(pos.down()).getMaterial() == Material.ORGANIC && world.getLightSubtracted(pos, 0) > 8));
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(entity, renderManager -> {
			return new MobRenderer(renderManager, new Modelgoblin_king(), 0.5f) {
				@Override
				public ResourceLocation getEntityTexture(Entity entity) {
					return new ResourceLocation("tiegerators_creatures:textures/goblin_king_tex.png");
				}
			};
		});
	}
	public static class CustomEntity extends MonsterEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 7;
			setNoAI(false);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(5, new SwimGoal(this));
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.ILLAGER;
		}

		protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
			super.dropSpecialItems(source, looting, recentlyHitIn);
			this.entityDropItem(new ItemStack(Blocks.GOLD_BLOCK, (int) (1)));
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
		}

		@Override
		public boolean attackEntityFrom(DamageSource source, float amount) {
			if (source.getImmediateSource() instanceof ArrowEntity)
				return false;
			if (source == DamageSource.FALL)
				return false;
			if (source == DamageSource.DROWN)
				return false;
			return super.attackEntityFrom(source, amount);
		}

		@Override
		protected void registerAttributes() {
			super.registerAttributes();
			if (this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED) != null)
				this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
			if (this.getAttribute(SharedMonsterAttributes.MAX_HEALTH) != null)
				this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
			if (this.getAttribute(SharedMonsterAttributes.ARMOR) != null)
				this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4);
			if (this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) == null)
				this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5);
			if (this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK) == null)
				this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK);
			this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(0.5D);
		}
	}

	// Made with Blockbench 3.8.3
	// Exported for Minecraft version 1.15 - 1.16
	// Paste this class into your mod and generate all required imports
	public static class Modelgoblin_king extends EntityModel<Entity> {
		private final ModelRenderer head;
		private final ModelRenderer body;
		private final ModelRenderer left_arm;
		private final ModelRenderer right_arm;
		private final ModelRenderer left_leg;
		private final ModelRenderer right_leg;
		public Modelgoblin_king() {
			textureWidth = 64;
			textureHeight = 64;
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, 1.0F, 0.0F);
			head.setTextureOffset(0, 20).addBox(-3.0F, 7.0F, -4.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
			head.setTextureOffset(0, 0).addBox(-1.0F, 10.0F, -5.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(18, 20).addBox(3.0F, 4.0F, -2.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(0, 20).addBox(-4.0F, 4.0F, -2.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(22, 20).addBox(2.0F, 5.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(0, 11).addBox(2.0F, 6.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			head.setTextureOffset(0, 4).addBox(-3.0F, 6.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
			head.setTextureOffset(0, 32).addBox(-3.0F, 6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(28, 31).addBox(2.0F, 6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(31, 21).addBox(2.0F, 5.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(29, 4).addBox(2.0F, 5.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(26, 5).addBox(2.0F, 5.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(23, 4).addBox(-3.0F, 5.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(31, 19).addBox(-3.0F, 5.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(30, 6).addBox(-3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(22, 11).addBox(-1.0F, 5.0F, -4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(0, 14).addBox(-1.0F, 5.0F, 1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(22, 13).addBox(-3.0F, 5.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(23, 2).addBox(-3.0F, 6.0F, 1.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(23, 0).addBox(-3.0F, 6.0F, -4.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
			body = new ModelRenderer(this);
			body.setRotationPoint(0.0F, -6.0F, 0.0F);
			body.setTextureOffset(0, 11).addBox(-4.0F, 20.0F, -4.0F, 8.0F, 3.0F, 6.0F, 0.0F, false);
			body.setTextureOffset(0, 0).addBox(-4.0F, 23.0F, -5.0F, 8.0F, 4.0F, 7.0F, 0.0F, false);
			left_arm = new ModelRenderer(this);
			left_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
			left_arm.setTextureOffset(28, 8).addBox(9.0F, 12.0F, -2.0F, 2.0F, 8.0F, 3.0F, 0.0F, false);
			right_arm = new ModelRenderer(this);
			right_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
			right_arm.setTextureOffset(24, 20).addBox(-11.0F, 12.0F, -2.0F, 2.0F, 8.0F, 3.0F, 0.0F, false);
			left_leg = new ModelRenderer(this);
			left_leg.setRotationPoint(-1.9F, 12.0F, 0.0F);
			left_leg.setTextureOffset(30, 31).addBox(2.9F, 9.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
			right_leg = new ModelRenderer(this);
			right_leg.setRotationPoint(1.9F, 12.0F, 0.0F);
			right_leg.setTextureOffset(22, 31).addBox(-4.9F, 9.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			head.render(matrixStack, buffer, packedLight, packedOverlay);
			body.render(matrixStack, buffer, packedLight, packedOverlay);
			left_arm.render(matrixStack, buffer, packedLight, packedOverlay);
			right_arm.render(matrixStack, buffer, packedLight, packedOverlay);
			left_leg.render(matrixStack, buffer, packedLight, packedOverlay);
			right_leg.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
			this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
			this.right_arm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
			this.left_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
			this.left_arm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
			this.right_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		}
	}
}
