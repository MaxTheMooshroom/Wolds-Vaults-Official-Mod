package xyz.iwolfking.woldsvaults.lib.models;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.init.ModDynamicModels;

import java.util.HashMap;
import java.util.Map;

import xyz.iwolfking.woldsvaults.util.builders.BuilderBlockModel;

public class BowModel extends DynamicModel<BowModel> {
   public BowModel(ResourceLocation id, String displayName) {
      super(id, displayName);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public BlockModel generateItemModel(Map<String, ResourceLocation> textures) {
      return MODEL;
   }
   
   private static final BlockModel MODEL;

   static {
      BuilderBlockModel builder = new BuilderBlockModel()
         .parent("item/generated")
         .putTexture("layer0", "minecraft:item/bow")
         .beginTransforms(TransformType.THIRD_PERSON_LEFT_HAND)
               .rotation(-80.0f, -280.0f, 40.0f)
               .translation(-1.0f, -2.0f, 2.5f)
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
         .next("item/custom_bow")
            .predicate("custom_model_data", 1.0f)
         .next("item/custom_bow_pulling_0")
            .predicate("custom_model_data", 1.0f)
            .predicate("pulling", 1)
         .next("item/custom_bow_pulling_1")
            .predicate("custom_model_data", 1.0f)
            .predicate("pulling", 1)
            .predicate("pull", 0.65f)
         .next("item/custom_bow_pulling_2")
            .predicate("custom_model_data", 1.0f)
            .predicate("pulling", 1)
            .predicate("pull", 0.9f)
         .buildOverrides();

      MODEL = builder.build();
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public Map<String, ResourceLocation> resolveTextures(ResourceManager resourceManager, ResourceLocation resourceLocation) {
      HashMap<String, ResourceLocation> textures = new HashMap<>();
      textures.put("layer0", ModDynamicModels.textureExists(resourceManager, resourceLocation) ? resourceLocation : ModDynamicModels.EMPTY_TEXTURE);
      if (ModDynamicModels.textureExists(resourceManager, appendToId(resourceLocation, "_overlay"))) {
         textures.put("layer1", appendToId(resourceLocation, "_overlay"));
      }

      for(int i = 0; i < 10; ++i) {
         if (ModDynamicModels.textureExists(resourceManager, appendToId(resourceLocation, "_layer" + i))) {
            textures.put("layer" + i, appendToId(resourceLocation, "_layer" + i));
         }
      }

      String modelName = resourceLocation.toString().substring(resourceLocation.toString().lastIndexOf('/') + 1);
      if(ModDynamicModels.jsonModelExists(resourceManager, appendToId(resourceLocation, "_pulling_0"))) {
         textures.put("bow_pulling_model_0", new ResourceLocation("the_vault", "models/item/gear/bow/" + modelName + "_pulling_0"));
      }

      if(ModDynamicModels.jsonModelExists(resourceManager, appendToId(resourceLocation, "_pulling_1"))) {
         textures.put("bow_pulling_model_1", new ResourceLocation("the_vault", "models/item/gear/bow/" + modelName + "_pulling_1"));
      }

      if(ModDynamicModels.jsonModelExists(resourceManager, appendToId(resourceLocation, "_pulling_2"))) {
         textures.put("bow_pulling_model_2", new ResourceLocation("the_vault", "models/item/gear/bow/" + modelName + "_pulling_2"));
      }

      return textures;
   }
}
