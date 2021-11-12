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
    private long id;

    // TODO: isFlipped macht hier keinen Sinn, weil wir Karten auch außerhalb des
    //  Spielkontexts verwenden können (z.B. Spielerprofil). Alternative?
    @Transient
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

    public void flipCard() {
        this.isFlipped = true;
    }

}