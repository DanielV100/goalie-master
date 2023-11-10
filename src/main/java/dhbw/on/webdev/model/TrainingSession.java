package dhbw.on.webdev.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "training_sessions")
public class TrainingSession {
    @Id
    @GeneratedValue
    public long id;

    @Column(name = "t_title")
    public String tTitle;

    @Column(name = "t_date")
    public LocalDate tDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goalkeeper_id")
    public Goalkeeper goalkeeper;

    @ElementCollection
    @CollectionTable(name = "training_sessions_goalkeepers", joinColumns = @JoinColumn(name = "training_sessions_id"))
    @Column(name = "goalkeeper_id")
    private List<Long> goalkeeperIds;

}
