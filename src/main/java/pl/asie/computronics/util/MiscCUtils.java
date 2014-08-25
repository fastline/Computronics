package pl.asie.computronics.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pl.asie.lib.util.MiscUtils;

import java.security.MessageDigest;

public class MiscCUtils {
	public static String getHashForStack(ItemStack stack, boolean includeMetadata) {
		String temp = Item.itemRegistry.getNameForObject(stack.getItem());
		if(includeMetadata) temp += ";" + stack.getItemDamage();
		try {
			byte[] data = MessageDigest.getInstance("MD5").digest(temp.getBytes());
			return MiscUtils.asHexString(data);
		} catch(Exception e) {
			return null;
		}
	}
	
	public static int getIntegerHashForStack(ItemStack stack, boolean includeMetadata) {
		String temp = Item.itemRegistry.getNameForObject(stack.getItem());
		if(includeMetadata) temp += ";" + stack.getItemDamage();
		try {
			byte[] data = MessageDigest.getInstance("MD5").digest(temp.getBytes());
			return data[0] | (data[1] << 8) | (data[2] << 16) | (data[3] << 24);
		} catch(Exception e) {
			return 0;
		}
	}
}
