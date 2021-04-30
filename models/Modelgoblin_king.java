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
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
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

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
		this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.right_arm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.left_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
		this.left_arm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
		this.right_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
	}
}