/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.view;

import org.eclipse.jface.resource.ImageDescriptor;
import org.hibernate.console.ConsoleConfiguration;
import org.jboss.tools.hibernate.spi.IColumn;
import org.jboss.tools.hibernate.spi.IPersistentClass;
import org.jboss.tools.hibernate.spi.IProperty;
import org.jboss.tools.hibernate.spi.ITable;
import org.jboss.tools.hibernate.spi.IType;
import org.jboss.tools.hibernate.spi.IValue;
import org.jboss.tools.hibernate.ui.diagram.UiPlugin;
import org.jboss.tools.hibernate.ui.diagram.editors.model.UtilTypeExtract;

/**
 * Map: ORM object -> Image descriptor 
 */
public class OrmImageMap {
	
	private OrmImageMap() {}

	public static ImageDescriptor getImageDescriptor(final Object obj, final ConsoleConfiguration cfg) {
		String imageName = null;
		if (obj instanceof ITable) {
			imageName = getImageName((ITable)obj);
		} else if (obj instanceof IColumn) {
			imageName = getImageName((IColumn)obj);
		} else if (obj instanceof IProperty) {
			imageName = getImageName((IProperty)obj, cfg);
		} else if (obj instanceof IPersistentClass) {
			imageName = getImageName((IPersistentClass)obj);
		} else if (obj instanceof String) {
			imageName = "Image_Error"; //$NON-NLS-1$;
		} else if (obj instanceof IProperty) {
			imageName = getImageName((IProperty)obj, cfg);
		} else if (obj instanceof IValue && ((IValue)obj).isSimpleValue() || ((IValue)obj).isOneToMany()) {
			imageName = getImageName((IValue)obj);
		}
		return UiPlugin.getImageDescriptor("images/" + ImageBundle.getString(imageName)); //$NON-NLS-1$
	}

	/**
	 * the image name for hierarchy:
	 * Table
	 * @param table
	 * @return
	 */
	public static String getImageName(ITable table) {
		return "Image_DatabaseTable"; //$NON-NLS-1$
	}

	/**
	 * the image name for hierarchy:
	 * Column
	 * @param column
	 * @return
	 */
	public static String getImageName(IColumn column) {
		String str = "Image_DatabaseColumn"; //$NON-NLS-1$
		final boolean primaryKey = HibernateUtils.isPrimaryKey(column);
		final boolean foreignKey = HibernateUtils.isForeignKey(column);
		final ITable table = HibernateUtils.getTable(column);
		if (column.isUnique()) {
			str = "Image_DatabaseUniqueKeyColumn"; //$NON-NLS-1$
		} else if (primaryKey && table != null && foreignKey) {
			str = "Image_DatabasePrimaryForeignKeysColumn"; //$NON-NLS-1$
		} else if (primaryKey) {
			str = "Image_DatabasePrimaryKeyColumn"; //$NON-NLS-1$
		} else if (table != null && foreignKey) {
			str = "Image_DatabaseForeignKeyColumn"; //$NON-NLS-1$
		}
		return str;

	}

	/**
	 * the image name for hierarchy:
	 * Property
	 * @param field
	 * @return
	 */
	public static String getImageName(IProperty field, final ConsoleConfiguration cfg) {
		String str = "Image_PersistentFieldSimple"; //$NON-NLS-1$
		if (field == null) {
			return str;
		}
		final IPersistentClass persistentClass = field.getPersistentClass(); 
		if (persistentClass != null && persistentClass.getVersion() == field) {
			str = "Image_PersistentFieldSimple_version"; //$NON-NLS-1$
		} else if (persistentClass != null && persistentClass.getIdentifierProperty() == field) {
			str = "Image_PersistentFieldSimple_id"; //$NON-NLS-1$
		} else if (field.getValue() != null) {
			final IValue value = field.getValue();
			if (value.isOneToMany()) {
				str = "Image_PersistentFieldOne-to-many"; //$NON-NLS-1$
			} else if (value.isOneToOne()) {
				str = "Image_PersistentFieldOne-to-one"; //$NON-NLS-1$
			} else if (value.isManyToOne()) {
				str = "Image_PersistentFieldMany-to-one"; //$NON-NLS-1$
			} else if (value.isAny()) {
				str = "Image_PersistentFieldAny"; //$NON-NLS-1$
			} else {
				IType type = UtilTypeExtract.getTypeUsingExecContext(value, cfg);
				if (type != null && type.isCollectionType()) {
					if (value.isPrimitiveArray()) {
						str = "Image_Collection_primitive_array"; //$NON-NLS-1$
					} else if (value.isArray()) {
						str = "Image_Collection_array"; //$NON-NLS-1$
					} else if (value.isList()) {
						str = "Image_Collection_list"; //$NON-NLS-1$
					} else if (value.isSet()) {
						str = "Image_Collection_set"; //$NON-NLS-1$
					} else if (value.isMap()) {
						str = "Image_Collection_map"; //$NON-NLS-1$
					} else if (value.isBag()) {
						str = "Image_Collection_bag"; //$NON-NLS-1$
					} else if (value.isIdentifierBag()) {
						str = "Image_Collection_idbag"; //$NON-NLS-1$
					} else {
						str = "Image_Collection"; //$NON-NLS-1$
					}
				}
			}
		} else if ("parent".equals(field.getName())) { //$NON-NLS-1$
			str = "Image_PersistentFieldParent"; //$NON-NLS-1$
		}
		return str;
	}

	/**
	 * the image name for hierarchy:
	 * SimpleValue
	 * |-- Any
	 * |-- Component 
	 * |-- DependantValue
	 * |-- ToOne
	 *     |-- ManyToOne
	 *     |-- OneToOne
	 * @param field
	 * @return
	 */
	public static String getImageName(IValue field) {
		String res = "Image_PersistentFieldSimple"; //$NON-NLS-1$
		if (field.isAny()) {
			res = "Image_PersistentFieldMany-to-any"; //$NON-NLS-1$
		} else if (field.isComponent()) {
			res = "Image_PersistentFieldComponent"; //$NON-NLS-1$
		} else if (field.isDependantValue()) {
       		if (field.getTable().getIdentifierValue() == field) {
				res = "Image_PersistentFieldComponent_id"; //$NON-NLS-1$				
			}
		} else if (field.isManyToOne()) {
			res = "Image_PersistentFieldMany-to-many"; //$NON-NLS-1$
		} else if (field.isOneToMany()) {
			res = "Image_PersistentFieldOne-to-many";  //$NON-NLS-1$
		}
		return res;
	}

	/**
	 * the image name for hierarchy:
	 * PersistentClass
	 * |-- RootClass
	 * |   |-- SpecialRootClass
	 * |
	 * |-- Subclass 
	 *     |-- JoinedSubclass
	 *     |-- SingleTableSubclass
	 *     |-- UnionSubclass
	 * @param persistentClass
	 * @return
	 */
	public static String getImageName(IPersistentClass persistentClass) {
		return "Image_PersistentClass"; //$NON-NLS-1$
	}

}
