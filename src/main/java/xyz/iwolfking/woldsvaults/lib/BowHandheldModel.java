package xyz.iwolfking.woldsvaults.lib;

import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.dynamodel.model.item.HandHeldModel;

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

import xyz.iwolfking.woldsvaults.util.builders.BuilderBlockModel;

// see also:
// xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom.MixinDynamicModel.createDefaultItemModel()
public class BowHandheldModel extends DynamicModel<HandHeldModel> {
   public BowHandheldModel(ResourceLocation id, String displayName) {
      super(id, displayName);
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public BlockModel generateItemModel(Map<String, ResourceLocation> textures) {
      BuilderBlockModel bmb = MODEL.get().clone();
      textures.forEach((name, location) -> {
         bmb.putTexture(name, new Material(InventoryMenu.BLOCK_ATLAS, location));
      });
      return bmb.build();
   }

   private static final Optional<BuilderBlockModel> MODEL;

   static {
      if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
         MODEL = Optional.empty();
      } else {
         BuilderBlockModel builder = new BuilderBlockModel()
            .parent("item/generated")
            .beginTransforms(TransformType.THIRD_PERSON_LEFT_HAND)
                  .rotation(-80.0f, -280.0f, 40.0f)
                  .translation(1.0f, -2.0f, 2.5f)
                  .scale(0.9f, 0.9f, 0.9f)
               .next(TransformType.THIRD_PERSON_RIGHT_HAND) 
                  .rotation(-80.0f, 260.0f, -40.0f)
                  .translation(-1.0f, -2.0f, 2.5f)
                  .scale(0.9f, 0.9f, 0.9f)
               .next(TransformType.FIRST_PERSON_LEFT_HAND)
                  .rotation(0.0f, 90.0f, -25.0f)
                  .translation(1.13f, 3.2f, 1.13f)
                  .scale(0.68f, 0.68f, 0.68f)
               .next(TransformType.FIRST_PERSON_RIGHT_HAND)
                  .rotation(0.0f, -90.0f, 25.0f)
                  .translation(1.13f, 3.2f, 1.13f)
                  .scale(0.68f, 0.68f, 0.68f)
               .buildTransforms()
            .beginOverrides("item/bow_pulling_0")
                  .predicate("pulling", 1.0f)
               .next("item/bow_pulling_1")
                  .predicate("pulling", 1.0f)
                  .predicate("pull", 0.65f)
               .next("item/bow_pulling_2")
                  .predicate("pulling", 1.0f)
                  .predicate("pull", 0.9f)
               .buildOverrides()
            .ambientOcclusion(true)
            .guiLight(BlockModel.GuiLight.SIDE);

         MODEL = Optional.of(builder.freeze());
      }
   }
}
