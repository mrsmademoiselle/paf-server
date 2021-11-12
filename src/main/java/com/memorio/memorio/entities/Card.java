package  com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Embeddable
@Entity(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private boolean isFlipped;

    @Column
    private String imagePath;

    @Deprecated
    public Card() {
    }

    public Card(String imagePath) {
        this.imagePath = imagePath;
        this.isFlipped = false;
    }

}