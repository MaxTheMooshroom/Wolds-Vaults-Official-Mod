package xyz.iwolfking.woldsvaults.builders;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import xyz.iwolfking.woldsvaults.util.Pair;

public class BuilderBlockModelOverride {
    BuilderBlockModelOverride(BuilderBlockModel bmb, String model) {
        this.builder_block_model = Optional.of(bmb);
        this.model = model;
    }

    public BuilderBlockModelOverride predicate(ResourceLocation rl, float value) {
        if (this.builder_block_model.isEmpty()) {
            throw new IllegalStateException("BuilderBlockModelOverride has been used");
        }

        this.overrides.add(Pair.of(rl, value));
        return this;
    }

    public BuilderBlockModelOverride predicate(String id, float value) {
        return this.predicate(new ResourceLocation(id), value);
    }

    public BuilderBlockModel buildOverride() {
        if (this.builder_block_model.isEmpty()) {
            throw new IllegalStateException("BuilderBlockModelOverride already used");
        }

        BuilderBlockModel bmb = this.builder_block_model.get();
        this.builder_block_model = Optional.empty();

        bmb.addOverride(new ResourceLocation(this.model), this.overrides);
        return bmb;
    }

    private String model;
    private Optional<BuilderBlockModel> builder_block_model;
    private List<Pair<ResourceLocation, Float>> overrides = new ArrayList<>();
}
