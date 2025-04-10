package com.wsyong11.cc_ae2_support.cc;

import net.minecraft.nbt.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public final class NbtUtil {
	@Nonnull
	public static Map<String, Object> encode(@Nonnull CompoundTag tag) {
		Objects.requireNonNull(tag, "tag is null");

		Map<String, Object> result = new HashMap<>(tag.size());
		for (String key : tag.getAllKeys()) {
			Tag tagItem = tag.get(key);
			result.put(key, encode(tagItem));
		}

		return result;
	}

	@Nullable
	public static Object encode(@Nullable Tag tag) {
		if (tag == null) return null;

		if (tag instanceof NumericTag numericTag)
			return numericTag.getAsNumber();
		else if (tag instanceof LongArrayTag t)
			return Arrays.stream(t.getAsLongArray())
			             .boxed()
			             .collect(Collectors.toList());
		else if (tag instanceof IntArrayTag t)
			return Arrays.stream(t.getAsIntArray())
			             .boxed()
			             .collect(Collectors.toList());
		else if (tag instanceof ByteArrayTag t) {
			byte[] array = t.getAsByteArray();

			List<Byte> list = new ArrayList<>(array.length);
			for (byte b : array) list.add(b);

			return list;
		} else if (tag instanceof ListTag t)
			return t.stream()
			        .map(NbtUtil::encode)
			        .toList();
		else if (tag instanceof StringTag t)
			return t.toString();
		else if (tag instanceof CompoundTag t)
			return encode(t);
		else
			return null;
	}

	private NbtUtil() { /* no-op */ }
}
