package com.ultreon.mods.ultramoons;

import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarMobSpawnInfo;
import corgitaco.enhancedcelestials.api.lunarevent.LunarTextComponents;
import corgitaco.enhancedcelestials.core.ECSounds;
import corgitaco.enhancedcelestials.lunarevent.BloodMoon;
import corgitaco.enhancedcelestials.lunarevent.client.MoonClientSettings;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Set;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(UltraMoons.MOD_ID)
public class UltraMoons {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "ultra_moons";

    public UltraMoons() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings("CodeBlock2Expr")
    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        Set<Integer> defaultSuperMoonPhases = ImmutableSet.of(0);
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(
                createLocation("ultra_blood_moon"),
                new BloodMoon(
                        new MoonClientSettings(
                                new ColorSettings("FF0000", 1.5, "FF0000", 1.5),
                                60.0F, ECSounds.BLOOD_MOON),
                        30,
                        0.00375,
                        defaultSuperMoonPhases,
                        new LunarTextComponents(
                                new CustomTranslationTextComponent("ultra_moons.name.ultra_blood_moon"),
                                new CustomTranslationTextComponent("ultra_moons.notification.ultra_blood_moon.rise", Style.EMPTY
                                        .applyFormat(ChatFormatting.RED)
                                        .applyFormat(ChatFormatting.BOLD)
                                        .applyFormat(ChatFormatting.UNDERLINE)),
                                new CustomTranslationTextComponent("ultra_moons.notification.ultra_blood_moon.set")),
                        true,
                        Util.make(new Object2DoubleArrayMap<>(), (map) -> {
                            map.put(MobCategory.MONSTER, 6.5);
                        }),
                        new LunarMobSpawnInfo(true, true, (new MobSpawnSettings.Builder()).build())));
    }

    private ResourceLocation createLocation(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("ultra-moons", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
