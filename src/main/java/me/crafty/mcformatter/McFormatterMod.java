package me.crafty.mcformatter;

import net.fabricmc.api.ModInitializer;
import net.minecraft.network.message.MessageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McFormatterMod implements ModInitializer {

    public static final String MOD_ID = "mcformatter";
    // IMPORTANT NOTE: Must use LOGGER.info(). LOGGER.debug() does not work.
    public static final Logger LOGGER = LoggerFactory.getLogger("MC Formatter");
    public static RegistryKey<MessageType> MESSAGE_TYPE_ID = RegistryKey.of(RegistryKeys.MESSAGE_TYPE, Identifier.of("mc_formatter", "modified_message"));
    public static MinecraftServer server = null;

    @Override
    public void onInitialize() {
        LOGGER.info("Initialised!");
    }

    public static void onServerStart(MinecraftServer pServer) {
        McFormatterMod.server = pServer;
    }

    public static void onServerStop() {
        server = null;
    }
}
