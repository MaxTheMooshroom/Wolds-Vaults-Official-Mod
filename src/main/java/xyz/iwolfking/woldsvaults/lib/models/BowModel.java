package xyz.iwolfking.woldsvaults.lib.models;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.math.Vector3f;

import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.init.ModDynamicModels;

import java.util.HashMap;
import java.util.Map;

import xyz.iwolfking.woldsvaults.builders.BuilderBlockModel;

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
         .parent("minecraft:item/generated")
         .putTexture("layer0", "minecraft:item/bow")
         .beginTransforms()
            .transform(TransformType.THIRD_PERSON_LEFT_HAND, 
               new Vector3f(-80.0f, -280.0f, 40.0f),
               new Vector3f(-1.0f, -2.0f, 2.5f),
               new Vector3f(0.9f, 0.9f, 0.9f)
            )
            .transform(TransformType.THIRD_PERSON_RIGHT_HAND, 
               new Vector3f(-80.0f, 260.0f, -40.0f),
               new Vector3f(-1.0f, -2.0f, 2.5f),
               new Vector3f(0.9f, 0.9f, 0.9f)
            )
            .transform(TransformType.FIRST_PERSON_LEFT_HAND, 
               new Vector3f(0.0f, 90.0f, -25.0f),
               new Vector3f(1.13f, 3.2f, 1.13f),
               new Vector3f(0.68f, 0.68f, 0.68f)
            )
            .transform(TransformType.FIRST_PERSON_RIGHT_HAND, 
               new Vector3f(0.0f, -90.0f, 25.0f),
               new Vector3f(1.13f, 3.2f, 1.13f),
               new Vector3f(0.68f, 0.68f, 0.68f)
            )
            .buildTransforms()
         .beginOverride("minecraft:item/bow_pulling_0")
            .predicate("pulling", 1.0f)
            .buildOverride()
         .beginOverride("minecraft:item/bow_pulling_1")
            .predicate("pulling", 1.0f)
            .predicate("pull", 0.65f)
            .buildOverride()
         .beginOverride("minecraft:item/bow_pulling_2")
            .predicate("pulling", 1.0f)
            .predicate("pull", 0.9f)
            .buildOverride()
         .beginOverride("item/custom_bow")
            .predicate("custom_model_data", 1.0f)
            .buildOverride()
         .beginOverride("item/custom_bow_pulling_0")
            .predicate("custom_model_data", 1.0f)
            .predicate("pulling", 1)
            .buildOverride()
         .beginOverride("item/custom_bow_pulling_1")
            .predicate("custom_model_data", 1.0f)
            .predicate("pulling", 1)
            .predicate("pull", 0.65f)
            .buildOverride()
         .beginOverride("item/custom_bow_pulling_2")
            .predicate("custom_model_data", 1.0f)
            .predicate("pulling", 1)
            .predicate("pull", 0.9f)
            .buildOverride();

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
