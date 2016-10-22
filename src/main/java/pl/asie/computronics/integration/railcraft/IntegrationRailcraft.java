package pl.asie.computronics.integration.railcraft;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.asie.computronics.Computronics;
import pl.asie.computronics.integration.railcraft.block.BlockDigitalDetector;
import pl.asie.computronics.integration.railcraft.block.BlockLocomotiveRelay;
import pl.asie.computronics.integration.railcraft.block.BlockTicketMachine;
import pl.asie.computronics.integration.railcraft.gui.GuiProviderTicketMachine;
import pl.asie.computronics.integration.railcraft.item.ItemRelaySensor;
import pl.asie.computronics.integration.railcraft.tile.TileDigitalDetector;
import pl.asie.computronics.integration.railcraft.tile.TileLocomotiveRelay;
import pl.asie.computronics.integration.railcraft.tile.TileTicketMachine;
import pl.asie.computronics.reference.Mods;
import pl.asie.lib.network.Packet;

import java.io.IOException;

/**
 * @author Vexatos
 */
public class IntegrationRailcraft {

	public BlockLocomotiveRelay locomotiveRelay;
	public BlockDigitalDetector detector;
	public ItemRelaySensor relaySensor;
	public Block digitalReceiverBox;
	public Block digitalControllerBox;
	public BlockTicketMachine ticketMachine;

	LocomotiveManager manager;
	public GuiProviderTicketMachine guiTicketMachine;

	private static boolean isEnabled(Configuration config, String name, boolean def) {
		return config.get("enable.railcraft", name, def).getBoolean(def);
	}

	public void preInit(Configuration config) {
		if(isEnabled(config, "locomotiveRelay", true)) {
			locomotiveRelay = new BlockLocomotiveRelay();
			Computronics.instance.registerBlockWithTileEntity(locomotiveRelay, TileLocomotiveRelay.class, "locomotive_relay");
			//IntegrationBuildCraftBuilder.INSTANCE.registerBlockBaseSchematic(locomotiveRelay); TODO BuildCraft

			relaySensor = new ItemRelaySensor();
			Computronics.instance.registerItem(relaySensor, "relay_sensor");

			manager = new LocomotiveManager();
			MinecraftForge.EVENT_BUS.register(manager);
		}
		/*if(isEnabled(config, "digitalReceiverBox", true)) { TODO Railcraft 10.1
			this.digitalReceiverBox = new BlockDigitalReceiverBox();
			Computronics.instance.registerBlockWithTileEntity(digitalReceiverBox, new ItemBlockSignalBox(digitalReceiverBox), TileDigitalReceiverBox.class, "digital_receiver_box");
		}
		if(isEnabled(config, "digitalControllerBox", true)) {
			this.digitalControllerBox = new BlockDigitalControllerBox();
			Computronics.instance.registerBlockWithTileEntity(digitalControllerBox, new ItemBlockSignalBox(digitalControllerBox), TileDigitalControllerBox.class, "digital_controller_box");
		}*/
		if(isEnabled(config, "digitalDetector", true)) {
			detector = new BlockDigitalDetector();
			Computronics.instance.registerBlockWithTileEntity(detector, TileDigitalDetector.class, "digital_detector");
			//IntegrationBuildCraftBuilder.INSTANCE.registerBlockBaseSchematic(detector); TODO BuildCraft
		}
		if(isEnabled(config, "ticketMachine", true)) {
			this.guiTicketMachine = new GuiProviderTicketMachine();
			Computronics.gui.registerGuiProvider(guiTicketMachine);
			ticketMachine = new BlockTicketMachine();
			Computronics.instance.registerBlockWithTileEntity(ticketMachine, TileTicketMachine.class, "ticket_machine");
			//IntegrationBuildCraftBuilder.INSTANCE.registerBlockBaseSchematic(ticketMachine); TODO BuildCraft
		}
	}

	@Optional.Method(modid = Mods.Railcraft)
	public void onMessageRailcraft(Packet packet, EntityPlayer player, boolean isServer) throws IOException {
		TileEntity entity = isServer ? packet.readTileEntityServer() : packet.readTileEntity();
		if(entity instanceof TileTicketMachine) {
			TileTicketMachine machine = (TileTicketMachine) entity;
			int i = packet.readInt();
			machine.setLocked((i & 1) == 1, isServer);
			machine.setSelectionLocked(((i >> 1) & 1) == 1, isServer);
			machine.setPrintLocked((((i >> 2) & 1) == 1), isServer);
			machine.setActive((((i >> 3) & 1) == 1), isServer);
			machine.setSelectedSlot(packet.readInt(), isServer);
		}
	}

	@Optional.Method(modid = Mods.Railcraft)
	public void printTicket(Packet packet, EntityPlayer player, boolean isServer) throws IOException {
		TileEntity entity = isServer ? packet.readTileEntityServer() : packet.readTileEntity();
		if(entity instanceof TileTicketMachine) {
			((TileTicketMachine) entity).printTicket();
		}
	}

	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = Mods.Railcraft)
	public void registerRenderers() {
		//ClientRegistry.bindTileEntitySpecialRenderer(TileDigitalReceiverBox.class, new TESRSignalBox());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileDigitalControllerBox.class, new TESRSignalBox());
		ModelBakery.registerItemVariants(relaySensor, new ResourceLocation(Mods.Computronics + ":relay_sensor_off"));
		ModelBakery.registerItemVariants(relaySensor, new ResourceLocation(Mods.Computronics + ":relay_sensor_on"));
		ModelLoader.setCustomMeshDefinition(relaySensor, new ItemRelaySensor.MeshDefinition());
	}
}
