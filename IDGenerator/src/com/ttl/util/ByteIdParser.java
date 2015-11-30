package com.ttl.util;

import java.nio.ByteBuffer;

import com.ttl.intf.IdParsable;

public class ByteIdParser implements IdParsable {
	
	@Override
	public String parse(byte[] idInByte) {
		ByteBuffer bb = ByteBuffer.wrap(idInByte);
		long ts = bb.getLong();
		int node_0 = bb.getInt();
		short node_1 = bb.getShort();
		short seq = bb.getShort();
		return String.format("%016d-%s%s-%04d", ts,
				Integer.toHexString(node_0), Integer.toHexString(node_1), seq);
	}
}
