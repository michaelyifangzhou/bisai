package cn.bisai.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Vedio.
 */
@Entity
@Table(name = "vedio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Vedio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",unique=true)
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "isplaying")
    private Boolean isplaying;

    @Column(name = "length")
    private Long length;
    @Column(name="CURTIME")
    private Long curtime;

    public Long getCurtime() {
        return curtime;
    }

    public void setCurtime(Long curtime) {
        this.curtime = curtime;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Vedio name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public Vedio url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isIsplaying() {
        return isplaying;
    }

    public Vedio isplaying(Boolean isplaying) {
        this.isplaying = isplaying;
        return this;
    }

    public void setIsplaying(Boolean isplaying) {
        this.isplaying = isplaying;
    }

    public Long getLength() {
        return length;
    }

    public Vedio length(Long length) {
        this.length = length;
        return this;
    }

    public void setLength(Long length) {
        this.length = length;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vedio)) {
            return false;
        }
        return id != null && id.equals(((Vedio) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Vedio{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", isplaying='" + isIsplaying() + "'" +
            ", length=" + getLength() +
            "}";
    }
}
