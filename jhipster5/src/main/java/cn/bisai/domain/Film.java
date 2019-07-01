package cn.bisai.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Film.
 */
@Entity
@Table(name = "film")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "length")
    private Long length;

    @Column(name = "playing")
    private Boolean playing;

    @Column(name = "curtime")
    private Long curtime;

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

    public Film name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public Film url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getLength() {
        return length;
    }

    public Film length(Long length) {
        this.length = length;
        return this;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Boolean isPlaying() {
        return playing;
    }

    public Film playing(Boolean playing) {
        this.playing = playing;
        return this;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
    }

    public Long getCurtime() {
        return curtime;
    }

    public Film curtime(Long curtime) {
        this.curtime = curtime;
        return this;
    }

    public void setCurtime(Long curtime) {
        this.curtime = curtime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Film)) {
            return false;
        }
        return id != null && id.equals(((Film) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Film{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", length=" + getLength() +
            ", playing='" + isPlaying() + "'" +
            ", curtime=" + getCurtime() +
            "}";
    }
}
