package utils;

import java.io.Serializable;
import java.util.List;

public interface AggregationInterface {
	/**
	 * method that aggregates a list of content
	 * @param content 	the list of content that has to be aggregated
	 * @return			a Serializable, the aggregated object
	 */
	public Serializable aggregate(List<Serializable> content);
}
