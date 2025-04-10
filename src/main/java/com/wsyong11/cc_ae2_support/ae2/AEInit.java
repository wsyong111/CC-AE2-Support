package com.wsyong11.cc_ae2_support.ae2;

import com.wsyong11.cc_ae2_support.ae2.part.Parts;
import com.wsyong11.cc_ae2_support.ae2.part.model.PartModelRegistry;

/**
 * Initialization of AE2
 */
public final class AEInit {
	public static void init() {
		PartModelRegistry.init();
		Parts.init();
	}

	private AEInit() { /* no-op */ }
}
