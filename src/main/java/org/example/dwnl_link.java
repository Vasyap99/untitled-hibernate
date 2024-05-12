package org.example;

import javax.persistence.*;
//import org.hibernate.annotations.Entity;

@Entity
@Table(name="dwnl_link")
public class dwnl_link {
    @Id
    @GeneratedValue
    @Column(name="i")
    public int i;
    @Column(name="url")
    public String url;
    @Column(name="downl")
    public boolean downl;
    dwnl_link(String name){this.url=name;this.downl=false;}
    dwnl_link(){}
}
