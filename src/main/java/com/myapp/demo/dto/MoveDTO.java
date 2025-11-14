package com.myapp.demo.dto;


import lombok.Data;

@Data
public class MoveDTO {
  private String newStartAt; // ISO instant
  private String newEndAt;   // ISO instant
public String getNewStartAt() {
	return newStartAt;
}
public void setNewStartAt(String newStartAt) {
	this.newStartAt = newStartAt;
}
public String getNewEndAt() {
	return newEndAt;
}
public void setNewEndAt(String newEndAt) {
	this.newEndAt = newEndAt;
}
  
}

