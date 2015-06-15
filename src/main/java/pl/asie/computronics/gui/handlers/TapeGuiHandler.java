package pl.asie.computronics.gui.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import pl.asie.computronics.tile.TapeDriveState.State;
import pl.asie.lib.network.Packet;

import java.io.IOException;

/**
 * @author Vexatos
 */
public interface TapeGuiHandler {

	public boolean canInteractWith(EntityPlayer player);

	public boolean exists();

	public State getEnumState();

	public void switchState(State state);

	public void writeLocation(Packet packet) throws IOException;

	public int getSyncPacketID();

	public void initialize(ICrafting icrafting);

	public void sendChanges();

	public void updateFromRemote(int id, int value);
}
