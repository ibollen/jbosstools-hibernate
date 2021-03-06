package org.jboss.tools.hibernate.proxy;

import org.hibernate.engine.spi.Mapping;
import org.jboss.tools.hibernate.spi.IMapping;

public class MappingProxy implements IMapping {
	
	private Mapping target = null;
	
	public MappingProxy(Mapping m) {
		target = m;
	}

	Mapping getTarget() {
		return target;
	}

}
