package xyz.iwolfking.woldsvaults.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Either;

import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

import xyz.iwolfking.woldsvaults.util.Pair;

public class BuilderBlockModel {
   public BuilderBlockModel() {}

   public BuilderBlockModel parent(ResourceLocation parent) {
      if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().parent()");
      }
      this.parentLocation = parent;
      return this;
   }

   public BuilderBlockModel parent(String parentId) {
      return this.parent(new ResourceLocation(parentId));
   }

   public BuilderBlockModel addElement(BlockElement element) {
      if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().addElement()");
      }
      this.elements.add(element);
      return this;
   }

   public BuilderBlockModel putTexture(String name, Material material) {
      if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().putTexture()");
      }
      this.textureMap.put(name, Either.left(material));
      return this;
   }

   public BuilderBlockModel putTexture(String name, String texture) {
      if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().putTexture()");
      }
      this.textureMap.put(name, Either.right(texture));
      return this;
   }

   public BuilderBlockModel ambientOcclusion(boolean flag) {
      if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().ambientOcclusion()");
      }
      this.ambientOcclusion = flag;
      return this;
   }

   public BuilderBlockModel guiLight(BlockModel.GuiLight light) {
      if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().guiLight()");
      }
      this.guiLight = light;
      return this;
   }

   public BuilderItemTransforms beginTransforms() {
      if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().beginTransforms()");
      }
      return new BuilderItemTransforms(this);
   }

   public BuilderBlockModelOverride beginOverride(String model) {
      if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().addOverride()");
      }
      
      return new BuilderBlockModelOverride(this, model);
   }

   void addOverride(ResourceLocation id, List<Pair<ResourceLocation, Float>> pairs) {
      if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().addOverride()");
      }

      Stream<ItemOverride.Predicate> preds = pairs.stream()
         .map(pair -> new ItemOverride.Predicate(pair.one(), pair.two()));

      this.overrides.add(new ItemOverride(id, preds.toList()));
   }

   public BuilderBlockModel freeze() {
      this.frozen = true;
      return this;
   }

   public BuilderBlockModel clone() {
      BuilderBlockModel b = new BuilderBlockModel();
      b.parentLocation = this.parentLocation;
      b.elements = new ArrayList<>(this.elements);
      b.textureMap = new HashMap<>(this.textureMap);
      b.ambientOcclusion = this.ambientOcclusion;
      b.guiLight = this.guiLight;
      b.transforms = new ItemTransforms(this.transforms);
      b.overrides = new ArrayList<>(this.overrides);

      return b;
   }

   public BlockModel build() {
      if (this.built) {
         throw new IllegalStateException("BlockModelBuilder already used");
      } else if (this.frozen) {
         throw new IllegalStateException("Frozen BlockModelBuilders cannot build. Consider using .clone().build()");
      }

      this.built = true;
      return new BlockModel(parentLocation, elements, textureMap, ambientOcclusion, guiLight, transforms, overrides);
   }
    
   private boolean frozen = false;
   private boolean built = false;
   private ResourceLocation parentLocation;
   private List<BlockElement> elements = new ArrayList<>();
   private Map<String, Either<Material, String>> textureMap = new HashMap<>();
   private boolean ambientOcclusion = true;
   private BlockModel.GuiLight guiLight = BlockModel.GuiLight.SIDE;
   private List<ItemOverride> overrides = new ArrayList<>();
   ItemTransforms transforms = ItemTransforms.NO_TRANSFORMS;
}

