package com.example.nagoyameshi.entity;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "stores")
@Data
public class Store {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "name")
	private String name;

	@Column(name = "image_name")
	private String imageName;

	@Column(name = "business_hours")
	private String businessHours;

	@Column(name = "description")
	private String description;

	@Column(name = "postal_code")
	private String postalCode;

	@Column(name = "address")
	private String address;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "map")
	private String Map;

	@Column(name = "transportation")
	private String transportation;

	@Column(name = "pay")
	private String pay;

	@Column(name = "homepage")
	private String homePage;

	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;

	@Transient
	private Double averageScore;

	@Transient
	private List<Review> review;

}
