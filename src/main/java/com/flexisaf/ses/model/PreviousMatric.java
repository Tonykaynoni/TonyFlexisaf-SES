package com.flexisaf.ses.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name="previous_matric")
@NoArgsConstructor
@AllArgsConstructor
public class PreviousMatric {
	@Id
	public Long id;
	
	@Column(name = "pre_matric", nullable = true)
	public String preMatric ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPreMatric() {
		return preMatric;
	}

	public void setPreMatric(String preMatric) {
		this.preMatric = preMatric;
	}

	
	
}
