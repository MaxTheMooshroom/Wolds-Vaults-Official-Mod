package xyz.iwolfking.woldsvaults.builders;

import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;

// ItemTransform is deprecated but the canonical method for item transforms is now
// the plural class ItemTransforms, which strictly depends on ItemTransform despite
// itself not being deprecated. I presume this is because ItemTransforms assumes you're
// creating an instance through deserialization of JSON, which does not require
// touching ItemTransform.
@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class BuilderItemTransforms {
   public ItemTransform thirdPersonLeftHand = ItemTransform.NO_TRANSFORM;
   public ItemTransform thirdPersonRightHand = ItemTransform.NO_TRANSFORM;
   public ItemTransform firstPersonLeftHand = ItemTransform.NO_TRANSFORM;
   public ItemTransform firstPersonRightHand = ItemTransform.NO_TRANSFORM;
   public ItemTransform head = ItemTransform.NO_TRANSFORM;
   public ItemTransform gui = ItemTransform.NO_TRANSFORM;
   public ItemTransform ground = ItemTransform.NO_TRANSFORM;
   public ItemTransform fixed = ItemTransform.NO_TRANSFORM;

   public Map<TransformType, ItemTransform> moddedTransforms = new HashMap<>();

   BuilderItemTransforms(BuilderBlockModel bmb) {
      this.block_model_builder = Optional.of(bmb);
   }

   // public BuilderItemTransforms transform(TransformType type, ItemTransform transform) {
   //    if (this.block_model_builder == null) {
   //       throw new IllegalStateException("ItemTransformsBuilder already used");
   //    }
      
   //    switch (type) {
   //    case THIRD_PERSON_LEFT_HAND:
   //       this.thirdPersonLeftHand = transform;
   //       break;
   //    case THIRD_PERSON_RIGHT_HAND:
   //       this.thirdPersonRightHand = transform;
   //       break;
   //    case FIRST_PERSON_LEFT_HAND:
   //       this.firstPersonLeftHand = transform;
   //       break;
   //    case FIRST_PERSON_RIGHT_HAND:
   //       this.firstPersonRightHand = transform;
   //       break;
   //    case HEAD:
   //       this.head = transform;
   //       break;
   //    case GUI:
   //       this.gui = transform;
   //       break;
   //    case GROUND:
   //       this.ground = transform;
   //       break;
   //    case FIXED:
   //       this.fixed = transform;
   //       break;
   //    default:
   //    }

   //    return this;
   // }

   public BuilderItemTransforms transform(TransformType type, Vector3f rotation, Vector3f translation, Vector3f scale) {
      // return this.transform(type, new ItemTransform(rotation, translation, scale));
   }

   public BuilderItemTransforms moddedTransform(TransformType type, ItemTransform transform) {
      if (this.block_model_builder == null) {
         throw new IllegalStateException("ItemTransformsBuilder already used");
      }

      this.moddedTransforms.put(type, transform);
      return this;
   }

   public BuilderBlockModel buildTransforms() {
      if (this.block_model_builder.isEmpty()) {
         throw new IllegalStateException("ItemTransformsBuilder already used");
      }

      ItemTransforms it = new ItemTransforms(this.thirdPersonLeftHand, this.thirdPersonRightHand, this.firstPersonLeftHand, this.firstPersonRightHand, this.head, this.gui, this.ground, this.fixed, ImmutableMap.copyOf(this.moddedTransforms));
      this.block_model_builder.get().transforms = it;

      BuilderBlockModel bmb = this.block_model_builder.get();
      this.block_model_builder = Optional.empty();
      return bmb;
   }

   @Nonnull
   private Optional<BuilderBlockModel> block_model_builder;
}
