package  com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
public class Card{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private boolean isFlipped;
    private String imagePath;

    public Card(){}

    public Card(String imagePath){
    this.imagePath = imagePath;
    }

}