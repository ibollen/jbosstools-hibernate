package org.jboss.tools.hibernate.spi;


public interface IHQLQueryPlan {

	IQueryTranslator[] getTranslators();

}
