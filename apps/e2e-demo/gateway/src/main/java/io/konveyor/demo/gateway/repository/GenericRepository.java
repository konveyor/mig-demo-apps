package io.konveyor.demo.gateway.repository;

import org.springframework.data.domain.Pageable;

public class GenericRepository {
	
	protected String getSortString(Pageable pageable) {
		if (pageable.getSort().toList().size() > 0) {
			String property = pageable.getSort().toList().get(0).getProperty();
			String order = "";
			if (pageable.getSort().toList().get(0).isAscending()) {
				order = "asc";
			} else {
				order = "desc";
			}
			return property + "," + order;
		}
		return null;
	}

}
