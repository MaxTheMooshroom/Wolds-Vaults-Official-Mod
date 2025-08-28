package xyz.iwolfking.woldsvaults.util.builders;

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

   public static class BuilderItemTransform {
      public TransformType type;
      public Vector3f[] vecs = {
         new Vector3f(0.0f,0.0f,0.0f),
         new Vector3f(0.0f,0.0f,0.0f),
         new Vector3f(1.0f,1.0f,1.0f)
      };
      
      BuilderItemTransform(BuilderItemTransforms pBit, TransformType pType) {
         this.builder_item_transforms = Optional.of(pBit);
         this.type = pType;
      }

      public BuilderItemTransform rotation(Vector3f rotation) {
         this.checkState();
         this.vecs[0] = rotation;
         return this;
      }

      public BuilderItemTransform rotation(float x, float y, float z) {
         this.checkState();
         this.vecs[0] = new Vector3f(x, y, z);
         return this;
      }

      public BuilderItemTransform translation(Vector3f translation) {
         this.checkState();
         this.vecs[1] = translation;
         return this;
      }

      public BuilderItemTransform translation(float x, float y, float z) {
         this.checkState();
         this.vecs[1] = new Vector3f(x, y, z);
         return this;
      }

      public BuilderItemTransform scale(Vector3f scale) {
         this.checkState();
         this.vecs[2] = scale;
         return this;
      }

      public BuilderItemTransform scale(float x, float y, float z) {
         this.checkState();
         this.vecs[2] = new Vector3f(x, y, z);
         return this;
      }

      public BuilderItemTransform next(TransformType pType) {
         this.checkState();

         BuilderItemTransforms bit = this.builder_item_transforms.get();
         bit.transforms(this.type, this.vecs[0], this.vecs[1], this.vecs[2]);

         return this.resetState(pType);
      }

      public BuilderBlockModel buildTransforms() {
         this.checkState();

         BuilderItemTransforms bit = this.builder_item_transforms.get();
         // bit.transform(this.type, this.vecs[0], this.vecs[1], this.vecs[2]);

         bit.block_model_builder.get().transforms = new ItemTransforms(
            bit.thirdPersonLeftHand, bit.thirdPersonRightHand,
            bit.firstPersonLeftHand, bit.firstPersonRightHand,
            bit.head, bit.gui, bit.ground, bit.fixed,
            ImmutableMap.copyOf(bit.moddedTransforms)
         );

         BuilderBlockModel bbm = bit.block_model_builder.get();
         bit.block_model_builder = Optional.empty();
         return bbm;
      }

      @Nonnull
      private Optional<BuilderItemTransforms> builder_item_transforms;

      private void checkState() {
         if (this.builder_item_transforms.isEmpty()) {
            throw new IllegalStateException("BuilderItemTransforms.BuilderItemTransform has been closed");
         }
      }

      private BuilderItemTransform resetState(TransformType pType) {
         this.type = pType;
         this.vecs[0] = new Vector3f(0.0f,0.0f,0.0f);
         this.vecs[1] = new Vector3f(0.0f,0.0f,0.0f);
         this.vecs[2] = new Vector3f(1.0f,1.0f,1.0f);
         return this;
      }
   }

   BuilderItemTransforms(BuilderBlockModel bmb) {
      this.block_model_builder = Optional.of(bmb);
   }

   private void checkState() {
      if (this.block_model_builder.isEmpty()) {
         throw new IllegalStateException("BuilderItemTransforms already used");
      }
   }

   public BuilderItemTransform beginTransforms(TransformType pType) {
      this.checkState();
      return new BuilderItemTransform(this, pType);
   }

   public BuilderItemTransforms transform(TransformType type, ItemTransform transform) {
      this.checkState();
      
      switch (type) {
      case THIRD_PERSON_LEFT_HAND:
         this.thirdPersonLeftHand = transform;
         break;
      case THIRD_PERSON_RIGHT_HAND:
         this.thirdPersonRightHand = transform;
         break;
      case FIRST_PERSON_LEFT_HAND:
         this.firstPersonLeftHand = transform;
         break;
      case FIRST_PERSON_RIGHT_HAND:
         this.firstPersonRightHand = transform;
         break;
      case HEAD:
         this.head = transform;
         break;
      case GUI:
         this.gui = transform;
         break;
      case GROUND:
         this.ground = transform;
         break;
      case FIXED:
         this.fixed = transform;
         break;
      default:
      }

      return this;
   }

   public BuilderItemTransforms transforms(TransformType type, Vector3f rotation, Vector3f translation, Vector3f scale) {
      return this.transform(type, new ItemTransform(rotation, translation, scale));
   }

   public BuilderItemTransforms moddedTransform(TransformType type, ItemTransform transform) {
      this.checkState();
      this.moddedTransforms.put(type, transform);
      return this;
   }

   @Nonnull
   private Optional<BuilderBlockModel> block_model_builder;
}
