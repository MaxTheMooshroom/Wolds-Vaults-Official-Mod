package xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;
import java.util.Optional;

import iskallia.vault.dynamodel.model.item.HandHeldModel;

import xyz.iwolfking.woldsvaults.util.builders.BuilderBlockModel;

@OnlyIn(Dist.CLIENT)
@Mixin(HandHeldModel.class)
public abstract class MixinHandHeldModel {
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
         //     "parent": "item/generated",
         //     "textures": {{textures}},
         //     "display": {
         //         "thirdperson_righthand": {
         //             "rotation": [0, -90, 55],
         //             "translation": [0, 4.0, 0.5],
         //             "scale": [0.85, 0.85, 0.85]
         //         },
         //         "thirdperson_lefthand": {
         //             "rotation": [0, 90, -55],
         //             "translation": [0, 4.0, 0.5],
         //             "scale": [0.85, 0.85, 0.85]
         //         },
         //         "firstperson_righthand": {
         //             "rotation": [0, -90, 25],
         //             "translation": [1.13, 3.2, 1.13],
         //             "scale": [0.68, 0.68, 0.68]
         //         },
         //         "firstperson_lefthand": {
         //             "rotation": [0, 90, -25],
         //             "translation": [1.13, 3.2, 1.13],
         //             "scale": [0.68, 0.68, 0.68]
         //         }
         //     }
         // }
         BuilderBlockModel builder = new BuilderBlockModel()
            .parent("minecraft:item/generated")
            .beginTransforms(TransformType.THIRD_PERSON_RIGHT_HAND)
                  .rotation(0.0f, -90.0f, 55.0f)
                  .translation(0.0f, 4.0f, 0.5f)
                  .scale(0.85f, 0.85f, 0.85f)
               .next(TransformType.THIRD_PERSON_LEFT_HAND)
                  .rotation(0.0f, 90.0f, -55.0f)
                  .translation(0.0f, 4.0f, 0.5f)
                  .scale(0.85f, 0.85f, 0.85f)
               .next(TransformType.THIRD_PERSON_RIGHT_HAND)
                  .rotation(0.0f, -90.0f, 25.0f)
                  .translation(1.13f, 3.2f, 1.13f)
                  .scale(0.68f, 0.68f, 0.68f)
               .next(TransformType.FIRST_PERSON_LEFT_HAND)
                  .rotation(0.0f, 90.0f, -25.0f)
                  .translation(1.13f, 3.2f, 1.13f)
                  .scale(0.68f, 0.68f, 0.68f)
               .buildTransforms();

         MODEL = Optional.of(builder.freeze());
      }
   }
}
