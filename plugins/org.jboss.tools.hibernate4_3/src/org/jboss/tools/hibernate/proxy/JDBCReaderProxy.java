package org.jboss.tools.hibernate.proxy;

import org.hibernate.cfg.reveng.JDBCReader;
import org.hibernate.cfg.reveng.ProgressListener;
import org.jboss.tools.hibernate.spi.IDatabaseCollector;
import org.jboss.tools.hibernate.spi.IJDBCReader;
import org.jboss.tools.hibernate.spi.IMetaDataDialect;
import org.jboss.tools.hibernate.spi.IProgressListener;

public class JDBCReaderProxy implements IJDBCReader {
	
	private JDBCReader target = null;
	private IMetaDataDialect metaDataDialect = null;

	public JDBCReaderProxy(JDBCReader reader) {
		target = reader;
	}

	@Override
	public IMetaDataDialect getMetaDataDialect() {
		if (metaDataDialect == null) {
			metaDataDialect = new MetaDataDialectProxy(target.getMetaDataDialect());
		}
		return metaDataDialect;
	}

	@Override
	public void readDatabaseSchema(
			IDatabaseCollector databaseCollector,
			String defaultCatalogName, 
			String defaultSchemaName,
			IProgressListener progressListener) {
		assert databaseCollector instanceof DatabaseCollectorProxy;
		target.readDatabaseSchema(
				((DatabaseCollectorProxy)databaseCollector).getTarget(), 
				defaultCatalogName, 
				defaultSchemaName,
				new ProgressListenerImpl(progressListener));
	}
	
	private class ProgressListenerImpl implements ProgressListener {
		private IProgressListener target;
		public ProgressListenerImpl(IProgressListener progressListener) {
			target = progressListener;
		}
		@Override
		public void startSubTask(String name) {
			target.startSubTask(name);
		}		
	}

}
