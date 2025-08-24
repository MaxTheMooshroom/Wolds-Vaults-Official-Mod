package xyz.iwolfking.woldsvaults.mixins.vaulthunters.custom;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import com.mojang.math.Vector3f;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import iskallia.vault.dynamodel.DynamicModel;

import xyz.iwolfking.woldsvaults.builders.BuilderBlockModel;

@Mixin(value = DynamicModel.class, remap = false)
public class MixinDynamicModel<M extends DynamicModel<M>> extends DynamicModel<M> {
   public MixinDynamicModel(ResourceLocation id, String displayName) {
      super(id, displayName);
   }

   // ItemTransform is deprecated but the canonical method for item transforms is now
   // the plural class ItemTransforms, which strictly depends on ItemTransform despite
   // itself not being deprecated. I presume this is because ItemTransforms assumes you're
   // creating an instance through deserialization of JSON, which does not require
   // touching ItemTransform.
   @SuppressWarnings({"deprecation"})
   private static BuilderBlockModel createDefaultItemModel() {
      // original string: "{  \"parent\": \"item/generated\",  \"textures\": {{textures}},  \"display\": {    \"thirdperson_lefthand\": {      \"rotation\": [0.0, 0.0, 0.0],      \"translation\": [0.0, 0.1875, 0.0625],      \"scale\": [0.55, 0.55, 0.55]    },    \"thirdperson_righthand\": {      \"rotation\": [0.0, 0.0, 0.0],      \"translation\": [0.0, 0.1875, 0.0625],      \"scale\": [0.55, 0.55, 0.55]    },    \"firstperson_lefthand\": {      \"rotation\": [0.0, -90.0, 25.0],      \"translation\": [0.070625, 0.2, 0.070625],      \"scale\": [0.68, 0.68, 0.68]    },    \"firstperson_righthand\": {      \"rotation\": [0.0, -90.0, 25.0],      \"translation\": [0.070625, 0.2, 0.070625],      \"scale\": [0.68, 0.68, 0.68]    },    \"head\": {      \"rotation\": [0, 180, 0],      \"translation\": [0.0, 0.8125, 0.4375],      \"scale\": [1, 1, 1]    },    \"gui\": {      \"rotation\": [0, 0, 0],      \"translation\": [0, 0, 0],      \"scale\": [1, 1, 1]    },    \"ground\": {      \"rotation\": [0, 0, 0],      \"translation\": [0.0, 0.125, 0.0],      \"scale\": [0.5, 0.5, 0.5]    },    \"fixed\": {      \"rotation\": [0.0, 180.0, 0.0],      \"translation\": [0.0, 0.0, 0.0],      \"scale\": [0.5, 0.5, 0.5]    }  }}"

      ItemTransform thirdPersonLeft = new ItemTransform(
         new Vector3f(0.0f, 0.0f, 0.0f),
         new Vector3f(0.0f, 0.1875f, 0.0625f),
         new Vector3f(0.55f, 0.55f, 0.55f)
      );

      ItemTransform thirdPersonRight = new ItemTransform(
         new Vector3f(0.0f, 0.0f, 0.0f),
         new Vector3f(0.0f, 0.1875f, 0.0625f),
         new Vector3f(0.55f, 0.55f, 0.55f)
      );

      ItemTransform firstPersonLeft = new ItemTransform(
         new Vector3f(0.0f, -90.0f, 25.0f),
         new Vector3f(0.070625f, 0.2f, 0.070625f),
         new Vector3f(0.68f, 0.68f, 0.68f)
      );

      ItemTransform firstPersonRight = new ItemTransform(
         new Vector3f(0.0f, -90.0f, 25.0f),
         new Vector3f(0.070625f, 0.2f, 0.070625f),
         new Vector3f(0.68f, 0.68f, 0.68f)
      );

      ItemTransform head = new ItemTransform(
         new Vector3f(0.0f, 180.0f, 0.0f),
         new Vector3f(0.0f, 0.8125f, 0.4375f),
         new Vector3f(1.0f, 1.0f, 1.0f)
      );

      ItemTransform gui = new ItemTransform(
         new Vector3f(0.0f, 0.0f, 0.0f),
         new Vector3f(0.0f, 0.0f, 0.0f),
         new Vector3f(1.0f, 1.0f, 1.0f)
      );

      ItemTransform ground = new ItemTransform(
         new Vector3f(0.0f, 0.0f, 0.0f),
         new Vector3f(0.0f, 0.125f, 0.0f),
         new Vector3f(0.5f, 0.5f, 0.5f)
      );

      ItemTransform fixed = new ItemTransform(
         new Vector3f(0.0f, 180.0f, 0.0f),
         new Vector3f(0.0f, 0.0f, 0.0f),
         new Vector3f(0.5f, 0.5f, 0.5f)
      );

      BuilderBlockModel builder = new BuilderBlockModel()
            .parent(new ResourceLocation("minecraft:item/generated"))
            .beginTransforms()
               .put(TransformType.THIRD_PERSON_LEFT_HAND, thirdPersonLeft)
               .put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdPersonRight)
               .put(TransformType.FIRST_PERSON_LEFT_HAND, firstPersonLeft)
               .put(TransformType.FIRST_PERSON_RIGHT_HAND, firstPersonRight)
               .put(TransformType.HEAD, head)
               .put(TransformType.GUI, gui)
               .put(TransformType.GROUND, ground)
               .put(TransformType.FIXED, fixed)
               .buildTransforms()
            .ambientOcclusion(true)
            .guiLight(BlockModel.GuiLight.SIDE);

      return builder;
   }

   static final BuilderBlockModel DEFAULT_ITEM_MODEL = createDefaultItemModel();

   @Overwrite(remap = false)
   @OnlyIn(Dist.CLIENT)
   public BlockModel generateItemModel(Map<String, ResourceLocation> textures) {
      return this.createUnbakedModel(DEFAULT_ITEM_MODEL.clone(), textures);
   }

   @Overwrite(remap = false)
   @OnlyIn(Dist.CLIENT)
   protected BlockModel createUnbakedModel(BuilderBlockModel builder, Map<String, ResourceLocation> textures) {
      textures.forEach((name, location) -> {
         builder.putTexture(name, new Material(InventoryMenu.BLOCK_ATLAS, location));
      });
      return builder.build();
   }
}
