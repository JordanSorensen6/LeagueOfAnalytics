package classes.db;

import javax.persistence.*;

@Entity
@Table(name = "static_champions", schema = "public", catalog = "LeagueOfAnalytics")
public class StaticChampionsEntity {
    private int id;
    private String name;
    private String formatted;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "formatted", nullable = false, length = -1)
    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StaticChampionsEntity that = (StaticChampionsEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (formatted != null ? !formatted.equals(that.formatted) : that.formatted != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (formatted != null ? formatted.hashCode() : 0);
        return result;
    }
}
