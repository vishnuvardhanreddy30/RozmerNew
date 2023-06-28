package com.rozmer.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FEED")
@Getter
@Setter
public class Feed {

@Id
@GeneratedValue
@Column(name="Feed_Id")
private Long feedId;

@Column(name = "TITLE")
private String title;

@Column(name = "THUMBNIL_IMG")
private byte[] picByte;

@Column(name = "TYPE")
private String type;

@Column(name = "BODY")
private String body;

public Feed(String name, String type, byte[] picByte , String body) {
    this.title = name;
    this.type = type;
    this.picByte = picByte;
    this.body = body;
}

}
