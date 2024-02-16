package com.example.nagoyameshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "seats")
@Data
public class Seat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  
  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;
  
  @Column(name = "seat_of_number")
  private String seatOfNumber;
  
  @Column(name = "image_name")
  private String imageName;
  
  @Column(name = "counter")
  private String counter;
  
  @Column(name = "private_room")
  private String privateRoom;
  
  @Column(name = "created_at", insertable = false, updatable = false)
  private Timestamp createdAt;
  
  @Column(name = "updated_at", insertable = false, updatable = false)
  private Timestamp updatedAt;
}
