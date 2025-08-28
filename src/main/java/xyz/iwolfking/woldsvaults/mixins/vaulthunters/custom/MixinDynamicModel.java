package xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import iskallia.vault.dynamodel.DynamicModel;

import xyz.iwolfking.woldsvaults.util.builders.BuilderBlockModel;

@OnlyIn(Dist.CLIENT)
@Mixin(DynamicModel.class)
public abstract class MixinDynamicModel {
   private static final Optional<BuilderBlockModel> MODEL;

   /**
    * @author MaxTheMooshroom (Maxine Zick <maxine@pnk.dev>)
    * @reason Replace hardcoded JSON parsing with direct construction
    *         to reduce startup overhead.
    */
   @Overwrite(remap = false)
   @OnlyIn(Dist.CLIENT)
   public BlockModel generateItemModel(Map<String, ResourceLocation> textures) {
      BuilderBlockModel bmb = MODEL.get().clone();
      textures.forEach((name, location) -> {
         bmb.putTexture(name, new Material(InventoryMenu.BLOCK_ATLAS, location));
      });
      return bmb.build();
   }

   static {
      if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
         MODEL = Optional.empty();
      } else {
         // original json:
         // {
         //    "parent": "item/generated",
         //    "textures": {{textures}},
         //    "display": {
         //       "thirdperson_lefthand": {
         //          "rotation": [0.0, 0.0, 0.0],
         //          "translation": [0.0, 0.1875, 0.0625],
         //          "scale": [0.55, 0.55, 0.55]
         //       },
         //       "thirdperson_righthand": {
         //          "rotation": [0.0, 0.0, 0.0],
         //          "translation": [0.0, 0.1875, 0.0625],
         //          "scale": [0.55, 0.55, 0.55]
         //       },
         //       "firstperson_lefthand": {
         //          "rotation": [0.0, -90.0, 25.0],
         //          "translation": [0.070625, 0.2, 0.070625],
         //          "scale": [0.68, 0.68, 0.68]
         //       },
         //       "firstperson_righthand": {
         //          "rotation": [0.0, -90.0, 25.0],
         //          "translation": [0.070625, 0.2, 0.070625],
         //          "scale": [0.68, 0.68, 0.68]
         //       },
         //       "head": {
         //          "rotation": [0, 180, 0],
         //          "translation": [0.0, 0.8125, 0.4375],
         //          "scale": [1, 1, 1]
         //       },
         //       "gui": {
         //          "rotation": [0, 0, 0],
         //          "translation": [0, 0, 0].
         //          "scale": [1, 1, 1]
         //       },
         //       "ground": {
         //          "rotation": [0, 0, 0].
         //          "translation": [0.0, 0.125, 0.0].
         //          "scale": [0.5, 0.5, 0.5]
         //       },
         //       "fixed": {
         //          "rotation": [0.0, 180.0, 0.0].
         //          "translation": [0.0, 0.0, 0.0].
         //          "scale": [0.5, 0.5, 0.5]
         //       }
         //    }
         // }
         BuilderBlockModel builder = new BuilderBlockModel()
            .parent("item/generated")
            .beginTransforms(TransformType.THIRD_PERSON_LEFT_HAND)
                  .rotation(0.0f, 0.0f, 0.0f)
                  .translation(0.0f, 0.1875f, 0.0625f)
                  .scale(0.55f, 0.55f, 0.55f)
               .next(TransformType.THIRD_PERSON_RIGHT_HAND)
                  .rotation(0.0f, 0.0f, 0.0f)
                  .translation(0.0f, 0.1875f, 0.0625f)
                  .scale(0.55f, 0.55f, 0.55f)
               .next(TransformType.FIRST_PERSON_LEFT_HAND)
                  .rotation(0.0f, -90.0f, 25.0f)
                  .translation(0.070625f, 0.2f, 0.070625f)
                  .scale(0.68f, 0.68f, 0.68f)
               .next(TransformType.FIRST_PERSON_RIGHT_HAND)
                  .rotation(0.0f, -90.0f, 25.0f)
                  .translation(0.070625f, 0.2f, 0.070625f)
                  .scale(0.68f, 0.68f, 0.68f)
               .next(TransformType.HEAD)
                  .rotation(0.0f, 180.0f, 0.0f)
                  .translation(0.0f, 0.8125f, 0.4375f)
                  .scale(1.0f, 1.0f, 1.0f)
               .next(TransformType.GUI)
                  .rotation(0.0f, 0.0f, 0.0f)
                  .translation(0.0f, 0.0f, 0.0f)
                  .scale(1.0f, 1.0f, 1.0f)
               .next(TransformType.GROUND)
                  .rotation(0.0f, 0.0f, 0.0f)
                  .translation(0.0f, 0.125f, 0.0f)
                  .scale(0.5f, 0.5f, 0.5f)
               .next(TransformType.FIXED)
                  .rotation(0.0f, 180.0f, 0.0f)
                  .translation(0.0f, 0.0f, 0.0f)
                  .scale(0.5f, 0.5f, 0.5f)
               .buildTransforms();
         MODEL = Optional.of(builder.freeze());
      }
   }
}
