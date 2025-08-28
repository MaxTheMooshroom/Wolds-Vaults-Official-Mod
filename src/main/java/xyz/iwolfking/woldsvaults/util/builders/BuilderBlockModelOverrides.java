package xyz.iwolfking.woldsvaults.util.builders;

import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

public class BuilderBlockModelOverrides {
   @Nonnull public List<ItemOverride.Predicate> currentPredicates = new ArrayList<>();
   @Nonnull public ResourceLocation model;

   BuilderBlockModelOverrides(BuilderBlockModel bbm, ResourceLocation pModel) {
      this.builderBlockModel = Optional.of(bbm);
      this.model = pModel;
   }

   public BuilderBlockModelOverrides predicate(ResourceLocation pId, float value) {
      this.checkState();
      
      ItemOverride.Predicate predicate = new ItemOverride.Predicate(pId, value);
      this.currentPredicates.add(predicate);

      return this;
   }

   public BuilderBlockModelOverrides predicate(String pId, float value) {
      return this.predicate(new ResourceLocation(pId), value);
   }

   public BuilderBlockModelOverrides next(ResourceLocation pModel) {
      this.checkState();

      BuilderBlockModel bbm = this.builderBlockModel.get();
      ItemOverride io = new ItemOverride(this.model, this.currentPredicates);
      bbm.overrides.add(io);

      this.model = pModel;
      this.currentPredicates = new ArrayList<>();
      return this;
   }

   public BuilderBlockModelOverrides next(String pModel) {
      return this.next(new ResourceLocation(pModel));
   }

   public BuilderBlockModel buildOverrides() {
      this.checkState();

      BuilderBlockModel bbm = this.builderBlockModel.get();
      ItemOverride io = new ItemOverride(this.model, this.currentPredicates);
      bbm.overrides.add(io);

      this.builderBlockModel = Optional.empty();
      return bbm;
   }

   @Nonnull
   private Optional<BuilderBlockModel> builderBlockModel;

   private void checkState() {
      if (this.builderBlockModel.isEmpty() || this.model == null) {
         throw new IllegalStateException("BuilderBlockModelOverrides.BuilderBlockModelOverride has been closed");
      }
   }
}
