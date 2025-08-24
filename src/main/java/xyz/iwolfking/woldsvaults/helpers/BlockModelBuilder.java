package xyz.iwolfking.woldsvaults.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.datafixers.util.Either;

import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class BlockModelBuilder {
    private boolean frozen = false;
    private boolean built = false;
    private ResourceLocation parentLocation;
    private List<BlockElement> elements = new ArrayList<>();
    private Map<String, Either<Material, String>> textureMap = new HashMap<>();
    private boolean ambientOcclusion = true;
    private BlockModel.GuiLight guiLight = BlockModel.GuiLight.SIDE;
    private ItemTransforms transforms = ItemTransforms.NO_TRANSFORMS;
    private List<ItemOverride> overrides = new ArrayList<>();

    public BlockModelBuilder parent(ResourceLocation parent) {
        if (this.frozen) {
            throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().parent()");
        }
        this.parentLocation = parent;
        return this;
    }

    public BlockModelBuilder addElement(BlockElement element) {
        if (this.frozen) {
            throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().addElement()");
        }
        this.elements.add(element);
        return this;
    }

    public BlockModelBuilder putTexture(String name, Either<Material, String> texture) {
        if (this.frozen) {
            throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().putTexture()");
        }
        this.textureMap.put(name, texture);
        return this;
    }

    public BlockModelBuilder ambientOcclusion(boolean flag) {
        if (this.frozen) {
            throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().ambientOcclusion()");
        }
        this.ambientOcclusion = flag;
        return this;
    }

    public BlockModelBuilder guiLight(BlockModel.GuiLight light) {
        if (this.frozen) {
            throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().guiLight()");
        }
        this.guiLight = light;
        return this;
    }

    public BlockModelBuilder transforms(ItemTransforms transforms) {
        if (this.frozen) {
            throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().transforms()");
        }
        this.transforms = transforms;
        return this;
    }

    public BlockModelBuilder addOverride(ItemOverride override) {
        if (this.frozen) {
            throw new IllegalStateException("Frozen BlockModelBuilders cannot be mutated. Consider using .clone().addOverride()");
        }
        this.overrides.add(override);
        return this;
    }

    public void freeze() {
        this.frozen = true;
    }

    public BlockModelBuilder clone() {
        BlockModelBuilder b = new BlockModelBuilder();
        b.parentLocation = this.parentLocation;
        b.elements = new ArrayList<>(this.elements);
        b.textureMap = new HashMap<>(this.textureMap);
        b.ambientOcclusion = this.ambientOcclusion;
        b.guiLight = this.guiLight;
        b.transforms = this.transforms;
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
}

