package xyz.iwolfking.woldsvaults.lib;

import iskallia.vault.dynamodel.model.item.HandHeldModel;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.math.Vector3f;

import java.util.Map;

import xyz.iwolfking.woldsvaults.builders.BuilderBlockModel;
import xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom.MixinDynamicModel;

// see also:
// xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom.MixinDynamicModel.createDefaultItemModel()
@SuppressWarnings({"deprecation"})
public class BowHandheldModel extends MixinDynamicModel<HandHeldModel> {
   public BowHandheldModel(ResourceLocation id, String displayName) {
      super(id, displayName);
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public BlockModel generateItemModel(Map<String, ResourceLocation> textures) {
      return this.createUnbakedModel(MODEL.clone(), textures);
   }

   private static final BuilderBlockModel MODEL;

   static {
      ItemTransform thirdPersonLeft = new ItemTransform(
         new Vector3f(-80.0f, -280.0f, 40.0f),
         new Vector3f(1.0f, -2.0f, 2.5f),
         new Vector3f(0.9f, 0.9f, 0.9f)
      );

      ItemTransform thirdPersonRight = new ItemTransform(
         new Vector3f(-80.0f, 260.0f, -40.0f),
         new Vector3f(-1.0f, -2.0f, 2.5f),
         new Vector3f(0.9f, 0.9f, 0.9f)
      );

      ItemTransform firstPersonLeft = new ItemTransform(
         new Vector3f(0.0f, 90.0f, -25.0f),
         new Vector3f(1.13f, 3.2f, 1.13f),
         new Vector3f(0.68f, 0.68f, 0.68f)
      );

      ItemTransform firstPersonRight = new ItemTransform(
         new Vector3f(0.0f, -90.0f, 25.0f),
         new Vector3f(1.13f, 3.2f, 1.13f),
         new Vector3f(0.68f, 0.68f, 0.68f)
      );

      BuilderBlockModel builder = new BuilderBlockModel()
         .parent("minecraft:item/generated")
         .beginTransforms()
            .put(TransformType.THIRD_PERSON_LEFT_HAND, thirdPersonLeft)
            .put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdPersonRight)
            .put(TransformType.FIRST_PERSON_LEFT_HAND, firstPersonLeft)
            .put(TransformType.FIRST_PERSON_RIGHT_HAND, firstPersonRight)
            .buildTransforms()
         .beginOverride("item/bow_pulling_0")
            .predicate("pulling", 1.0f)
            .buildOverride()
         .beginOverride("item/bow_pulling_1")
            .predicate("pulling", 1.0f)
            .predicate("pull", 0.65f)
            .buildOverride()
         .beginOverride("item/bow_pulling_2")
            .predicate("pulling", 1.0f)
            .predicate("pull", 0.9f)
            .buildOverride()
         .ambientOcclusion(true)
         .guiLight(BlockModel.GuiLight.SIDE);

      MODEL = builder.freeze();
   }
}
