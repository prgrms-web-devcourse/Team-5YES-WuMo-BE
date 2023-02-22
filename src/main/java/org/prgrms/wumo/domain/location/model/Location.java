package org.prgrms.wumo.domain.location.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.prgrms.wumo.domain.route.model.Route;

import lombok.Getter;

@Getter
@Entity
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Route route;

	public void addRoute(Route route) {

	}
}
