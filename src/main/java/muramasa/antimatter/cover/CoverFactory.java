package muramasa.antimatter.cover;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import muramasa.antimatter.AntimatterAPI;
import muramasa.antimatter.capability.ICoverHandler;
import muramasa.antimatter.gui.GuiData;
import muramasa.antimatter.gui.GuiInstance;
import muramasa.antimatter.machine.Tier;
import muramasa.antimatter.registration.IAntimatterObject;
import muramasa.antimatter.texture.Texture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class CoverFactory implements IAntimatterObject {

  final String id;
  final String domain;

  private final CoverSupplier supplier;
  private Map<Tier, ItemStack> itemStacks = Collections.emptyMap();
  private ItemStack itemStack;
  public final List<Consumer<GuiInstance>> guiCallbacks = new ObjectArrayList<>();
  private Iterable<Texture> textures;

  protected boolean gui = false;

  protected CoverFactory(String domain, String id, CoverSupplier supplier) {
    this.id = id;
    this.supplier = supplier;
    this.domain = domain;
    AntimatterAPI.register(CoverFactory.class, this);
  }

  public final CoverSupplier get() {
    return this.supplier;
  }

  public ItemStack getItem(Tier tier) {
    return tier == null ? getItem() : itemStacks.getOrDefault(tier, ItemStack.EMPTY);
  }

  public Iterable<Texture> getTextures() {
     return textures == null ? () -> Collections.emptyIterator() : textures;
  }
  public ItemStack getItem() {
    return itemStack;
    
  }

  void setItems(Map<Tier, ItemStack> stacks) {
    this.itemStack = stacks.remove(null);
    if (itemStack == null) itemStack = ItemStack.EMPTY;
    this.itemStacks = ImmutableMap.copyOf(stacks);
  }

  void addTextures(Iterable<Texture> textures) {
    this.textures = textures;
  }

  void setHasGui() {
    this.gui = true;
  }

  public boolean hasGui() {
    return this.gui;
  }

  @Override
  public String getDomain() {
    return domain;
  }

  public static Builder builder(final CoverSupplier supplier) {
    return new Builder(supplier);
  }

  @Override
  public String getId() {
    return id;
  }

  public static CompoundNBT writeCover(CompoundNBT nbt, ICover cover) {
    CoverFactory factory = cover.getFactory();
    nbt.putString(cover.side().getIndex() + "d", factory.getDomain());
    nbt.putString(cover.side().getIndex() + "i", factory.getId());
    if (cover.getTier() != null) nbt.putString(cover.side().getIndex() + "t", cover.getTier().getId());
    CompoundNBT inner = cover.serialize();
    if (!inner.isEmpty())
      nbt.put(cover.side().getIndex() + "c", inner);
    return nbt;
  }

  public static ICover readCover(ICoverHandler<?> source, Direction dir, CompoundNBT nbt) {
    String domain = nbt.getString(dir.getIndex()+"d");
    String id = nbt.getString(dir.getIndex()+"i");
    CoverFactory factory = AntimatterAPI.get(CoverFactory.class, id, domain);
    if (factory == null) {
      throw new IllegalStateException("Reading a cover with null factory, game in bad state");
    }
    Tier tier = nbt.contains(dir.getIndex()+"t") ? AntimatterAPI.get(Tier.class, nbt.getString(dir.getIndex()+"t")) : null;
    ICover cover = factory.supplier.get(source, tier, dir, factory);
    if (nbt.contains(dir.getIndex()+"c"))
      cover.deserialize((CompoundNBT) nbt.get(dir.getIndex()+"c"));
    return cover;
  }

  public static class Builder {
    List<Tier> tiers = Collections.singletonList(null);

    final CoverSupplier supplier;
    BiFunction<CoverFactory, Tier, ItemStack> itemBuilder;
    boolean gui = false;
    Iterable<Texture> textures;

    public Builder(final CoverSupplier supplier) {
      this.supplier = supplier;
    }

    public Builder setTiers(Tier... tiers) {
      this.tiers = Arrays.asList(tiers);
      return this;
    }

    public Builder item(BiFunction<CoverFactory, Tier, ItemStack> item) {
      this.itemBuilder = item;
      return this;
    }

    public Builder gui() {
      this.gui = true;
      return this;
    }

    public Builder addTextures(Iterable<Texture> textures) {
      this.textures = textures;
      return this;
    }

    public Builder addTextures(Texture... textures) {
      this.textures = Arrays.asList(textures);
      return this;
    }

    public CoverFactory build(String domain, String id) {
      CoverFactory factory = new CoverFactory(domain, id, this.supplier);
      if (this.itemBuilder != null) {
        Map<Tier, ItemStack> map = new Object2ObjectOpenHashMap<>();
        for (Tier tier : this.tiers) {
          ItemStack stack = this.itemBuilder.apply(factory, tier);
          map.put(tier, stack);
        }
        factory.setItems(map);
      }
      if (gui) factory.setHasGui();
      return factory;
    }

  }

  public interface CoverSupplier {
    ICover get(ICoverHandler<?> source, @Nullable Tier tier, Direction side, CoverFactory factory);
  }

}
