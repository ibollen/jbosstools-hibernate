package org.jboss.tools.hibernate.proxy;

import java.util.HashSet;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;
import org.jboss.tools.hibernate.spi.IColumn;
import org.jboss.tools.hibernate.spi.IForeignKey;
import org.jboss.tools.hibernate.spi.IPrimaryKey;
import org.jboss.tools.hibernate.spi.ITable;
import org.jboss.tools.hibernate.spi.IValue;

public class TableProxy implements ITable {
	
	private Table target = null;
	private HashSet<IColumn> columns = null;
	private IValue identifierValue = null;
	private IPrimaryKey primaryKey = null;
	private HashSet<IForeignKey> foreignKeys = null;
	
	public TableProxy(Table table) {
		target = table;
	}

	@Override
	public String getName() {
		return target.getName();
	}

	public Table getTarget() {
		return target;
	}

	@Override
	public void addColumn(IColumn column) {
		assert column instanceof ColumnProxy;
		target.addColumn(((ColumnProxy)column).getTarget());
		columns = null;
	}

	@Override
	public void setPrimaryKey(IPrimaryKey pk) {
		assert pk instanceof PrimaryKeyProxy;
		primaryKey = pk;
		target.setPrimaryKey(((PrimaryKeyProxy)pk).getTarget());
	}

	@Override
	public String getCatalog() {
		return target.getCatalog();
	}

	@Override
	public String getSchema() {
		return target.getSchema();
	}

	@Override
	public IPrimaryKey getPrimaryKey() {
		if (primaryKey == null && target.getPrimaryKey() != null) {
			primaryKey = new PrimaryKeyProxy(target.getPrimaryKey());
		}
		return primaryKey;
	}

	@Override
	public Iterator<IColumn> getColumnIterator() {
		if (columns == null) {
			initializeColumns();
		}
		return columns.iterator();
	}
	
	@SuppressWarnings("unchecked")
	private void initializeColumns() {
		columns = new HashSet<IColumn>();
		Iterator<Column> iterator = target.getColumnIterator();
		while (iterator.hasNext()) {
			columns.add(new ColumnProxy(iterator.next()));
		}
	}

	@Override
	public Iterator<IForeignKey> getForeignKeyIterator() {
		if (foreignKeys == null) {
			initializeForeignKeys();
		}
		return foreignKeys.iterator();
	}
	
	private void initializeForeignKeys() {
		foreignKeys = new HashSet<IForeignKey>();
		Iterator<?> origin = target.getForeignKeyIterator();
		while (origin.hasNext()) {
			foreignKeys.add(new ForeignKeyProxy((ForeignKey)origin.next()));
		}
	}

	@Override
	public String getComment() {
		return target.getComment();
	}

	@Override
	public String getRowId() {
		return target.getRowId();
	}

	@Override
	public String getSubselect() {
		return target.getSubselect();
	}

	@Override
	public boolean hasDenormalizedTables() {
		return target.hasDenormalizedTables();
	}

	@Override
	public boolean isAbstract() {
		return target.isAbstract();
	}

	@Override
	public boolean isAbstractUnionTable() {
		return target.isAbstractUnionTable();
	}

	@Override
	public boolean isPhysicalTable() {
		return target.isPhysicalTable();
	}
	
	@Override
	public boolean equals(Object o) {
		boolean result = false;
		if (o != null & o.getClass() == getClass()) {
			result = ((TableProxy)o).getTarget().equals(getTarget());
		}
		return result;
	}

	@Override
	public IValue getIdentifierValue() {
		if (identifierValue == null && target.getIdentifierValue() != null) {
			identifierValue = new ValueProxy(target.getIdentifierValue());
		}
		return identifierValue;
	}

}
