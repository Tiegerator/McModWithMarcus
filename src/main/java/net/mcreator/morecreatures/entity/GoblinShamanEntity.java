
package net.mcreator.morecreatures.entity;

import net.minecraftforge.registries.ObjectHolder;
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
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.goal.BreakDoorGoal;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.block.material.Material;

import net.mcreator.morecreatures.item.ShamanStaffItem;
import net.mcreator.morecreatures.item.GoblinLeatherItem;
import net.mcreator.morecreatures.TiegeratorsCreaturesModElements;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@TiegeratorsCreaturesModElements.ModElement.Tag
public class GoblinShamanEntity extends TiegeratorsCreaturesModElements.ModElement {
	public static EntityType entity = null;
	@ObjectHolder("tiegerators_creatures:entitybulletgoblin_shaman")
	public static final EntityType arrow = null;
	public GoblinShamanEntity(TiegeratorsCreaturesModElements instance) {
		super(instance, 7);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@Override
	public void initElements() {
		entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.CREATURE).setShouldReceiveVelocityUpdates(true)
				.setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new).immuneToFire().size(0.6f, 1.8f))
						.build("goblin_shaman").setRegistryName("goblin_shaman");
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -13395712, -13421824, new Item.Properties().group(ItemGroup.MISC))
				.setRegistryName("goblin_shaman_spawn_egg"));
		elements.entities.add(() -> (EntityType.Builder.<ArrowCustomEntity>create(ArrowCustomEntity::new, EntityClassification.MISC)
				.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(ArrowCustomEntity::new)
				.size(0.5f, 0.5f)).build("entitybulletgoblin_shaman").setRegistryName("entitybulletgoblin_shaman"));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
			boolean biomeCriteria = false;
			if (ForgeRegistries.BIOMES.getKey(biome).equals(new ResourceLocation("tiegerators_creatures:goblin_plains")))
				biomeCriteria = true;
			if (!biomeCriteria)
				continue;
			biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(entity, 20, 4, 4));
		}
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos,
						random) -> (world.getBlockState(pos.down()).getMaterial() == Material.ORGANIC && world.getLightSubtracted(pos, 0) > 8));
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(entity, renderManager -> {
			return new MobRenderer(renderManager, new Modelgoblin_shaman(), 0.5f) {
				@Override
				public ResourceLocation getEntityTexture(Entity entity) {
					return new ResourceLocation("tiegerators_creatures:textures/goblin_shaman_tex.png");
				}
			};
		});
		RenderingRegistry.registerEntityRenderingHandler(arrow,
				renderManager -> new SpriteRenderer(renderManager, Minecraft.getInstance().getItemRenderer()));
	}
	public static class CustomEntity extends MonsterEntity implements IRangedAttackMob {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 3;
			setNoAI(false);
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ShamanStaffItem.block, (int) (1)));
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
			this.goalSelector.addGoal(4, new FollowMobGoal(this, (float) 1, 10, 5));
			this.goalSelector.addGoal(5, new BreakDoorGoal(this, e -> true));
			this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(7, new SwimGoal(this));
			this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25, 20, 10) {
				@Override
				public boolean shouldContinueExecuting() {
					return this.shouldExecute();
				}
			});
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
			super.dropSpecialItems(source, looting, recentlyHitIn);
			this.entityDropItem(new ItemStack(GoblinLeatherItem.block, (int) (1)));
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
			if (source == DamageSource.CACTUS)
				return false;
			return super.attackEntityFrom(source, amount);
		}

		@Override
		protected void registerAttributes() {
			super.registerAttributes();
			if (this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED) != null)
				this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
			if (this.getAttribute(SharedMonsterAttributes.MAX_HEALTH) != null)
				this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(13);
			if (this.getAttribute(SharedMonsterAttributes.ARMOR) != null)
				this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1);
			if (this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) == null)
				this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5);
		}

		public void attackEntityWithRangedAttack(LivingEntity target, float flval) {
			ArrowCustomEntity entityarrow = new ArrowCustomEntity(arrow, this, this.world);
			double d0 = target.getPosY() + (double) target.getEyeHeight() - 1.1;
			double d1 = target.getPosX() - this.getPosX();
			double d3 = target.getPosZ() - this.getPosZ();
			entityarrow.shoot(d1, d0 - entityarrow.getPosY() + (double) MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F, d3, 1.6F, 12.0F);
			world.addEntity(entityarrow);
		}
	}

	@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
	private static class ArrowCustomEntity extends AbstractArrowEntity implements IRendersAsItem {
		public ArrowCustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			super(arrow, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, World world) {
			super(type, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, double x, double y, double z, World world) {
			super(type, x, y, z, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, LivingEntity entity, World world) {
			super(type, entity, world);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack getItem() {
			return new ItemStack(Items.MAGMA_CREAM, (int) (1));
		}

		@Override
		protected ItemStack getArrowStack() {
			return new ItemStack(Items.MAGMA_CREAM, (int) (1));
		}
	}

	// Made with Blockbench 3.8.3
	// Exported for Minecraft version 1.15 - 1.16
	// Paste this class into your mod and generate all required imports
	public static class Modelgoblin_shaman extends EntityModel<Entity> {
		private final ModelRenderer head;
		private final ModelRenderer body;
		private final ModelRenderer left_arm;
		private final ModelRenderer right_arm;
		private final ModelRenderer left_leg;
		private final ModelRenderer right_leg;
		public Modelgoblin_shaman() {
			textureWidth = 64;
			textureHeight = 64;
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, 0.0F, 0.0F);
			head.setTextureOffset(0, 0).addBox(-3.0F, 7.0F, -4.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
			head.setTextureOffset(0, 0).addBox(-1.0F, 10.0F, -5.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(20, 29).addBox(3.0F, 4.0F, -2.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(0, 12).addBox(-4.0F, 4.0F, -2.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(7, 22).addBox(-3.0F, 5.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
			head.setTextureOffset(18, 0).addBox(2.0F, 5.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
			body = new ModelRenderer(this);
			body.setRotationPoint(0.0F, 0.0F, 0.0F);
			body.setTextureOffset(0, 12).addBox(-4.0F, 13.0F, -3.0F, 8.0F, 5.0F, 5.0F, 0.0F, false);
			body.setTextureOffset(18, 0).addBox(-3.0F, 18.0F, -2.0F, 6.0F, 2.0F, 4.0F, 0.0F, false);
			left_arm = new ModelRenderer(this);
			left_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
			left_arm.setTextureOffset(10, 22).addBox(9.0F, 11.0F, -2.0F, 2.0F, 8.0F, 3.0F, 0.0F, false);
			right_arm = new ModelRenderer(this);
			right_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
			right_arm.setTextureOffset(0, 22).addBox(-11.0F, 11.0F, -2.0F, 2.0F, 8.0F, 3.0F, 0.0F, false);
			right_arm.setTextureOffset(21, 17).addBox(-11.0F, 18.0F, -7.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			right_arm.setTextureOffset(21, 7).addBox(-11.0F, 18.0F, 1.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
			right_arm.setTextureOffset(20, 23).addBox(-11.0F, 17.0F, -10.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
			right_arm.setTextureOffset(28, 14).addBox(-12.0F, 18.0F, -10.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			right_arm.setTextureOffset(21, 13).addBox(-10.0F, 18.0F, -10.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
			right_arm.setTextureOffset(0, 4).addBox(-11.0F, 18.0F, -11.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			left_leg = new ModelRenderer(this);
			left_leg.setRotationPoint(-1.9F, 12.0F, 0.0F);
			left_leg.setTextureOffset(28, 6).addBox(2.9F, 8.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
			right_leg = new ModelRenderer(this);
			right_leg.setRotationPoint(1.9F, 12.0F, 0.0F);
			right_leg.setTextureOffset(26, 27).addBox(-4.9F, 8.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
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
