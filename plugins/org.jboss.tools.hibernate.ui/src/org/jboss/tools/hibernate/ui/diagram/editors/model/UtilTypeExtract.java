/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.diagram.editors.model;

import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.jboss.tools.hibernate.spi.IType;
import org.jboss.tools.hibernate.spi.IValue;

/**
 * @author vitali
 */
public class UtilTypeExtract {

	public static IType getTypeUsingExecContext(final IValue val, final ConsoleConfiguration cfg) {
		IType type = null;
		if (val == null) {
			return type;
		}
		try {
			if (cfg != null) {
				type = (IType) cfg.execute(new Command() {
					public Object execute() {
						return val.getType();
					}
				});								
			} else {
				type = val.getType();
			}
		} catch (Exception e) {
			//type is not accessible
			// EnumType -> setParameterValues(Properties parameters) in case parameters := null NPE
			// and this is only way to catch java.lang.reflect.InvocationTargetException
			HibernateConsolePlugin.getDefault().logErrorMessage("Exception: ", e); //$NON-NLS-1$
		}
		return type;
	}
	
}
